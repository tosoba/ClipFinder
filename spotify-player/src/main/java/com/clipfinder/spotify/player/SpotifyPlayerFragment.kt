package com.clipfinder.spotify.player

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.base.handler.SlidingPanelController
import com.clipfinder.core.android.base.provider.IntentProvider
import com.clipfinder.core.android.spotify.base.SpotifyPlayerConnectionStateCallback
import com.clipfinder.core.android.spotify.base.SpotifyPlayerOperationLogCallback
import com.clipfinder.core.android.spotify.base.SpotifyTrackChangeHandler
import com.clipfinder.core.android.spotify.ext.id
import com.clipfinder.core.android.spotify.ext.networkConnectivity
import com.clipfinder.core.android.spotify.ext.summaryText
import com.clipfinder.core.android.spotify.fragment.ISpotifyPlayerFragment
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.spotify.player.SpotifyAudioTrackController
import com.clipfinder.core.android.spotify.visualizer.datasource.SpotifyPlayerNVDataSource
import com.clipfinder.core.android.spotify.visualizer.renderer.ColumnarVisualizerRenderer
import com.clipfinder.core.android.util.ext.*
import com.clipfinder.core.android.view.onSeekBarProgressChangeListener
import com.clipfinder.core.ext.castAs
import com.clipfinder.core.notification.PlaybackNotification
import com.clipfinder.spotify.player.databinding.FragmentSpotifyPlayerBinding
import com.spotify.sdk.android.player.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_player.*
import me.bogerchan.niervisualizer.NierVisualizerManager
import me.bogerchan.niervisualizer.renderer.IRenderer
import timber.log.Timber

