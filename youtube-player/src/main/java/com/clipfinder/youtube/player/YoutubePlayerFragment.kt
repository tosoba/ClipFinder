package com.clipfinder.youtube.player

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.base.fragment.IYoutubePlayerFragment
import com.clipfinder.core.android.base.handler.SlidingPanelController
import com.clipfinder.core.android.base.provider.IntentProvider
import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist
import com.clipfinder.core.android.util.ext.*
import com.clipfinder.core.ext.castAs
import com.clipfinder.core.notification.PlaybackNotification
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_youtube_player.*
import kotlinx.android.synthetic.main.fragment_youtube_player.view.*

class YoutubePlayerFragment : BaseMvRxFragment(), IYoutubePlayerFragment {
    private val viewModel: YoutubePlayerViewModel by fragmentViewModel()
    private val broadcastReceivers: ArrayList<BroadcastReceiver> = ArrayList(5)
    private lateinit var youTubePlayer: YouTubePlayer

    override val playerView: View?
        get() = view

    private val onPlayPauseBtnClickListener: View.OnClickListener =
        View.OnClickListener {
            withState(viewModel) { state ->
                if (state.mode is YoutubePlayerMode.Idle) return@withState
                if (state.playbackInProgress) youTubePlayer.pause() else youTubePlayer.play()
            }
        }

    private val onCloseBtnClickListener: View.OnClickListener =
        View.OnClickListener {
            activity?.castAs<SlidingPanelController>()?.hideIfVisible()
            stopPlaybackAndNullifyLastPlayedItems()
        }

    private val closeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(context).apply {
            setImageResource(R.drawable.close)
            layoutParams =
                RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    .apply {
                        setMargins(10, 10, 10, 10)
                        addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
            setOnClickListener(onCloseBtnClickListener)
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private val playlistYoutubePlayerStateChangeListener =
        object : AbstractYouTubePlayerListener() {
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.ENDED ->
                        withState(viewModel) { (_, mode) ->
                            require(mode is YoutubePlayerMode.Playlist)
                            playNextVideo(mode)
                            refreshBackgroundPlaybackNotificationIfShowing()
                        }
                    else -> return
                }
            }
        }

    private val singleVideoYoutubePlayerStateChangeListener =
        object : AbstractYouTubePlayerListener() {
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.PLAYING -> {
                        viewModel.updatePlaybackState(inProgress = true)
                        youtube_player_play_pause_when_collapsed_btn?.setImageResource(
                            R.drawable.pause
                        )
                        refreshBackgroundPlaybackNotificationIfShowing()
                    }
                    PlayerConstants.PlayerState.PAUSED, PlayerConstants.PlayerState.ENDED -> {
                        viewModel.updatePlaybackState(inProgress = false)
                        youtube_player_play_pause_when_collapsed_btn?.setImageResource(
                            R.drawable.play
                        )
                        refreshBackgroundPlaybackNotificationIfShowing()
                    }
                    else -> return
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_youtube_player, container, false).apply {
            this.close_youtube_player_when_collapsed_btn.setOnClickListener(onCloseBtnClickListener)
            this.youtube_player_play_pause_when_collapsed_btn.setOnClickListener(
                onPlayPauseBtnClickListener
            )
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initYouTubePlayerView()
        initPlayerViewControls(view)
    }

    override fun onStart() {
        super.onStart()
        cancelPlaybackNotification()
    }

    override fun onStop() {
        showPlaybackNotification()
        super.onStop()
    }

    override fun onDestroyView() {
        youtube_player_view?.release()
        super.onDestroyView()
    }

    override fun onDestroy() {
        with(requireContext()) {
            notificationManager.cancelAll()
            broadcastReceivers.forEach(this::unregisterReceiver)
        }
        super.onDestroy()
    }

    override fun onDragging() {
        youtube_player_collapsed_controls_group?.visibility = View.GONE
    }

    override fun onExpanded() {
        youtube_player_collapsed_controls_group?.visibility = View.GONE
        youtube_player_view?.getPlayerUiController()?.showUi(true)
    }

    override fun onCollapsed() {
        youtube_player_collapsed_controls_group?.visibility = View.VISIBLE
        youtube_player_view?.getPlayerUiController()?.showUi(false)
    }

