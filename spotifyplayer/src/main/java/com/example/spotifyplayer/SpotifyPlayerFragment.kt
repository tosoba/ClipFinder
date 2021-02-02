package com.example.spotifyplayer

import android.Manifest
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
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.ext.castAs
import com.example.core.android.base.handler.SlidingPanelController
import com.example.core.android.base.provider.IntentProvider
import com.example.core.android.spotify.controller.SpotifyTrackChangeHandler
import com.example.core.android.spotify.fragment.ISpotifyPlayerFragment
import com.example.core.android.spotify.model.Album
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.notification.PlaybackNotification
import com.example.core.android.util.ext.*
import com.example.core.android.view.onSeekBarProgressChangeListener
import com.example.core.android.view.visualizer.ColumnarVisualizerRenderer
import com.example.spotifyplayer.databinding.FragmentSpotifyPlayerBinding
import com.spotify.sdk.android.player.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_player.*
import me.bogerchan.niervisualizer.NierVisualizerManager
import me.bogerchan.niervisualizer.renderer.IRenderer
import timber.log.Timber
import kotlin.math.pow

class SpotifyPlayerFragment : BaseMvRxFragment(), ISpotifyPlayerFragment, Player.NotificationCallback {
    private val viewModel: SpotifyPlayerViewModel by fragmentViewModel()
    private val broadcastReceivers: ArrayList<BroadcastReceiver> = ArrayList(6)

