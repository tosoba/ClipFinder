package com.example.there.findclips.fragment.player.youtube

import android.arch.lifecycle.Lifecycle
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentYoutubePlayerBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.player.IPlayerFragment
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.slidingPanelController
import com.example.there.findclips.view.OnYoutubePlayerStateChangeListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import kotlinx.android.synthetic.main.fragment_youtube_player.*


class YoutubePlayerFragment :
        Fragment(),
        IPlayerFragment,
        Injectable {

    private var youTubePlayer: YouTubePlayer? = null

    private val onYoutubePlayerPlayPauseBtnClickListener: View.OnClickListener = View.OnClickListener {
        if (youTubePlayer == null || (lastPlayedVideo == null && lastVideoPlaylist == null)) return@OnClickListener
        if (youtubePlaybackInProgress) youTubePlayer?.pause()
        else youTubePlayer?.play()
    }

    private val onYoutubePlayerCloseBtnClickListener: View.OnClickListener = View.OnClickListener {
        slidingPanelController?.hideIfVisible()
        youTubePlayer?.pause()
        lastPlayedVideo = null
        lastVideoPlaylist = null
    }

    private val fragmentView: YoutubePlayerView by lazy(LazyThreadSafetyMode.NONE) {
        YoutubePlayerView(onYoutubePlayerCloseBtnClickListener, onYoutubePlayerPlayPauseBtnClickListener)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentYoutubePlayerBinding>(
            inflater,
            R.layout.fragment_youtube_player,
            container,
            false
    ).apply {
        playerView = fragmentView
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
        youtube_player_view?.playerUIController?.showUI(true)
    }

    override fun onCollapsed() {
        youtube_player_collapsed_controls_group?.visibility = View.VISIBLE
        youtube_player_view?.playerUIController?.showUI(false)
    }

    override fun onHidden() {
        youTubePlayer?.pause()
        lastPlayedVideo = null
    }

    override fun stopPlayback() {
        youTubePlayer?.pause()
        lastPlayedVideo = null
        lastVideoPlaylist = null
    }

    fun onPlayerDimensionsChange(slideOffset: Float) {
        val youtubePlayerGuidelinePercentage = (1 - minimumYoutubePlayerGuidelinePercent) * slideOffset + minimumYoutubePlayerGuidelinePercent
        youtube_player_guideline.setGuidelinePercent(youtubePlayerGuidelinePercentage)
        youtube_player_guideline?.requestLayout()
    }

    private val closeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(context).apply {
            setImageResource(R.drawable.close)
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(10, 10, 10, 10)
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
            setOnClickListener(onYoutubePlayerCloseBtnClickListener)
            setBackgroundColor(Color.TRANSPARENT)
        }
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

    var lastPlayedVideo: Video? = null
        private set

    private var youtubePlaybackInProgress: Boolean = false

    private var lastVideoPlaylist: VideoPlaylist? = null
    private var videosToPlay: List<Video>? = null
    private var currentVideoIndex = 0

    fun loadVideo(video: Video) {
        if (video == lastPlayedVideo) return
        lastPlayedVideo = video

        lastVideoPlaylist = null


        youTubePlayer?.removeListener(playlistYoutubePlayerStateChangeListener)
        playVideo(video)

        youtube_player_view?.playerUIController?.setVideoTitle(video.title)
    }

    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (videoPlaylist == lastVideoPlaylist) return

        lastVideoPlaylist = videoPlaylist
        videosToPlay = videos
        currentVideoIndex = 0

        lastPlayedVideo = null

        youTubePlayer?.addListener(playlistYoutubePlayerStateChangeListener)

        val firstVideo = videos.first()
        playVideo(firstVideo)
        youtube_player_view?.playerUIController?.setVideoTitle(firstVideo.title)
    }

    private fun playVideo(video: Video) {
        youtube_player_video_title_when_collapsed_txt?.text = video.title
        if (lifecycle.currentState == Lifecycle.State.RESUMED) youTubePlayer?.loadVideo(video.id, 0f)
        else youTubePlayer?.cueVideo(video.id, 0f)
    }

    private val playlistYoutubePlayerStateChangeListener = object : OnYoutubePlayerStateChangeListener {
        override fun onStateChange(state: PlayerConstants.PlayerState) {
            when (state) {
                PlayerConstants.PlayerState.ENDED -> {
                    if (videosToPlay?.size ?: 0 > ++currentVideoIndex) {
                        playVideo(videosToPlay!![currentVideoIndex])
                    } else {
                        Toast.makeText(context, "${lastVideoPlaylist?.name
                                ?: "Unknown playlist"} has ended.", Toast.LENGTH_SHORT).show()
                        slidingPanelController?.hideIfVisible()
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
                    youtubePlaybackInProgress = true
                    youtube_player_play_pause_when_collapsed_btn?.setImageResource(R.drawable.pause)
                }

                PlayerConstants.PlayerState.PAUSED -> {
                    youtubePlaybackInProgress = false
                    youtube_player_play_pause_when_collapsed_btn?.setImageResource(R.drawable.play)
                }

                else -> return
            }
        }
    }

    companion object {
        private const val minimumYoutubePlayerGuidelinePercent = .45f
    }
}
