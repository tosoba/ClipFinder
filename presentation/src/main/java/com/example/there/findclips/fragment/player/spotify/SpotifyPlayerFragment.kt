package com.example.there.findclips.fragment.player.spotify

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
import com.example.there.data.api.SpotifyClient
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentSpotifyPlayerBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.player.IPlayerFragment
import com.example.there.findclips.main.MainActivity
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.OnSeekBarProgressChangeListener
import com.spotify.sdk.android.player.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_spotify_player.*
import javax.inject.Inject

class SpotifyPlayerFragment :
        BaseVMFragment<SpotifyPlayerViewModel>(SpotifyPlayerViewModel::class.java),
        IPlayerFragment,
        Player.NotificationCallback,
        Player.OperationCallback,
        Injectable {

    private val view: SpotifyPlayerView by lazy(LazyThreadSafetyMode.NONE) {
        SpotifyPlayerView(
                state = viewModel.viewState,
                onSpotifyPlayPauseBtnClickListener = onSpotifyPlayPauseBtnClickListener,
                onCloseSpotifyPlayerBtnClickListener = onCloseSpotifyPlayerBtnClickListener,
                onPreviousBtnClickListener = onPreviousBtnClickListener,
                onNextBtnClickListener = onNextBtnClickListener,
                onPlaybackSeekBarProgressChangeListener = onPlaybackSeekBarProgressChangeListener
        )
    }

    val isPlayerLoggedIn: Boolean
        get() = spotifyPlayer?.isLoggedIn == true

    val isPlayerInitialized: Boolean
        get() = spotifyPlayer?.isInitialized == true

    fun logOutPlayer() {
        spotifyPlayer?.logout()
    }

    @Inject
    lateinit var applicationContext: Context

    var lastPlayedTrack: Track? = null
        private set
    var lastPlayedAlbum: Album? = null
        private set
    var lastPlayedPlaylist: Playlist? = null
        private set

    private var spotifyPlayer: SpotifyPlayer? = null

    var playerMetadata: Metadata? = null
        private set

    var currentPlaybackState: PlaybackState? = null
        private set

    private val onSpotifyPlayPauseBtnClickListener = View.OnClickListener {
        if (currentPlaybackState != null && currentPlaybackState!!.isPlaying) {
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSpotifyPlayerBinding>(
            inflater,
            R.layout.fragment_spotify_player,
            container,
            false
    ).apply {
        fragmentView = view
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpotifyPlayer()
    }

    override fun onStart() {
        super.onStart()
        if (backgroundPlaybackNotificationIsShowing) {
            applicationContext.notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
            backgroundPlaybackNotificationIsShowing = false
        }
    }

    override fun onStop() {
        if (shouldShowPlaybackNotification && !backgroundPlaybackNotificationIsShowing) {
            backgroundPlaybackNotificationIsShowing = true
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
        currentPlaybackState = spotifyPlayer?.playbackState
        playerMetadata = spotifyPlayer?.metadata

        fun updatePlayback() {
            playerMetadata?.let {
                viewModel.viewState.nextTrackExists.set(it.nextTrack != null)
                viewModel.viewState.previousTrackExists.set(it.prevTrack != null)

                val trackDuration = it.currentTrack.durationMs
                val positionMs = currentPlaybackState!!.positionMs

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

            PlayerEvent.kSpPlaybackNotifyTrackChanged -> playerMetadata?.currentTrack?.let {
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
        lastPlayedTrack = null
        lastPlayedPlaylist = null
        lastPlayedAlbum = null
    }

    fun onAuthenticationComplete(accessToken: String) {
        if (spotifyPlayer == null) {
            val playerConfig = Config(applicationContext, accessToken, SpotifyClient.id)

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

    fun loadTrack(track: Track) {
        lastPlayedAlbum = null
        lastPlayedPlaylist = null

        lastPlayedTrack = track
        resetProgressAndPlay(track.uri)
    }

    fun loadAlbum(album: Album) {

        lastPlayedTrack = null
        lastPlayedPlaylist = null

        lastPlayedAlbum = album
        resetProgressAndPlay(album.uri)
    }

    fun loadPlaylist(playlist: Playlist) {
        lastPlayedTrack = null
        lastPlayedAlbum = null

        lastPlayedPlaylist = playlist
        resetProgressAndPlay(playlist.uri)
    }

    private var spotifyPlaybackTimer: CountDownTimer? = null

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

    private val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener by lazy {
        object : OnSeekBarProgressChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val positionInMs = progress * 1000
                    spotifyPlaybackTimer?.cancel()
                    spotifyPlayer?.seekToPosition(loggerSpotifyPlayerOperationCallback, positionInMs)
                    spotifyPlaybackTimer = playbackTimer(
                            trackDuration = playerMetadata!!.currentTrack.durationMs,
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
        if (backgroundPlaybackNotificationIsShowing) refreshPlaybackNotification()
    }

    private fun resetProgressAndPlay(uri: String) {
        slidingPanelController?.expandIfHidden()
        playback_seek_bar?.progress = 0
        spotifyPlayer?.playUri(loggerSpotifyPlayerOperationCallback, uri, 0, 0)
    }

    private fun getNetworkConnectivity(context: Context): Connectivity {
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) Connectivity.fromNetworkType(activeNetwork.type)
        else Connectivity.OFFLINE
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

    private var backgroundPlaybackNotificationIsShowing = false

    private fun notificationBuilder(largeIcon: Bitmap?): NotificationCompat.Builder = NotificationCompat.Builder(context!!, FindClipsApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.play)
            .apply {
                val bigText = playerMetadata?.currentTrack?.name
                        ?: lastPlayedTrack?.name ?: "Unknown track"
                if (largeIcon != null) setLargeIcon(largeIcon).setStyle(NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                        .bigPicture(largeIcon)
                        .setBigContentTitle(bigText)
                        .setSummaryText("${playerMetadata?.currentTrack?.artistName
                                ?: "Unknown artist"} - ${playerMetadata?.currentTrack?.albumName
                                ?: "Unknown album"}"))
                else setContentText(bigText)
            }
            .setContentTitle("ClipFinder")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            0,
                            Intent(context, MainActivity::class.java),
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
                if (playerMetadata?.prevTrack != null) {
                    val prevTrackIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(ACTION_PREV_TRACK),
                            0
                    )
                    addAction(R.drawable.previous_track, getString(R.string.previous_track), prevTrackIntent)
                }

                if (currentPlaybackState?.isPlaying == true) {
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

                if (playerMetadata?.nextTrack != null) {
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

    private fun showPlaybackNotification() = playerMetadata?.currentTrack?.let {
        viewModel.getBitmapSingle(
                Picasso.with(context),
                playerMetadata!!.currentTrack.albumCoverWebUrl,
                { bitmap -> applicationContext.notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(bitmap).build()) },
                { applicationContext.notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(null).build()) }
        )
    }

    private fun refreshPlaybackNotification() {
        applicationContext.notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
        showPlaybackNotification()
    }

    private val shouldShowPlaybackNotification: Boolean
        get() = spotifyPlayer != null &&
                spotifyPlayer?.isInitialized == true &&
                playerMetadata?.currentTrack != null &&
                (lastPlayedAlbum != null || lastPlayedTrack != null || lastPlayedPlaylist != null)

    private val deleteNotificationIntentReceiver: DeleteNotificationIntentReceiver by lazy { DeleteNotificationIntentReceiver() }

    inner class DeleteNotificationIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            backgroundPlaybackNotificationIsShowing = false
            stopPlayback()
        }
    }

    private val pausePlaybackIntentReceiver: PausePlaybackIntentReceiver by lazy { PausePlaybackIntentReceiver() }

    inner class PausePlaybackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
                ?: Unit
    }

    private val resumePlaybackIntentReceiver: ResumePlaybackIntentReceiver by lazy { ResumePlaybackIntentReceiver() }

    inner class ResumePlaybackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.resume(loggerSpotifyPlayerOperationCallback)
                ?: Unit
    }

    private val nextTrackIntentReceiver = NextTrackIntentReceiver()

    inner class NextTrackIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = spotifyPlayer?.skipToNext(loggerSpotifyPlayerOperationCallback)
                ?: Unit
    }

    private val prevTrackIntentReceiver = PreviousTrackIntentReceiver()

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
