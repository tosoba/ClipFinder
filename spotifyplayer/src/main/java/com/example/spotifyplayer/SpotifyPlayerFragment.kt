package com.example.spotifyplayer

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.ISpotifyPlayerFragment
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.Constants
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.OnSeekBarProgressChangeListener
import com.example.spotifyapi.SpotifyAuth
import com.spotify.sdk.android.player.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_spotify_player.*
import javax.inject.Inject

class SpotifyPlayerFragment : BaseVMFragment<SpotifyPlayerViewModel>(SpotifyPlayerViewModel::class.java),
        ISpotifyPlayerFragment,
        Player.NotificationCallback,
        Player.OperationCallback,
        Injectable {

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

    @Inject
    lateinit var applicationContext: Context

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
                (viewModel.playerState.lastPlayedAlbum != null || viewModel.playerState.lastPlayedTrack != null || viewModel.playerState.lastPlayedPlaylist != null)

    private val deleteNotificationIntentReceiver: DeleteNotificationIntentReceiver by lazy { DeleteNotificationIntentReceiver() }
    private val pausePlaybackIntentReceiver: PausePlaybackIntentReceiver by lazy { PausePlaybackIntentReceiver() }
    private val resumePlaybackIntentReceiver: ResumePlaybackIntentReceiver by lazy { ResumePlaybackIntentReceiver() }
    private val nextTrackIntentReceiver: NextTrackIntentReceiver by lazy { NextTrackIntentReceiver() }
    private val prevTrackIntentReceiver: PreviousTrackIntentReceiver by lazy { PreviousTrackIntentReceiver() }

    private var spotifyPlaybackTimer: CountDownTimer? = null

    private val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener by lazy {
        object : OnSeekBarProgressChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
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
    }

    private val loggerSpotifyPlayerOperationCallback = object : Player.OperationCallback {
        override fun onSuccess() {
            Log.e("SUCCESS", "loggerSpotifyPlayerOperationCallback")
        }

        override fun onError(error: Error) {
            Log.e("ERR", "loggerSpotifyPlayerOperationCallback ${error.name}")
        }
    }

    private val networkStateReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (spotifyPlayer != null) {
                    val connectivity = getNetworkConnectivity(applicationContext)
                    spotifyPlayer?.setConnectivityStatus(this@SpotifyPlayerFragment, connectivity)
                }
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<com.example.spotifyplayer.databinding.FragmentSpotifyPlayerBinding>(
            inflater,
            R.layout.fragment_spotify_player,
            container,
            false
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

            unregisterReceiver(networkStateReceiver)
            unregisterReceiver(pausePlaybackIntentReceiver)
            unregisterReceiver(resumePlaybackIntentReceiver)
            unregisterReceiver(deleteNotificationIntentReceiver)
            unregisterReceiver(prevTrackIntentReceiver)
            unregisterReceiver(nextTrackIntentReceiver)
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
        viewModel.playerState.lastPlayedTrack = null
        viewModel.playerState.lastPlayedPlaylist = null
        viewModel.playerState.lastPlayedAlbum = null
    }

    override fun onAuthenticationComplete(accessToken: String) {
        if (spotifyPlayer == null) {
            val playerConfig = Config(applicationContext, accessToken, SpotifyAuth.id)

            spotifyPlayer = Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
                override fun onInitialized(player: SpotifyPlayer) {
                    spotifyPlayer?.setConnectivityStatus(loggerSpotifyPlayerOperationCallback, getNetworkConnectivity(applicationContext))
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

    private fun playbackTimer(trackDuration: Long, positionMs: Long): CountDownTimer = object : CountDownTimer(
            trackDuration - positionMs,
            1000
    ) {
        override fun onFinish() = Unit

        override fun onTick(millisUntilFinished: Long) {
            val seconds = (trackDuration - millisUntilFinished) / 1000
            playback_seek_bar?.progress = seconds.toInt()
        }
    }

    private fun initSpotifyPlayer() {
        with(applicationContext) {
            registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            registerReceiver(deleteNotificationIntentReceiver, IntentFilter(ACTION_DELETE_NOTIFICATION))
            registerReceiver(pausePlaybackIntentReceiver, IntentFilter(ACTION_PAUSE_PLAYBACK))
            registerReceiver(resumePlaybackIntentReceiver, IntentFilter(ACTION_RESUME_PLAYBACK))
            registerReceiver(prevTrackIntentReceiver, IntentFilter(ACTION_PREV_TRACK))
            registerReceiver(nextTrackIntentReceiver, IntentFilter(ACTION_NEXT_TRACK))
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

    @SuppressLint("MissingPermission")
    private fun getNetworkConnectivity(context: Context): Connectivity {
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) Connectivity.fromNetworkType(activeNetwork.type)
        else Connectivity.OFFLINE
    }

    private fun notificationBuilder(largeIcon: Bitmap?): NotificationCompat.Builder = NotificationCompat.Builder(context!!, Constants.NOTIFICATION_CHANNEL_ID)
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
            .setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            0,
                            intentProvider?.providedIntent,
                            0
                    )
            )
            .setDeleteIntent(
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(ACTION_DELETE_NOTIFICATION),
                            0
                    )
            )
            .apply {
                if (viewModel.playerState.playerMetadata?.prevTrack != null) {
                    val prevTrackIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(ACTION_PREV_TRACK),
                            0
                    )
                    addAction(R.drawable.previous_track, getString(R.string.previous_track), prevTrackIntent)
                }

                if (viewModel.playerState.currentPlaybackState?.isPlaying == true) {
                    val pauseIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(ACTION_PAUSE_PLAYBACK),
                            0
                    )
                    addAction(R.drawable.pause, getString(R.string.pause), pauseIntent)
                } else {
                    val resumeIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(ACTION_RESUME_PLAYBACK),
                            0
                    )
                    addAction(R.drawable.play, getString(R.string.play), resumeIntent)
                }

                if (viewModel.playerState.playerMetadata?.nextTrack != null) {
                    val nextTrackIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(ACTION_NEXT_TRACK),
                            0
                    )
                    addAction(R.drawable.next_track, getString(R.string.next_track), nextTrackIntent)
                }
            }
            .setAutoCancel(true)

    private fun showPlaybackNotification() = viewModel.playerState.playerMetadata?.currentTrack?.let {
        viewModel.getBitmapSingle(
                Picasso.with(context),
                viewModel.playerState.playerMetadata!!.currentTrack.albumCoverWebUrl,
                { bitmap -> applicationContext.notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(bitmap).build()) },
                { applicationContext.notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(null).build()) }
        )
    }

    private fun refreshPlaybackNotification() {
        applicationContext.notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
        showPlaybackNotification()
    }

    inner class DeleteNotificationIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.playerState.backgroundPlaybackNotificationIsShowing = false
            stopPlayback()
        }
    }

    inner class PausePlaybackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
                ?: Unit
    }

    inner class ResumePlaybackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.resume(loggerSpotifyPlayerOperationCallback)
                ?: Unit
    }

    inner class NextTrackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.skipToNext(loggerSpotifyPlayerOperationCallback)
                ?: Unit
    }

    inner class PreviousTrackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.skipToPrevious(loggerSpotifyPlayerOperationCallback)
                ?: Unit
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
