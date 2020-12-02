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
import androidx.lifecycle.Lifecycle
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.youtube.player.OnYoutubePlayerStateChangeListener
import com.clipfinder.core.ext.castAs
import com.example.core.android.base.fragment.IYoutubePlayerFragment
import com.example.core.android.base.handler.SlidingPanelController
import com.example.core.android.model.videos.Video
import com.example.core.android.model.videos.VideoPlaylist
import com.example.core.android.util.ext.dpToPx
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import kotlinx.android.synthetic.main.fragment_youtube_player.*
import kotlinx.android.synthetic.main.fragment_youtube_player.view.*

class YoutubePlayerFragment : BaseMvRxFragment(), IYoutubePlayerFragment {
    private val viewModel: YoutubePlayerViewModel by fragmentViewModel()
    private lateinit var youTubePlayer: YouTubePlayer

    override val playerView: View?
        get() = view
    override val lastPlayedVideo: Video?
        get() = withState(viewModel, YoutubePlayerState::lastPlayedVideo)

    private val onYoutubePlayerPlayPauseBtnClickListener: View.OnClickListener = View.OnClickListener {
        withState(viewModel) { state ->
            if (state.lastPlayedVideo == null && state.lastPlayedVideoPlaylist == null) return@withState
            if (state.playbackInProgress) youTubePlayer.pause()
            else youTubePlayer.play()
        }
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

    private val playlistYoutubePlayerStateChangeListener = object : OnYoutubePlayerStateChangeListener {
        override fun onStateChange(playerState: PlayerConstants.PlayerState) {
            when (playerState) {
                PlayerConstants.PlayerState.ENDED -> withState(viewModel) { state ->
                    if (state.playlistVideos.size > state.currentPlaylistVideoIndex + 1) {
                        playVideo(state.playlistVideos[state.currentPlaylistVideoIndex + 1])
                        viewModel.onNextVideoFromPlaylistStarted()
                    } else {
                        Toast.makeText(context, "${
                            state.lastPlayedVideoPlaylist?.name ?: "Unknown playlist"
                        } has ended.", Toast.LENGTH_SHORT).show()
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
                    viewModel.updatePlaybackState(inProgress = true)
                    youtube_player_play_pause_when_collapsed_btn?.setImageResource(R.drawable.pause)
                }

                PlayerConstants.PlayerState.PAUSED, PlayerConstants.PlayerState.ENDED -> {
                    viewModel.updatePlaybackState(inProgress = false)
                    youtube_player_play_pause_when_collapsed_btn?.setImageResource(R.drawable.play)
                }

                else -> return
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_youtube_player, container, false)
        .apply {
            this.close_youtube_player_when_collapsed_btn
                .setOnClickListener(onYoutubePlayerCloseBtnClickListener)
            this.youtube_player_play_pause_when_collapsed_btn
                .setOnClickListener(onYoutubePlayerPlayPauseBtnClickListener)
        }

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
        youtube_player_guideline.setGuidelinePercent(
            (1 - minimumYoutubePlayerGuidelinePercent) * slideOffset + minimumYoutubePlayerGuidelinePercent
        )
        youtube_player_guideline.requestLayout()
    }

    override fun loadVideo(video: Video) {
        if (withState(viewModel) { state -> video == state.lastPlayedVideo }) return

        viewModel.onLoadVideo(video)

        youTubePlayer.removeListener(playlistYoutubePlayerStateChangeListener)
        playVideo(video)

        youtube_player_view?.playerUIController?.setVideoTitle(video.title)
    }

    override fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (withState(viewModel) { state -> videoPlaylist == state.lastPlayedVideoPlaylist }) return

        viewModel.onLoadVideoPlaylist(videoPlaylist, videos)

        youTubePlayer.addListener(playlistYoutubePlayerStateChangeListener)

        val firstVideo = videos.first()
        playVideo(firstVideo)
        youtube_player_view?.playerUIController?.setVideoTitle(firstVideo.title)
    }

    override fun invalidate() = Unit

    private fun stopPlaybackAndNullifyLastPlayedItems() {
        youTubePlayer.pause()
        viewModel.clearLastPlayed()
    }

    private fun initPlayerViewControls(view: View) {
        view.findViewById<RelativeLayout>(R.id.controls_root).apply {
            addView(closeBtn)
            val titleParams = findViewById<TextView>(R.id.video_title)?.layoutParams as RelativeLayout.LayoutParams
            val margin20 = view.context.dpToPx(20f).toInt()
            titleParams.setMargins(margin20, 0, margin20, 0)
        }
    }

    private fun initYouTubePlayerView() {
        with(youtube_player_view) {
            lifecycle.addObserver(this)
            initialize({
                youTubePlayer = it
                it.addListener(singleVideoYoutubePlayerStateChangeListener)
            }, true)
            playerUIController.showFullscreenButton(false)
            playerUIController.showVideoTitle(true)
        }
    }

    private fun playVideo(video: Video) {
        youtube_player_video_title_when_collapsed_txt?.text = video.title
        if (lifecycle.currentState == Lifecycle.State.RESUMED) youTubePlayer.loadVideo(video.id, 0f)
        else youTubePlayer.cueVideo(video.id, 0f)
    }

    companion object {
        private const val minimumYoutubePlayerGuidelinePercent = .45f
    }
}