    override fun onHidden() {
        stopPlaybackAndNullifyLastPlayedItems()
    }

    override fun stopPlayback() {
        stopPlaybackAndNullifyLastPlayedItems()
    }

    override fun onPlayerDimensionsChange(slideOffset: Float) {
        youtube_player_guideline.setGuidelinePercent(
            (1 - minYoutubePlayerGuidelinePercent) * slideOffset + minYoutubePlayerGuidelinePercent
        )
        youtube_player_guideline.requestLayout()
    }

    override fun loadVideo(video: Video) {
        if (viewModel.isAlreadyPlaying(video)) return
        viewModel.onLoadVideo(video)
        youTubePlayer.removeListener(playlistYoutubePlayerStateChangeListener)
        playVideo(video)
        youtube_player_view?.getPlayerUiController()?.setVideoTitle(video.title)
    }

    override fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (viewModel.isAlreadyPlaying(videoPlaylist)) return
        if (videos.isEmpty()) {
            Toast.makeText(context, "Playlist ${videoPlaylist.name} is empty.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        viewModel.onLoadVideoPlaylist(videoPlaylist, videos)
        youTubePlayer.addListener(playlistYoutubePlayerStateChangeListener)
        val firstVideo = videos.first()
        playVideo(firstVideo)
        youtube_player_view?.getPlayerUiController()?.setVideoTitle(firstVideo.title)
    }

    override fun invalidate() = Unit

    private fun stopPlaybackAndNullifyLastPlayedItems() {
        youTubePlayer.pause()
        viewModel.clearLastPlayed()
    }

    private fun initPlayerViewControls(view: View) {
        view.findViewById<RelativeLayout>(R.id.controls_container).apply {
            addView(closeBtn)
            val titleParams =
                findViewById<TextView>(R.id.video_title).layoutParams as RelativeLayout.LayoutParams
            val margin20Px = view.context.dpToPx(20f).toInt()
            titleParams.setMargins(margin20Px, 0, margin20Px, 0)
        }
    }

    private fun initYouTubePlayerView() {
        youtube_player_view?.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@YoutubePlayerFragment.youTubePlayer = youTubePlayer
                    youTubePlayer.addListener(singleVideoYoutubePlayerStateChangeListener)
                    youtube_player_view?.getPlayerUiController()?.showFullscreenButton(false)
                    youtube_player_view?.getPlayerUiController()?.showVideoTitle(true)
                }
            }
        )
        youtube_player_view?.enableBackgroundPlayback(true)
        with(requireContext()) {
            broadcastReceivers.addAll(
                createAndRegisterReceiverFor(
                    IntentFilter(PlaybackNotification.ACTION_DELETE_NOTIFICATION)
                ) { _, _ ->
                    viewModel.updatePlayerNotificationState(false)
                    stopPlayback()
                },
                createAndRegisterReceiverFor(
                    YoutubePlayerNotificationAction.YOUTUBE_ACTION_PAUSE_PLAYBACK.filter
                ) { _, _ -> youTubePlayer.pause() },
                createAndRegisterReceiverFor(
                    YoutubePlayerNotificationAction.YOUTUBE_ACTION_RESUME_PLAYBACK.filter
                ) { _, _ -> youTubePlayer.play() },
                createAndRegisterReceiverFor(
                    YoutubePlayerNotificationAction.YOUTUBE_ACTION_PREV_VIDEO.filter
                ) { _, _ ->
                    playPrevVideo(
                        withState(viewModel) { state -> state.mode as YoutubePlayerMode.Playlist }
                    )
                },
                createAndRegisterReceiverFor(
                    YoutubePlayerNotificationAction.YOUTUBE_ACTION_NEXT_VIDEO.filter
                ) { _, _ ->
                    playNextVideo(
                        withState(viewModel) { state -> state.mode as YoutubePlayerMode.Playlist }
                    )
                }
            )
        }
    }

    private fun playVideo(video: Video) {
        youtube_player_video_title_when_collapsed_txt?.text = video.title
        if (lifecycle.currentState == Lifecycle.State.RESUMED) youTubePlayer.loadVideo(video.id, 0f)
        else youTubePlayer.cueVideo(video.id, 0f)
    }

    private fun playNextVideo(mode: YoutubePlayerMode.Playlist) {
        if (mode.videos.size > mode.currentVideoIndex + 1) {
            playVideo(mode.videos[mode.currentVideoIndex + 1])
            viewModel.onNextVideoFromPlaylistStarted()
        } else {
            Toast.makeText(context, "${mode.playlist.name} has ended.", Toast.LENGTH_SHORT).show()
            activity?.castAs<SlidingPanelController>()?.hideIfVisible()
        }
    }

    private fun playPrevVideo(mode: YoutubePlayerMode.Playlist) {
        require(mode.currentVideoIndex - 1 < 0)
        playVideo(mode.videos[mode.currentVideoIndex - 1])
        viewModel.onNextVideoFromPlaylistStarted()
    }

    private fun showPlaybackNotification() {
        withState(viewModel) { state ->
            val currentVideo = state.currentVideo ?: return@withState
            viewModel.updatePlayerNotificationState(true)
            Picasso.with(context)
                .getBitmapSingle(
                    url = currentVideo.thumbnailUrl,
                    onError = {
                        requireContext()
                            .notificationManager
                            .notify(PlaybackNotification.ID, buildNotification(state, null))
                    },
                    onSuccess = { bitmap ->
                        requireContext()
                            .notificationManager
                            .notify(PlaybackNotification.ID, buildNotification(state, bitmap))
                    }
                )
                .disposeOnDestroy(this)
        }
    }

    private fun cancelPlaybackNotification() {
        withState(viewModel) {
            if (!it.showingPlaybackNotification) return@withState
            requireContext().notificationManager.cancel(PlaybackNotification.ID)
            viewModel.updatePlayerNotificationState(false)
        }
    }

    private fun refreshBackgroundPlaybackNotificationIfShowing() {
        withState(viewModel) { state ->
            if (state.showingPlaybackNotification) showPlaybackNotification()
        }
    }

    private fun buildNotification(state: YoutubePlayerState, largeIcon: Bitmap?): Notification =
        NotificationCompat.Builder(requireContext(), PlaybackNotification.CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.play)
            .apply {
                val bigText = requireNotNull(state.currentVideo).title
                if (largeIcon != null) {
                    setLargeIcon(largeIcon)
                        .setStyle(
                            NotificationCompat.BigPictureStyle()
                                .bigLargeIcon(
                                    BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
                                )
                                .bigPicture(largeIcon)
                                .setBigContentTitle(bigText)
                        )
                } else {
                    setContentText(bigText)
                }
            }
            .setContentTitle(getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                requireContext()
                    .activityPendingIntent((requireActivity() as IntentProvider).providedIntent)
            )
            .setDeleteIntent(
                requireContext()
                    .broadcastPendingIntent(Intent(PlaybackNotification.ACTION_DELETE_NOTIFICATION))
            )
            .apply {
                val (isPlaying, mode) = state
                if (mode.prevTrackAvailable) {
                    addAction(
                        R.drawable.previous_track,
                        getString(R.string.previous_track),
                        requireContext()
                            .broadcastPendingIntent(
                                YoutubePlayerNotificationAction.YOUTUBE_ACTION_PREV_VIDEO.intent
                            )
                    )
                }

                if (isPlaying) {
                    addAction(
                        R.drawable.pause,
                        getString(R.string.pause),
                        requireContext()
                            .broadcastPendingIntent(
                                YoutubePlayerNotificationAction.YOUTUBE_ACTION_PAUSE_PLAYBACK.intent
                            )
                    )
                } else {
                    addAction(
                        R.drawable.play,
                        getString(R.string.play),
                        requireContext()
                            .broadcastPendingIntent(
                                YoutubePlayerNotificationAction.YOUTUBE_ACTION_RESUME_PLAYBACK
                                    .intent
                            )
                    )
                }

                if (mode.nextTrackAvailable) {
                    addAction(
                        R.drawable.next_track,
                        getString(R.string.next_track),
                        requireContext()
                            .broadcastPendingIntent(
                                YoutubePlayerNotificationAction.YOUTUBE_ACTION_NEXT_VIDEO.intent
                            )
                    )
                }
            }
            .setAutoCancel(true)
            .build()

    companion object {
        private const val minYoutubePlayerGuidelinePercent = .45f
    }
}