class SpotifyPlayerFragment :
    BaseMvRxFragment(),
    ISpotifyPlayerFragment,
    Player.NotificationCallback,
    ConnectionStateCallback {
    private val viewModel: SpotifyPlayerViewModel by fragmentViewModel()
    private val broadcastReceivers: ArrayList<BroadcastReceiver> = ArrayList(6)

    private val spotifyPlayerView: SpotifyPlayerView by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyPlayerView(
            onSpotifyPlayPauseBtnClickListener = onPlayPauseBtnClickListener,
            onCloseSpotifyPlayerBtnClickListener = onClosePlayerBtnClickListener,
            onPreviousBtnClickListener = onPreviousBtnClickListener,
            onNextBtnClickListener = onNextBtnClickListener,
            onPlaybackSeekBarProgressChangeListener = onPlaybackSeekBarProgressChangeListener
        )
    }

    private var player: SpotifyPlayer? = null
    override val isPlayerLoggedIn: Boolean
        get() = player?.isLoggedIn == true
    override val playerView: View?
        get() = this.view

    private val onPlayPauseBtnClickListener: View.OnClickListener =
        View.OnClickListener {
            withState(viewModel) {
                if (it.playbackState?.isPlaying == true) {
                    player?.pause(SpotifyPlayerOperationLogCallback)
                    visualizerManager.pause()
                } else {
                    player?.resume(SpotifyPlayerOperationLogCallback)
                    visualizerManager.resume()
                }
            }
        }

    private val onNextBtnClickListener =
        View.OnClickListener { player?.skipToNext(SpotifyPlayerOperationLogCallback) }

    private val onPreviousBtnClickListener =
        View.OnClickListener { player?.skipToPrevious(SpotifyPlayerOperationLogCallback) }

    private val onClosePlayerBtnClickListener =
        View.OnClickListener {
            activity?.castAs<SlidingPanelController>()?.hideIfVisible()
            stopPlayback()
        }

    private var spotifyPlaybackTimer: CountDownTimer? = null

    private val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        onSeekBarProgressChangeListener { _: SeekBar?, progress: Int, fromUser: Boolean ->
            if (!fromUser) return@onSeekBarProgressChangeListener
            val positionInMs = progress * 1000
            spotifyPlaybackTimer?.cancel()
            player?.seekToPosition(SpotifyPlayerOperationLogCallback, positionInMs)
            spotifyPlaybackTimer =
                playbackTimer(
                    trackDuration =
                        withState(viewModel) {
                            requireNotNull(it.playerMetadata).currentTrack.durationMs
                        },
                    positionMs = positionInMs.toLong()
                )
                    .apply { start() }
        }
    }

    private val audioTrackController by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyAudioTrackController()
    }

    private lateinit var albumCoverImageTarget: Target
    private val visualizerPaint: Paint by lazy(LazyThreadSafetyMode.NONE) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 8f
            color = Color.parseColor("#ffffff")
        }
    }
    private val visualizerRenderers: Array<IRenderer> by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(ColumnarVisualizerRenderer(visualizerPaint))
    }
    private val visualizerManager: NierVisualizerManager by lazy(LazyThreadSafetyMode.NONE) {
        NierVisualizerManager().apply { init(SpotifyPlayerNVDataSource(audioTrackController)) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentSpotifyPlayerBinding.inflate(inflater, container, false)
            .apply {
                fragmentView = spotifyPlayerView
                val nextTrackExists = MutableLiveData(false)
                val previousTrackExists = MutableLiveData(false)
                val currentTrackTitle = MutableLiveData("")
                viewModel.selectSubscribe(
                    this@SpotifyPlayerFragment,
                    SpotifyPlayerState::playerMetadata
                ) { metadata ->
                    nextTrackExists.value = metadata?.nextTrack != null
                    previousTrackExists.value = metadata?.prevTrack != null
                    metadata?.currentTrack?.let {
                        val trackName = it.name
                        val artistName = it.artistName
                        val currentTrackLabelText =
                            if (trackName != null && artistName != null) {
                                "$artistName - $trackName"
                            } else {
                                ""
                            }
                        currentTrackTitle.value = currentTrackLabelText
                    }
                }
                this.nextTrackExists = nextTrackExists
                this.previousTrackExists = previousTrackExists
                this.currentTrackTitle = currentTrackTitle
                visualizerSurfaceView.setZOrderOnTop(true)
                visualizerSurfaceView.holder.setFormat(PixelFormat.TRANSLUCENT)
            }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initIntentReceivers()
    }

    override fun onStart() {
        super.onStart()
        cancelPlaybackNotification()
        if (player?.playbackState?.isPlaying == true) visualizerManager.resume()
    }

    override fun onStop() {
        withState(viewModel) { state ->
            val currentTrack = state.playerMetadata?.currentTrack
            val shouldShowPlaybackNotification =
                player?.isInitialized == true &&
                    currentTrack != null &&
                    state.lastPlayedItem !is NoLastPlayedItem &&
                    !state.showingPlaybackNotification
            if (!shouldShowPlaybackNotification) return@withState
            viewModel.updatePlayerNotificationState(true)
            showPlaybackNotification(currentTrack!!)
        }
        visualizerManager.pause()
        super.onStop()
    }

    override fun onDestroy() {
        with(requireContext()) {
            notificationManager.cancelAll()
            broadcastReceivers.forEach(this::unregisterReceiver)
        }

        player?.removeNotificationCallback(this)
        player?.removeConnectionStateCallback(this)
        Spotify.destroyPlayer(this)

        visualizerManager.release()

        super.onDestroy()
    }

    override fun onDragging() {
        visualizer_surface_view?.hideIfShowing()
        visualizerManager.pause()
    }

    override fun onExpanded() {
        visualizer_surface_view?.showIfHidden()
        visualizerManager.resume()
    }

    override fun onHidden() {
        stopPlayback()
    }

    override fun onPlaybackError(error: Error?) {
        Timber.tag("PLAY_ERR").e("onPlaybackError: ${error?.name ?: "error unknown"}")
    }

    private fun updatePlayback() {
        withState(viewModel) { state ->
            state.playerMetadata?.let {
                val trackDuration = it.currentTrack.durationMs
                val positionMs = requireNotNull(state.playbackState).positionMs
                spotifyPlaybackTimer?.cancel()
                spotifyPlaybackTimer = playbackTimer(trackDuration, positionMs).apply { start() }
            }
        }
    }

    override fun onPlaybackEvent(event: PlayerEvent) {
        viewModel.onPlaybackEvent(
            playerMetadata = player?.metadata,
            playbackState = player?.playbackState
        )

        when (event) {
            PlayerEvent.kSpPlaybackNotifyNext,
            PlayerEvent.kSpPlaybackNotifyPrev,
            PlayerEvent.kSpPlaybackNotifyPlay -> {
                updatePlayback()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.pause)
                refreshBackgroundPlaybackNotificationIfShowing()
            }
            PlayerEvent.kSpPlaybackNotifyPause -> {
                spotifyPlaybackTimer?.cancel()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.play)
                refreshBackgroundPlaybackNotificationIfShowing()
            }
            PlayerEvent.kSpPlaybackNotifyTrackChanged ->
                withState(viewModel) { state ->
                    state.playerMetadata?.currentTrack?.let { track ->
                        track.id?.let { id ->
                            activity?.castAs<SpotifyTrackChangeHandler>()?.onTrackChanged(id)
                        }

                        albumCoverImageTarget =
                            object : Target {
                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                    current_track_image_view?.setImageDrawable(placeHolderDrawable)
                                }

                                override fun onBitmapFailed(errorDrawable: Drawable?) {
                                    current_track_image_view?.setImageDrawable(errorDrawable)
                                }

                                override fun onBitmapLoaded(
                                    bitmap: Bitmap,
                                    from: Picasso.LoadedFrom?
                                ) {
                                    Palette.from(bitmap).generate { palette ->
                                        palette?.dominantColor?.let { color ->
                                            visualizerPaint.color = color
                                        }
                                    }
                                    current_track_image_view?.setImageBitmap(bitmap)
                                }
                            }

                        Picasso.with(context)
                            .load(track.albumCoverWebUrl)
                            .into(albumCoverImageTarget)
                        refreshBackgroundPlaybackNotificationIfShowing()
                    }
                }
            else -> return
        }
    }

    override fun stopPlayback() {
        player?.pause(SpotifyPlayerOperationLogCallback)
    }

    override fun initializePlayer(accessToken: String, callback: () -> Unit) {
        if (player == null) {
            player =
                SpotifyPlayer.Builder(
                        Config(requireContext(), accessToken, BuildConfig.SPOTIFY_CLIENT_ID)
                    )
                    .setAudioController(audioTrackController)
                    .build(
                        object : SpotifyPlayer.InitializationObserver {
                            override fun onInitialized(player: SpotifyPlayer) {
                                player.setConnectivityStatus(
                                    SpotifyPlayerOperationLogCallback,
                                    requireContext().networkConnectivity
                                )
                                player.addNotificationCallback(this@SpotifyPlayerFragment)
                                player.addConnectionStateCallback(this@SpotifyPlayerFragment)
                                callback()
                            }

                            override fun onError(error: Throwable) {
                                Timber.tag("PLAYER").e("Error: ${error.message ?: "unknown"}")
                            }
                        }
                    )
        } else {
            var connectionStateCallback: ConnectionStateCallback? = null
            connectionStateCallback =
                object : SpotifyPlayerConnectionStateCallback {
                    override fun onLoggedIn() {
                        callback()
                        player?.removeConnectionStateCallback(connectionStateCallback)
                    }

                    override fun onLoginFailed(error: Error?) {
                        player?.removeConnectionStateCallback(connectionStateCallback)
                    }
                }
            player?.addConnectionStateCallback(connectionStateCallback)
            player?.login(accessToken)
        }
    }

    override fun logOutPlayer() {
        player?.logout()
    }

    override fun loadTrack(track: Track) {
        viewModel.onLoadTrack(track)
        resetProgressAndPlay(track.uri)
    }

    override fun loadAlbum(album: Album) {
        viewModel.onLoadAlbum(album)
        resetProgressAndPlay(album.uri)
    }

    override fun loadPlaylist(playlist: Playlist) {
        viewModel.onLoadPlaylist(playlist)
        resetProgressAndPlay(playlist.uri)
    }

    override fun onLoggedOut() {
        Toast.makeText(requireContext(), "You logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggedIn() {
        Toast.makeText(requireContext(), "You successfully logged in.", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionMessage(message: String?) {
        Timber.tag("onConnectionMessage: ").e(message ?: "Unknown connection message.")
    }

    override fun onLoginFailed(error: Error?) {
        Timber.e("onLoginFailed")
        Toast.makeText(
                requireContext(),
                "Login failed: ${error?.name ?: "error unknown"}",
                Toast.LENGTH_SHORT
            )
            .show()
    }

    override fun onTemporaryError() {
        Timber.tag("ERR").e("onTemporaryError")
    }

    override fun invalidate() = Unit

    private fun playbackTimer(trackDuration: Long, positionMs: Long): CountDownTimer =
        tickingTimer(trackDuration - positionMs, 1000) { millisUntilFinished ->
            val seconds = (trackDuration - millisUntilFinished) / 1000
            if (playback_seek_bar?.max == 0) playback_seek_bar?.max = (trackDuration / 1000).toInt()
            playback_seek_bar?.progress = seconds.toInt()
        }

    private fun initIntentReceivers() {
        with(requireContext()) {
            broadcastReceivers.addAll(
                createAndRegisterReceiverFor(
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                ) { _, _ ->
                    player?.setConnectivityStatus(
                        SpotifyPlayerOperationLogCallback,
                        requireContext().networkConnectivity
                    )
                },
                createAndRegisterReceiverFor(
                    IntentFilter(PlaybackNotification.ACTION_DELETE_NOTIFICATION)
                ) { _, _ ->
                    viewModel.updatePlayerNotificationState(false)
                    stopPlayback()
                },
                createAndRegisterReceiverFor(
                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_PAUSE_PLAYBACK.filter
                ) { _, _ -> player?.pause(SpotifyPlayerOperationLogCallback) },
                createAndRegisterReceiverFor(
                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_RESUME_PLAYBACK.filter
                ) { _, _ -> player?.resume(SpotifyPlayerOperationLogCallback) },
                createAndRegisterReceiverFor(
                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_PREV_TRACK.filter
                ) { _, _ -> player?.skipToPrevious(SpotifyPlayerOperationLogCallback) },
                createAndRegisterReceiverFor(
                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_NEXT_TRACK.filter
                ) { _, _ -> player?.skipToNext(SpotifyPlayerOperationLogCallback) }
            )
        }
    }

    private fun resetProgressAndPlay(uri: String) {
        activity?.castAs<SlidingPanelController>()?.expandIfHidden()
        playback_seek_bar?.progress = 0
        player?.playUri(SpotifyPlayerOperationLogCallback, uri, 0, 0)

        // TODO: implement a mechanism which will return the surface views surrounding circular
        // image views in fragments like the playlist fragment etc
        // use a subject within MainActivity (or SpotifyMainFragment) and change visualizers
        // accordingly
        visualizerManager.start(visualizer_surface_view, visualizerRenderers)
    }

    private fun showPlaybackNotification(track: Metadata.Track) {
        Picasso.with(context)
            .getBitmapSingle(
                url = track.albumCoverWebUrl,
                onError = {
                    requireContext()
                        .notificationManager
                        .notify(PlaybackNotification.ID, buildNotification(track, null))
                }
            ) { bitmap ->
                requireContext()
                    .notificationManager
                    .notify(PlaybackNotification.ID, buildNotification(track, bitmap))
            }
            .disposeOnDestroy(this)
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
            if (state.showingPlaybackNotification && state.playerMetadata?.currentTrack != null) {
                showPlaybackNotification(state.playerMetadata.currentTrack)
            }
        }
    }

    private fun buildNotification(track: Metadata.Track, largeIcon: Bitmap?): Notification =
        NotificationCompat.Builder(requireContext(), PlaybackNotification.CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.play)
            .apply {
                val bigText = track.name ?: "Unknown track"
                if (largeIcon != null) {
                    val style =
                        NotificationCompat.BigPictureStyle()
                            .bigLargeIcon(
                                BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
                            )
                            .bigPicture(largeIcon)
                            .setBigContentTitle(bigText)
                            .setSummaryText(track.summaryText)
                    setLargeIcon(largeIcon).setStyle(style)
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
                withState(viewModel) { state ->
                    if (state.playerMetadata?.prevTrack != null) {
                        addAction(
                            R.drawable.previous_track,
                            getString(R.string.previous_track),
                            requireContext()
                                .broadcastPendingIntent(
                                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_PREV_TRACK.intent
                                )
                        )
                    }

                    if (state.playbackState?.isPlaying == true) {
                        addAction(
                            R.drawable.pause,
                            getString(R.string.pause),
                            requireContext()
                                .broadcastPendingIntent(
                                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_PAUSE_PLAYBACK
                                        .intent
                                )
                        )
                    } else {
                        addAction(
                            R.drawable.play,
                            getString(R.string.play),
                            requireContext()
                                .broadcastPendingIntent(
                                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_RESUME_PLAYBACK
                                        .intent
                                )
                        )
                    }

                    if (state.playerMetadata?.nextTrack != null) {
                        addAction(
                            R.drawable.next_track,
                            getString(R.string.next_track),
                            requireContext()
                                .broadcastPendingIntent(
                                    SpotifyPlayerNotificationAction.SPOTIFY_ACTION_NEXT_TRACK.intent
                                )
                        )
                    }
                }
            }
            .setAutoCancel(true)
            .build()
}