    private val spotifyPlayerView: SpotifyPlayerView by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyPlayerView(
            onSpotifyPlayPauseBtnClickListener = onSpotifyPlayPauseBtnClickListener,
            onCloseSpotifyPlayerBtnClickListener = onCloseSpotifyPlayerBtnClickListener,
            onPreviousBtnClickListener = onPreviousBtnClickListener,
            onNextBtnClickListener = onNextBtnClickListener,
            onPlaybackSeekBarProgressChangeListener = onPlaybackSeekBarProgressChangeListener
        )
    }

    private var spotifyPlayer: SpotifyPlayer? = null
    override val isPlayerLoggedIn: Boolean get() = spotifyPlayer?.isLoggedIn == true
    override val isPlayerInitialized: Boolean get() = spotifyPlayer?.isInitialized == true
    override val playerView: View? get() = this.view

    private val onSpotifyPlayPauseBtnClickListener: View.OnClickListener = View.OnClickListener {
        withState(viewModel) {
            if (it.playbackState?.isPlaying == true) {
                spotifyPlayer?.pause(spotifyPlayerOperationCallback)
                visualizerManager?.pause()
            } else {
                spotifyPlayer?.resume(spotifyPlayerOperationCallback)
                visualizerManager?.resume()
            }
        }
    }

    private val onNextBtnClickListener = View.OnClickListener {
        spotifyPlayer?.skipToNext(spotifyPlayerOperationCallback)
    }

    private val onPreviousBtnClickListener = View.OnClickListener {
        spotifyPlayer?.skipToPrevious(spotifyPlayerOperationCallback)
    }

    private val onCloseSpotifyPlayerBtnClickListener = View.OnClickListener {
        activity?.castAs<SlidingPanelController>()?.hideIfVisible()
        stopPlayback()
    }

    private var spotifyPlaybackTimer: CountDownTimer? = null

    private val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener by lazy(LazyThreadSafetyMode.NONE) {
        onSeekBarProgressChangeListener { _: SeekBar?, progress: Int, fromUser: Boolean ->
            if (!fromUser) return@onSeekBarProgressChangeListener
            val positionInMs = progress * 1000
            spotifyPlaybackTimer?.cancel()
            spotifyPlayer?.seekToPosition(spotifyPlayerOperationCallback, positionInMs)
            spotifyPlaybackTimer = playbackTimer(
                trackDuration = withState(viewModel) { requireNotNull(it.playerMetadata).currentTrack.durationMs },
                positionMs = positionInMs.toLong()
            ).apply { start() }
        }
    }

    private val spotifyPlayerOperationCallback = object : Player.OperationCallback {
        override fun onSuccess() {
            Timber.tag("PLAY_OP").e("Success")
        }

        override fun onError(error: Error?) {
            Timber.tag("PLAY_OP").e("Error: ${error?.name ?: "unknown"}")
        }
    }

    private lateinit var albumCoverImageTarget: Target
    private val visualizerPaint: Paint by lazy(LazyThreadSafetyMode.NONE) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 8f
            color = Color.parseColor("#ffffff")
        }
    }
    private val visualizerRenderers: Array<IRenderer> by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf<IRenderer>(ColumnarVisualizerRenderer(visualizerPaint))
    }
    private var visualizerManager: NierVisualizerManager? = null

    private fun createNewVisualizerManager() {
        visualizerManager?.release()
        visualizerManager = NierVisualizerManager().apply {
            init(object : NierVisualizerManager.NVDataSource {
                private val audioBufferSize = 81920
                private val audioRecordByteBuffer by lazy { ByteArray(audioBufferSize / 2) }
                private val audioRecordShortBuffer by lazy { ShortArray(audioBufferSize / 2) }
                private val outputBuffer: ByteArray = ByteArray(512)

                override fun getDataSamplingInterval() = 0L
                override fun getDataLength() = outputBuffer.size
                override fun fetchFftData(): ByteArray {
                    audioTrackController.audioBuffer.peek(audioRecordShortBuffer)
                    audioRecordShortBuffer.forEachIndexed { index, sh ->
                        audioRecordByteBuffer[index] = (sh / 2.0.pow(10.0)).toInt().toByte()
                    }
                    var bufferIndex = 0
                    for (idx in audioRecordByteBuffer.indices step (audioRecordByteBuffer.size / (outputBuffer.size))) {
                        if (bufferIndex >= outputBuffer.size) break
                        outputBuffer[bufferIndex++] = audioRecordByteBuffer[idx]
                    }
                    return outputBuffer
                }

                override fun fetchWaveData(): ByteArray? = null
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSpotifyPlayerBinding.inflate(inflater, container, false)
        .apply {
            fragmentView = spotifyPlayerView
            val playbackSeekBarMaxValue = MutableLiveData(0)
            val nextTrackExists = MutableLiveData(false)
            val previousTrackExists = MutableLiveData(false)
            val currentTrackTitle = MutableLiveData("")
            viewModel.selectSubscribe(this@SpotifyPlayerFragment, SpotifyPlayerState::playerMetadata) { metadata ->
                nextTrackExists.value = metadata?.nextTrack != null
                previousTrackExists.value = metadata?.prevTrack != null
                playbackSeekBarMaxValue
                    .value = (metadata?.currentTrack?.durationMs ?: 0).toInt() * 1000
                metadata?.currentTrack?.let {
                    val trackName = it.name
                    val artistName = it.artistName
                    val currentTrackLabelText = if (trackName != null && artistName != null) {
                        "$artistName - $trackName"
                    } else {
                        ""
                    }
                    currentTrackTitle.value = currentTrackLabelText
                }
            }
            this.playbackSeekBarMaxValue = playbackSeekBarMaxValue
            this.nextTrackExists = nextTrackExists
            this.previousTrackExists = previousTrackExists
            this.currentTrackTitle = currentTrackTitle
            visualizerSurfaceView.setZOrderOnTop(true)
            visualizerSurfaceView.holder.setFormat(PixelFormat.TRANSLUCENT)
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpotifyPlayer()
    }

    override fun onStart() {
        super.onStart()
        withState(viewModel) {
            if (it.backgroundPlaybackNotificationIsShowing) {
                requireContext().notificationManager.cancel(PlaybackNotification.ID)
                viewModel.updatePlayerNotificationState(false)
            }
        }
        visualizerManager?.apply {
            if (spotifyPlayer?.playbackState?.isPlaying == true) resume()
        }
    }

    override fun onStop() {
        withState(viewModel) { state ->
            val shouldShowPlaybackNotification = spotifyPlayer?.isInitialized == true
                && state.playerMetadata?.currentTrack != null
                && state.lastPlayedItem !is NoLastPlayedItem
                && !state.backgroundPlaybackNotificationIsShowing
            if (shouldShowPlaybackNotification) {
                viewModel.updatePlayerNotificationState(true)
                showPlaybackNotification()
            }
        }
        visualizerManager?.pause()
        super.onStop()
    }

    override fun onDestroy() {
        with(requireContext()) {
            notificationManager.cancelAll()
            broadcastReceivers.forEach(this::unregisterReceiver)
        }

        spotifyPlayer?.removeNotificationCallback(this)
        spotifyPlayer?.removeConnectionStateCallback(activity?.castAs<ConnectionStateCallback>())
        Spotify.destroyPlayer(this)

        visualizerManager?.release()
        visualizerManager = null

        super.onDestroy()
    }

    override fun onDragging() {
        visualizer_surface_view?.hideIfShowing()
        visualizerManager?.pause()
    }

    override fun onExpanded() {
        visualizer_surface_view?.showIfHidden()
        visualizerManager?.resume()
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
        Timber.tag("PLAY_EVT").e(event.toString())
        viewModel.onPlaybackEvent(playerMetadata = spotifyPlayer?.metadata, playbackState = spotifyPlayer?.playbackState)

        when (event) {
            PlayerEvent.kSpPlaybackNotifyNext, PlayerEvent.kSpPlaybackNotifyPrev, PlayerEvent.kSpPlaybackNotifyPlay -> {
                updatePlayback()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.pause)
                refreshBackgroundPlaybackNotificationIfShowing()
//                visualizer_surface_view?.showIfHidden()
            }

            PlayerEvent.kSpPlaybackNotifyPause -> {
                spotifyPlaybackTimer?.cancel()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.play)
                refreshBackgroundPlaybackNotificationIfShowing()
//                visualizer_surface_view?.hideIfShowing()
            }

            PlayerEvent.kSpPlaybackNotifyTrackChanged -> withState(viewModel) { state ->
                state.playerMetadata?.currentTrack?.let {
                    it.id?.let { id -> activity?.castAs<SpotifyTrackChangeHandler>()?.onTrackChanged(id) }

                    albumCoverImageTarget = object : Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            current_track_image_view?.setImageDrawable(placeHolderDrawable)
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable?) {
                            current_track_image_view?.setImageDrawable(errorDrawable)
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                            Palette.from(bitmap).generate { palette ->
                                palette?.dominantColor?.let { color -> visualizerPaint.color = color }
                            }
                            current_track_image_view?.setImageBitmap(bitmap)
                        }
                    }

                    Picasso.with(context).load(it.albumCoverWebUrl).into(albumCoverImageTarget)
                }
            }

            else -> return
        }
    }

    override fun stopPlayback() {
        spotifyPlayer?.pause(spotifyPlayerOperationCallback)
    }

    private val audioTrackController: SpotifyAudioTrackController by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyAudioTrackController()
    }

    override fun onAuthenticationComplete(accessToken: String) {
        if (spotifyPlayer == null) {
            spotifyPlayer = SpotifyPlayer
                .Builder(Config(requireContext(), accessToken, BuildConfig.SPOTIFY_CLIENT_ID))
                .setAudioController(audioTrackController)
                .build(
                    object : SpotifyPlayer.InitializationObserver {
                        override fun onInitialized(player: SpotifyPlayer) {
                            player.setConnectivityStatus(spotifyPlayerOperationCallback, requireContext().networkConnectivity)
                            player.addNotificationCallback(this@SpotifyPlayerFragment)
                            player.addConnectionStateCallback(activity?.castAs<ConnectionStateCallback>())
                        }

                        override fun onError(error: Throwable) {
                            Timber.tag("PLAYER").e("Error: ${error.message ?: "unknown"}")
                        }
                    }
                )
        } else {
            spotifyPlayer?.login(accessToken)
        }
    }

    override fun logOutPlayer() {
        spotifyPlayer?.logout()
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

    override fun invalidate() = Unit

    private fun playbackTimer(
        trackDuration: Long, positionMs: Long
    ): CountDownTimer = tickingTimer(trackDuration - positionMs, 1000) { millisUntilFinished ->
        val seconds = (trackDuration - millisUntilFinished) / 1000
        playback_seek_bar?.progress = seconds.toInt()
    }

    private fun initSpotifyPlayer() {
        with(requireContext()) {
            broadcastReceivers.addAll(
                createAndRegisterReceiverFor(IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)) { _, _ ->
                    spotifyPlayer?.setConnectivityStatus(
                        spotifyPlayerOperationCallback, requireContext().networkConnectivity
                    )
                },
                createAndRegisterReceiverFor(IntentFilter(ACTION_DELETE_NOTIFICATION)) { _, _ ->
                    viewModel.updatePlayerNotificationState(false)
                    stopPlayback()
                },
                createAndRegisterReceiverFor(IntentFilter(ACTION_PAUSE_PLAYBACK)) { _, _ ->
                    spotifyPlayer?.pause(spotifyPlayerOperationCallback)
                },
                createAndRegisterReceiverFor(IntentFilter(ACTION_RESUME_PLAYBACK)) { _, _ ->
                    spotifyPlayer?.resume(spotifyPlayerOperationCallback)
                },
                createAndRegisterReceiverFor(IntentFilter(ACTION_PREV_TRACK)) { _, _ ->
                    spotifyPlayer?.skipToPrevious(spotifyPlayerOperationCallback)
                },
                createAndRegisterReceiverFor(IntentFilter(ACTION_NEXT_TRACK)) { _, _ ->
                    spotifyPlayer?.skipToNext(spotifyPlayerOperationCallback)
                }
            )
        }

        spotifyPlayer?.addNotificationCallback(this)
        spotifyPlayer?.addConnectionStateCallback(activity?.castAs<ConnectionStateCallback>())
    }

    private fun refreshBackgroundPlaybackNotificationIfShowing() {
        withState(viewModel) { state ->
            if (state.backgroundPlaybackNotificationIsShowing) refreshPlaybackNotification()
        }
    }

    private fun resetProgressAndPlay(uri: String) {
        activity?.castAs<SlidingPanelController>()?.expandIfHidden()
        playback_seek_bar?.progress = 0
        spotifyPlayer?.playUri(spotifyPlayerOperationCallback, uri, 0, 0)

        if (context?.isGranted(Manifest.permission.RECORD_AUDIO) == true) {
            createNewVisualizerManager()

            //TODO: implement a mechanism which will return the surface views surrounding circular image views in fragments like the playlist fragment etc
            //use a subject within MainActivity (or SpotifyMainFragment) and change visualizers accordingly
            visualizerManager?.start(visualizer_surface_view, visualizerRenderers)
        }
    }

    private fun buildNotification(largeIcon: Bitmap?): Notification = NotificationCompat
        .Builder(requireContext(), PlaybackNotification.CHANNEL_ID)
        .setSmallIcon(R.drawable.play)
        .apply {
            withState(viewModel) { state ->
                val bigText = state.playerMetadata?.currentTrack?.name ?: "Unknown track"
                if (largeIcon != null) {
                    val style = NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))
                        .bigPicture(largeIcon)
                        .setBigContentTitle(bigText)
                        .setSummaryText("${
                            state.playerMetadata?.currentTrack?.artistName ?: "Unknown artist"
                        } - ${
                            state.playerMetadata?.currentTrack?.albumName ?: "Unknown album"
                        }")
                    setLargeIcon(largeIcon).setStyle(style)
                } else {
                    setContentText(bigText)
                }
            }
        }
        .setContentTitle(getString(R.string.app_name))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(
            requireContext().getActivityPendingIntent((requireActivity() as IntentProvider).providedIntent)
        )
        .setDeleteIntent(requireContext().getBroadcastPendingIntent(Intent(ACTION_DELETE_NOTIFICATION)))
        .apply {
            withState(viewModel) { state ->
                if (state.playerMetadata?.prevTrack != null) {
                    val prevTrackIntent = requireContext().getBroadcastPendingIntent(Intent(ACTION_PREV_TRACK))
                    addAction(R.drawable.previous_track, getString(R.string.previous_track), prevTrackIntent)
                }

                if (state.playbackState?.isPlaying == true) {
                    val pauseIntent = requireContext().getBroadcastPendingIntent(Intent(ACTION_PAUSE_PLAYBACK))
                    addAction(R.drawable.pause, getString(R.string.pause), pauseIntent)
                } else {
                    val resumeIntent = requireContext().getBroadcastPendingIntent(Intent(ACTION_RESUME_PLAYBACK))
                    addAction(R.drawable.play, getString(R.string.play), resumeIntent)
                }

                if (state.playerMetadata?.nextTrack != null) {
                    val nextTrackIntent = requireContext().getBroadcastPendingIntent(Intent(ACTION_NEXT_TRACK))
                    addAction(R.drawable.next_track, getString(R.string.next_track), nextTrackIntent)
                }
            }
        }
        .setAutoCancel(true)
        .build()

    private fun showPlaybackNotification() {
        withState(viewModel) { state ->
            state.playerMetadata?.currentTrack?.let {
                Picasso.with(context)
                    .getBitmapSingle(
                        url = it.albumCoverWebUrl,
                        onError = {
                            requireContext().notificationManager
                                .notify(PlaybackNotification.ID, buildNotification(null))
                        }
                    ) { bitmap ->
                        requireContext().notificationManager
                            .notify(PlaybackNotification.ID, buildNotification(bitmap))
                    }
                    .disposeOnDestroy(this)
            }
        }
    }

    private fun refreshPlaybackNotification() {
        requireContext().notificationManager.cancel(PlaybackNotification.ID)
        showPlaybackNotification()
    }

    companion object {
        private const val ACTION_PAUSE_PLAYBACK = "ACTION_PAUSE_PLAYBACK"
        private const val ACTION_RESUME_PLAYBACK = "ACTION_RESUME_PLAYBACK"
        private const val ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION"
        private const val ACTION_PREV_TRACK = "ACTION_PREV_TRACK"
        private const val ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK"
    }
}
