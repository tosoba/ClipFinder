package com.example.youtubeplayer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.example.core.ext.castAs
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.IYoutubePlayerFragment
import com.example.coreandroid.base.handler.SlidingPanelController
import com.example.coreandroid.model.videos.Video
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.coreandroid.util.ext.dpToPx
import com.example.youtubeplayer.databinding.FragmentYoutubePlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import kotlinx.android.synthetic.main.fragment_youtube_player.*


class YoutubePlayerFragment :
    BaseVMFragment<YoutubePlayerViewModel>(YoutubePlayerViewModel::class),
    IYoutubePlayerFragment {

    override val playerView: View? get() = view

    private var youTubePlayer: YouTubePlayer? = null

    private val onYoutubePlayerPlayPauseBtnClickListener: View.OnClickListener = View.OnClickListener {
        if (youTubePlayer == null || (viewModel.playerState.lastPlayedVideo == null && viewModel.playerState.lastVideoPlaylist == null))
            return@OnClickListener
        if (viewModel.playerState.youtubePlaybackInProgress) youTubePlayer?.pause()
        else youTubePlayer?.play()
    }

    private val onYoutubePlayerCloseBtnClickListener: View.OnClickListener = View.OnClickListener {
        activity?.castAs<SlidingPanelController>()?.hideIfVisible()
        stopPlaybackAndNullifyLastPlayedItems()
    }

    private val closeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(context).apply {
            setImageResource(R.drawable.close)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10, 10, 10, 10)
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
            setOnClickListener(onYoutubePlayerCloseBtnClickListener)
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private val fragmentView: YoutubePlayerView by lazy(LazyThreadSafetyMode.NONE) {
        YoutubePlayerView(onYoutubePlayerCloseBtnClickListener, onYoutubePlayerPlayPauseBtnClickListener)
    }

    private val playlistYoutubePlayerStateChangeListener = object : OnYoutubePlayerStateChangeListener {
        override fun onStateChange(state: PlayerConstants.PlayerState) {
            when (state) {
                PlayerConstants.PlayerState.ENDED -> {
                    if (viewModel.playerState.videosToPlay?.size ?: 0 > ++viewModel.playerState.currentVideoIndex) {
                        playVideo(viewModel.playerState.videosToPlay!![viewModel.playerState.currentVideoIndex])
                    } else {
                        Toast.makeText(context, "${viewModel.playerState.lastVideoPlaylist?.name
                            ?: "Unknown playlist"} has ended.", Toast.LENGTH_SHORT).show()
                        activity?.castAs<SlidingPanelController>()?.hideIfVisible()
                    }
                }
                else -> return
            }
        }
    }

    private val singleVideoYoutubePlayerStateChangeListener = object : OnYoutubePlayerStateChangeListener {
        override fun onStateChange(state: PlayerConstants.PlayerState) {
            when (state) {
                PlayerConstants.PlayerState.PLAYING -> {
                    viewModel.playerState.youtubePlaybackInProgress = true
                    youtube_player_play_pause_when_collapsed_btn?.setImageResource(R.drawable.pause)
                }

                PlayerConstants.PlayerState.PAUSED -> {
                    viewModel.playerState.youtubePlaybackInProgress = false
                    youtube_player_play_pause_when_collapsed_btn?.setImageResource(R.drawable.play)
                }

                else -> return
            }
        }
    }

    override val lastPlayedVideo: Video? get() = viewModel.playerState.lastPlayedVideo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentYoutubePlayerBinding>(
        inflater, R.layout.fragment_youtube_player, container, false
    ).apply {
        fragmentView = this@YoutubePlayerFragment.fragmentView
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initYouTubePlayerView()
        initPlayerViewControls(view)
    }

    override fun onDragging() {
        youtube_player_collapsed_controls_group?.visibility = View.GONE
    }

    override fun onExpanded() {
        youtube_player_collapsed_controls_group?.visibility = View.GONE
        youtube_player_view?.playerUIController?.showUI(true)
    }

    override fun onCollapsed() {
        youtube_player_collapsed_controls_group?.visibility = View.VISIBLE
        youtube_player_view?.playerUIController?.showUI(false)
    }

    override fun onHidden() = stopPlaybackAndNullifyLastPlayedItems()

    override fun stopPlayback() = stopPlaybackAndNullifyLastPlayedItems()

    override fun onPlayerDimensionsChange(slideOffset: Float) {
        val youtubePlayerGuidelinePercentage = (1 - minimumYoutubePlayerGuidelinePercent) * slideOffset + minimumYoutubePlayerGuidelinePercent
        youtube_player_guideline.setGuidelinePercent(youtubePlayerGuidelinePercentage)
        youtube_player_guideline?.requestLayout()
    }

    override fun loadVideo(video: Video) {
        if (video == viewModel.playerState.lastPlayedVideo) return

        viewModel.onLoadVideo(video)

        youTubePlayer?.removeListener(playlistYoutubePlayerStateChangeListener)
        playVideo(video)

        youtube_player_view?.playerUIController?.setVideoTitle(video.title)
    }

    override fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (videoPlaylist == viewModel.playerState.lastVideoPlaylist) return

        viewModel.onLoadVideoPlaylist(videoPlaylist, videos)

        youTubePlayer?.addListener(playlistYoutubePlayerStateChangeListener)

        val firstVideo = videos.first()
        playVideo(firstVideo)
        youtube_player_view?.playerUIController?.setVideoTitle(firstVideo.title)
    }

    private fun stopPlaybackAndNullifyLastPlayedItems() {
        youTubePlayer?.pause()
        viewModel.playerState.lastPlayedVideo = null
        viewModel.playerState.lastVideoPlaylist = null
    }

    private fun initPlayerViewControls(view: View) = view.findViewById<RelativeLayout>(R.id.controls_root).apply {
        addView(closeBtn)
        val titleParams = findViewById<TextView>(R.id.video_title)?.layoutParams as RelativeLayout.LayoutParams
        titleParams.setMargins(view.context.dpToPx(20f).toInt(), 0, view.context.dpToPx(20f).toInt(), 0)
    }

    private fun initYouTubePlayerView() = with(youtube_player_view) {
        lifecycle.addObserver(this)
        initialize({
            youTubePlayer = it
            youTubePlayer?.addListener(singleVideoYoutubePlayerStateChangeListener)
        }, true)
        playerUIController.showFullscreenButton(false)
        playerUIController.showVideoTitle(true)
    }

    private fun playVideo(video: Video) {
        youtube_player_video_title_when_collapsed_txt?.text = video.title
        if (lifecycle.currentState == Lifecycle.State.RESUMED) youTubePlayer?.loadVideo(video.id, 0f)
        else youTubePlayer?.cueVideo(video.id, 0f)
    }

    companion object {
        private const val minimumYoutubePlayerGuidelinePercent = .45f
    }
}
