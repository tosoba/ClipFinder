package com.example.spotifyplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.ISpotifyPlayerFragment
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.Constants
import com.example.coreandroid.util.PendingIntents
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.onSeekBarProgressChangeListener
import com.example.spotifyapi.util.SpotifyAuth
import com.example.spotifyplayer.databinding.FragmentSpotifyPlayerBinding
import com.spotify.sdk.android.player.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_spotify_player.*
import org.koin.android.ext.android.inject

class SpotifyPlayerFragment : BaseVMFragment<SpotifyPlayerViewModel>(SpotifyPlayerViewModel::class),
        ISpotifyPlayerFragment,
        Player.NotificationCallback,
        Player.OperationCallback {

    private val receivers: ArrayList<BroadcastReceiver> = ArrayList(6)

    private val spotifyPlayerView: SpotifyPlayerView by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyPlayerView(
                state = viewModel.viewState,
                onSpotifyPlayPauseBtnClickListener = onSpotifyPlayPauseBtnClickListener,
                onCloseSpotifyPlayerBtnClickListener = onCloseSpotifyPlayerBtnClickListener,
                onPreviousBtnClickListener = onPreviousBtnClickListener,
                onNextBtnClickListener = onNextBtnClickListener,
                onPlaybackSeekBarProgressChangeListener = onPlaybackSeekBarProgressChangeListener
        )
    }

    override val playerView: View?
        get() = this.view

    override val isPlayerLoggedIn: Boolean
        get() = spotifyPlayer?.isLoggedIn == true

    override val isPlayerInitialized: Boolean
        get() = spotifyPlayer?.isInitialized == true

    private val applicationContext: Context by inject()

    private var spotifyPlayer: SpotifyPlayer? = null

    override val lastPlayedTrack: Track?
        get() = viewModel.playerState.lastPlayedTrack

    override val lastPlayedAlbum: Album?
        get() = viewModel.playerState.lastPlayedAlbum

    override val lastPlayedPlaylist: Playlist?
        get() = viewModel.playerState.lastPlayedPlaylist

    private val onSpotifyPlayPauseBtnClickListener = View.OnClickListener {
        if (viewModel.playerState.currentPlaybackState != null && viewModel.playerState.currentPlaybackState!!.isPlaying) {
            spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
        } else {
            spotifyPlayer?.resume(loggerSpotifyPlayerOperationCallback)
        }
    }

    private val onNextBtnClickListener = View.OnClickListener {
        spotifyPlayer?.skipToNext(loggerSpotifyPlayerOperationCallback)
    }

    private val onPreviousBtnClickListener = View.OnClickListener {
        spotifyPlayer?.skipToPrevious(loggerSpotifyPlayerOperationCallback)
    }

    private val onCloseSpotifyPlayerBtnClickListener = View.OnClickListener {
        slidingPanelController?.hideIfVisible()
        stopPlayback()
    }

    private val shouldShowPlaybackNotification: Boolean
        get() = spotifyPlayer != null &&
                spotifyPlayer?.isInitialized == true &&
                viewModel.playerState.playerMetadata?.currentTrack != null &&
                (viewModel.playerState.lastPlayedAlbum != null
                        || viewModel.playerState.lastPlayedTrack != null
                        || viewModel.playerState.lastPlayedPlaylist != null)

    private var spotifyPlaybackTimer: CountDownTimer? = null

    private val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener by lazy {
        onSeekBarProgressChangeListener { _: SeekBar?, progress: Int, fromUser: Boolean ->
            if (fromUser) {
                val positionInMs = progress * 1000
                spotifyPlaybackTimer?.cancel()
                spotifyPlayer?.seekToPosition(loggerSpotifyPlayerOperationCallback, positionInMs)
                spotifyPlaybackTimer = playbackTimer(
                        trackDuration = viewModel.playerState.playerMetadata!!.currentTrack.durationMs,
                        positionMs = positionInMs.toLong()
                ).apply { start() }
            }
        }
    }


    private val loggerSpotifyPlayerOperationCallback = object : Player.OperationCallback {
        override fun onSuccess() {
            Log.e("SUCCESS", "loggerSpotifyPlayerOperationCallback")
        }

        override fun onError(error: Error) {
            Log.e("ERR", "loggerSpotifyPlayerOperationCallback ${error.name}")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyPlayerBinding>(
            inflater, R.layout.fragment_spotify_player, container, false
    ).apply {
        fragmentView = spotifyPlayerView
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpotifyPlayer()
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.playerState.backgroundPlaybackNotificationIsShowing) {
            applicationContext.notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
            viewModel.playerState.backgroundPlaybackNotificationIsShowing = false
        }
    }

    override fun onStop() {
        if (shouldShowPlaybackNotification && !viewModel.playerState.backgroundPlaybackNotificationIsShowing) {
            viewModel.playerState.backgroundPlaybackNotificationIsShowing = true
            showPlaybackNotification()
        }
        super.onStop()
    }

    override fun onDestroy() {
        with(applicationContext) {
            notificationManager.cancelAll()
            receivers.forEach(this::unregisterReceiver)
        }

        spotifyPlayer?.removeNotificationCallback(this)
        spotifyPlayer?.removeConnectionStateCallback(connectionStateCallback)
        Spotify.destroyPlayer(this)

        super.onDestroy()
    }

    override fun onHidden() {
        stopPlayback()
    }

    override fun onSuccess() {
        Log.e("playerOperation", "Success")
    }

    override fun onError(error: Error?) {
        Log.e("playerOperation", error?.name ?: "error unknown")
    }

    override fun onPlaybackError(error: Error?) {
        Log.e("ERR", "onPlaybackError: ${error?.name ?: "error unknown"}")
    }

    override fun onPlaybackEvent(event: PlayerEvent) {
        Log.e("PlaybackEvent", event.toString())
        viewModel.playerState.currentPlaybackState = spotifyPlayer?.playbackState
        viewModel.playerState.playerMetadata = spotifyPlayer?.metadata

        fun updatePlayback() {
            viewModel.playerState.playerMetadata?.let {
                viewModel.viewState.nextTrackExists.set(it.nextTrack != null)
                viewModel.viewState.previousTrackExists.set(it.prevTrack != null)

                val trackDuration = it.currentTrack.durationMs
                val positionMs = viewModel.playerState.currentPlaybackState!!.positionMs

                viewModel.viewState.playbackSeekbarMaxValue.set((trackDuration / 1000).toInt())
                spotifyPlaybackTimer?.cancel()
                spotifyPlaybackTimer = playbackTimer(trackDuration, positionMs).apply { start() }
            }
        }

        when (event) {
            PlayerEvent.kSpPlaybackNotifyNext, PlayerEvent.kSpPlaybackNotifyPrev, PlayerEvent.kSpPlaybackNotifyPlay -> {
                updatePlayback()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.pause)
                refreshBackgroundPlaybackNotificationIfShowing()
            }

            PlayerEvent.kSpPlaybackNotifyPause -> {
                spotifyPlaybackTimer?.cancel()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.play)
                refreshBackgroundPlaybackNotificationIfShowing()
            }

            PlayerEvent.kSpPlaybackNotifyTrackChanged -> viewModel.playerState.playerMetadata?.currentTrack?.let {
                val trackName = it.name
                val artistName = it.artistName
                val currentTrackLabelText = if (trackName != null && artistName != null) "$artistName - $trackName" else ""
                viewModel.viewState.currentTrackTitle.set(currentTrackLabelText)
                it.id?.let { id ->
                    spotifyTrackChangeHandler?.onTrackChanged(id)
                }

                Picasso.with(context)
                        .load(it.albumCoverWebUrl)
                        .error(R.drawable.error_placeholder)
                        .placeholder(R.drawable.track_placeholder)
                        .into(current_track_image_view)
            }

            else -> return
        }
    }

    override fun stopPlayback() {
        spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
    }

    override fun onAuthenticationComplete(accessToken: String) {
        if (spotifyPlayer == null) {
            val playerConfig = Config(applicationContext, accessToken, SpotifyAuth.id)

            spotifyPlayer = Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
                override fun onInitialized(player: SpotifyPlayer) {
                    spotifyPlayer?.setConnectivityStatus(loggerSpotifyPlayerOperationCallback, applicationContext.networkConnectivity)
                    spotifyPlayer?.addNotificationCallback(this@SpotifyPlayerFragment)
                    spotifyPlayer?.addConnectionStateCallback(connectionStateCallback)
                }

                override fun onError(error: Throwable) {
                    Log.e("ERR", "SpotifyPlayer.InitializationObserver")
                }
            })
        } else {
            spotifyPlayer?.login(accessToken)
        }
    }

    override fun logOutPlayer() {
        spotifyPlayer?.logout()
    }

    override fun loadTrack(track: Track) {
        viewModel.playerState.lastPlayedAlbum = null
        viewModel.playerState.lastPlayedPlaylist = null
        viewModel.playerState.lastPlayedTrack = track
        resetProgressAndPlay(track.uri)
    }

    override fun loadAlbum(album: Album) {
        viewModel.playerState.lastPlayedTrack = null
        viewModel.playerState.lastPlayedPlaylist = null
        viewModel.playerState.lastPlayedAlbum = album
        resetProgressAndPlay(album.uri)
    }

    override fun loadPlaylist(playlist: Playlist) {
        viewModel.playerState.lastPlayedTrack = null
        viewModel.playerState.lastPlayedAlbum = null
        viewModel.playerState.lastPlayedPlaylist = playlist
        resetProgressAndPlay(playlist.uri)
    }

    private fun playbackTimer(
            trackDuration: Long, positionMs: Long
    ): CountDownTimer = tickingTimer(trackDuration - positionMs, 1000) { millisUntilFinished ->
        val seconds = (trackDuration - millisUntilFinished) / 1000
        playback_seek_bar?.progress = seconds.toInt()
    }

    private fun initSpotifyPlayer() {
        with(applicationContext) {
            receivers.addAll(
                    registerReceiverFor(IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)) { _, _ ->
                        spotifyPlayer?.setConnectivityStatus(this@SpotifyPlayerFragment, applicationContext.networkConnectivity)
                    },
                    registerReceiverFor(IntentFilter(ACTION_DELETE_NOTIFICATION)) { _, _ ->
                        viewModel.playerState.backgroundPlaybackNotificationIsShowing = false
                        stopPlayback()
                    },
                    registerReceiverFor(IntentFilter(ACTION_PAUSE_PLAYBACK)) { _, _ ->
                        spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
                    },
                    registerReceiverFor(IntentFilter(ACTION_RESUME_PLAYBACK)) { _, _ ->
                        spotifyPlayer?.resume(loggerSpotifyPlayerOperationCallback)
                    },
                    registerReceiverFor(IntentFilter(ACTION_PREV_TRACK)) { _, _ ->
                        spotifyPlayer?.skipToPrevious(loggerSpotifyPlayerOperationCallback)
                    },
                    registerReceiverFor(IntentFilter(ACTION_NEXT_TRACK)) { _, _ ->
                        spotifyPlayer?.skipToNext(loggerSpotifyPlayerOperationCallback)
                    }
            )
        }

        spotifyPlayer?.addNotificationCallback(this)
        spotifyPlayer?.addConnectionStateCallback(connectionStateCallback)
    }

    private fun refreshBackgroundPlaybackNotificationIfShowing() {
        if (viewModel.playerState.backgroundPlaybackNotificationIsShowing) refreshPlaybackNotification()
    }

    private fun resetProgressAndPlay(uri: String) {
        slidingPanelController?.expandIfHidden()
        playback_seek_bar?.progress = 0
        spotifyPlayer?.playUri(loggerSpotifyPlayerOperationCallback, uri, 0, 0)
    }

    private fun notificationBuilder(
            largeIcon: Bitmap?
    ): NotificationCompat.Builder = NotificationCompat.Builder(context!!, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.play)
            .apply {
                val bigText = viewModel.playerState.playerMetadata?.currentTrack?.name
                        ?: viewModel.playerState.lastPlayedTrack?.name ?: "Unknown track"
                if (largeIcon != null) setLargeIcon(largeIcon).setStyle(NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))
                        .bigPicture(largeIcon)
                        .setBigContentTitle(bigText)
                        .setSummaryText("${viewModel.playerState.playerMetadata?.currentTrack?.artistName
                                ?: "Unknown artist"} - ${viewModel.playerState.playerMetadata?.currentTrack?.albumName
                                ?: "Unknown album"}"))
                else setContentText(bigText)
            }
            .setContentTitle("ClipFinder")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(PendingIntents.getActivity(context, intentProvider?.providedIntent))
            .setDeleteIntent(PendingIntents.getBroadcast(context, Intent(ACTION_DELETE_NOTIFICATION)))
            .apply {
                if (viewModel.playerState.playerMetadata?.prevTrack != null) {
                    val prevTrackIntent = PendingIntents.getBroadcast(context, Intent(ACTION_PREV_TRACK))
                    addAction(R.drawable.previous_track, getString(R.string.previous_track), prevTrackIntent)
                }

                if (viewModel.playerState.currentPlaybackState?.isPlaying == true) {
                    val pauseIntent = PendingIntents.getBroadcast(context, Intent(ACTION_PAUSE_PLAYBACK))
                    addAction(R.drawable.pause, getString(R.string.pause), pauseIntent)
                } else {
                    val resumeIntent = PendingIntents.getBroadcast(context, Intent(ACTION_RESUME_PLAYBACK))
                    addAction(R.drawable.play, getString(R.string.play), resumeIntent)
                }

                if (viewModel.playerState.playerMetadata?.nextTrack != null) {
                    val nextTrackIntent = PendingIntents.getBroadcast(context, Intent(ACTION_NEXT_TRACK))
                    addAction(R.drawable.next_track, getString(R.string.next_track), nextTrackIntent)
                }
            }
            .setAutoCancel(true)

    private fun showPlaybackNotification() = viewModel.playerState.playerMetadata?.currentTrack?.let {
        viewModel.getBitmapSingle(
                Picasso.with(context),
                viewModel.playerState.playerMetadata!!.currentTrack.albumCoverWebUrl,
                { applicationContext.notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(null).build()) },
                { bitmap -> applicationContext.notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(bitmap).build()) }
        )
    }

    private fun refreshPlaybackNotification() {
        applicationContext.notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
        showPlaybackNotification()
    }

    companion object {
        private const val PLAYBACK_NOTIFICATION_ID = 100

        private const val ACTION_PAUSE_PLAYBACK = "ACTION_PAUSE_PLAYBACK"
        private const val ACTION_RESUME_PLAYBACK = "ACTION_RESUME_PLAYBACK"
        private const val ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION"
        private const val ACTION_PREV_TRACK = "ACTION_PREV_TRACK"
        private const val ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK"
    }
}
