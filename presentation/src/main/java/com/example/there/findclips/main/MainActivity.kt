package com.example.there.findclips.main

import android.app.PendingIntent
import android.app.SearchManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.SearchRecentSuggestions
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.findclips.BR
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.R
import com.example.there.findclips.SpotifyClient
import com.example.there.findclips.base.activity.BaseVMActivity
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.databinding.DrawerHeaderBinding
import com.example.there.findclips.fragment.addvideo.AddVideoDialogFragment
import com.example.there.findclips.fragment.addvideo.AddVideoViewState
import com.example.there.findclips.fragment.list.SpotifyTracksFragment
import com.example.there.findclips.fragment.search.SearchFragment
import com.example.there.findclips.fragment.search.SearchSuggestionProvider
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.settings.SettingsActivity
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnSeekBarProgressChangeListener
import com.example.there.findclips.view.OnYoutubePlayerStateChangeListener
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.list.item.VideoItemView
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.*
import com.squareup.picasso.Picasso
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity :
        BaseVMActivity<MainViewModel>(MainViewModel::class.java),
        HasSupportFragmentInjector,
        Player.NotificationCallback,
        ConnectionStateCallback,
        Player.OperationCallback {

    val connectivitySnackbarParentView: View?
        get() = findViewById(R.id.main_view_pager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.deleteAllVideoSearchData()

        initViewBindings()

        setupNavigationFromSimilarTracks()

        initYouTubePlayerView()
        initPlayerViewControls()

        initSpotifyPlayer()

        addStatePropertyChangedCallbacks()
    }

    private fun setupNavigationFromSimilarTracks() {
        similarTracksFragment?.onItemClick = {
            sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            pagerAdapter.currentHostFragment?.showFragment(TrackVideosFragment.newInstance(it), true)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun addStatePropertyChangedCallbacks() = with(lifecycle) {
        addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isLoggedIn, loggedInCallback))
        addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.playerState) { observable, _ ->
            val newState = (observable as ObservableField<PlayerState>).get()!!
            when (newState) {
                PlayerState.TRACK -> lastPlayedTrack?.let { viewModel.updateTrackFavouriteState(it) }
                PlayerState.PLAYLIST -> lastPlayedPlaylist?.let { viewModel.updatePlaylistFavouriteState(it) }
                PlayerState.ALBUM -> lastPlayedAlbum?.let { viewModel.updateAlbumFavouriteState(it) }
                else -> viewModel.viewState.itemFavouriteState.set(false)
            }
        })
    }

    private var backgroundPlaybackNotificationIsShowing = false

    override fun onStart() {
        super.onStart()
        if (backgroundPlaybackNotificationIsShowing) {
            notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
            backgroundPlaybackNotificationIsShowing = false
        }
    }

    private fun notificationBuilder(largeIcon: Bitmap?): NotificationCompat.Builder = NotificationCompat.Builder(this, FindClipsApp.CHANNEL_ID)
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
                            this,
                            0,
                            Intent(this, MainActivity::class.java),
                            0
                    )
            )
            .setDeleteIntent(
                    PendingIntent.getBroadcast(
                            this,
                            0,
                            Intent(ACTION_DELETE_NOTIFICATION),
                            0
                    )
            )
            .apply {
                if (playerMetadata?.prevTrack != null) {
                    val prevTrackIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
                            0,
                            Intent(ACTION_PREV_TRACK),
                            0
                    )
                    addAction(R.drawable.previous_track, getString(R.string.previous_track), prevTrackIntent)
                }

                if (currentPlaybackState?.isPlaying == true) {
                    val pauseIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
                            0,
                            Intent(ACTION_PAUSE_PLAYBACK),
                            0
                    )
                    addAction(R.drawable.pause, getString(R.string.pause), pauseIntent)
                } else {
                    val resumeIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
                            0,
                            Intent(ACTION_RESUME_PLAYBACK),
                            0
                    )
                    addAction(R.drawable.play, getString(R.string.play), resumeIntent)
                }

                if (playerMetadata?.nextTrack != null) {
                    val nextTrackIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
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
                Picasso.with(this),
                playerMetadata!!.currentTrack.albumCoverWebUrl,
                { bitmap -> notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(bitmap).build()) },
                { notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notificationBuilder(null).build()) }
        )
    }

    private val deleteNotificationIntentReceiver: DeleteNotificationIntentReceiver by lazy { DeleteNotificationIntentReceiver() }

    inner class DeleteNotificationIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            backgroundPlaybackNotificationIsShowing = false
            stopSpotifyPlayback()
        }
    }

    private fun refreshPlaybackNotification() {
        notificationManager.cancel(PLAYBACK_NOTIFICATION_ID)
        showPlaybackNotification()
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

    private val shouldShowPlaybackNotification: Boolean
        get() = spotifyPlayer != null &&
                spotifyPlayer?.isInitialized == true &&
                playerMetadata?.currentTrack != null &&
                (lastPlayedAlbum != null || lastPlayedTrack != null || lastPlayedPlaylist != null)

    override fun onStop() {
        if (shouldShowPlaybackNotification && !backgroundPlaybackNotificationIsShowing) {
            backgroundPlaybackNotificationIsShowing = true
            showPlaybackNotification()
        }
        super.onStop()
    }

    private fun initViewBindings() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view
        binding.relatedVideosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.itemFavouriteState) { _, _ ->
            binding.addToFavouritesFab.hideAndShow()
        })

        val drawerHeaderBinding: DrawerHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.drawer_header, binding.drawerNavigationView, false)
        drawerHeaderBinding.viewState = viewModel.drawerViewState
        binding.drawerNavigationView.addHeaderView(drawerHeaderBinding.root)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updatePlayersDimensions(currentSlideOffset)
        updateMainContentLayoutParams()
        updateFavouriteBtnOnConfigChange(newConfig)
    }

    override fun onDestroy() {
        addVideoDialogFragment = null

        notificationManager.cancelAll()

        unregisterReceiver(networkStateReceiver)
        unregisterReceiver(pausePlaybackIntentReceiver)
        unregisterReceiver(resumePlaybackIntentReceiver)
        unregisterReceiver(deleteNotificationIntentReceiver)
        unregisterReceiver(prevTrackIntentReceiver)
        unregisterReceiver(nextTrackIntentReceiver)

        spotifyPlayer?.removeNotificationCallback(this)
        spotifyPlayer?.removeConnectionStateCallback(this)
        Spotify.destroyPlayer(this)

        super.onDestroy()
    }

    // region spotify player

    fun showLoginDialog() {
        if (!playerLoggedIn) MaterialDialog.Builder(this)
                .title(R.string.spotify_login)
                .content(R.string.playback_requires_login)
                .positiveText(R.string.login)
                .negativeText(R.string.cancel)
                .onPositive { _, _ -> openLoginWindow() }
                .build()
                .apply { show() }
    }

    private val loggedInCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, id: Int) = invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isLoggedIn = viewModel.viewState.isLoggedIn.get() ?: false

        menu?.apply {
            findItem(R.id.action_login)?.isVisible = !isLoggedIn
            findItem(R.id.action_logout)?.isVisible = isLoggedIn
        }

        drawer_navigation_view?.menu?.apply {
            findItem(R.id.drawer_action_login)?.isVisible = !isLoggedIn
            findItem(R.id.drawer_action_logout)?.isVisible = isLoggedIn
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun resetProgressAndPlay(uri: String) {
        sliding_layout?.expandIfHidden()
        playback_seek_bar?.progress = 0
        spotifyPlayer?.playUri(loggerSpotifyPlayerOperationCallback, uri, 0, 0)
    }

    private var lastPlayedTrack: Track? = null

    fun loadTrack(track: Track) {
        if (viewModel.viewState.isLoggedIn.get() == false || lastPlayedTrack == track) return

        stopYoutubePlayback()
        lastPlayedAlbum = null
        lastPlayedPlaylist = null

        lastPlayedTrack = track
        viewModel.viewState.playerState.set(PlayerState.TRACK)

        resetProgressAndPlay(track.uri)
    }

    private var lastPlayedAlbum: Album? = null

    fun loadAlbum(album: Album) {
        if (viewModel.viewState.isLoggedIn.get() == false || lastPlayedAlbum == album) return

        stopYoutubePlayback()
        lastPlayedTrack = null
        lastPlayedPlaylist = null

        lastPlayedAlbum = album
        viewModel.viewState.playerState.set(PlayerState.ALBUM)

        resetProgressAndPlay(album.uri)
    }

    private var lastPlayedPlaylist: Playlist? = null

    fun loadPlaylist(playlist: Playlist) {
        if (viewModel.viewState.isLoggedIn.get() == false || lastPlayedPlaylist == playlist) return

        stopYoutubePlayback()
        lastPlayedTrack = null
        lastPlayedAlbum = null

        lastPlayedPlaylist = playlist
        viewModel.viewState.playerState.set(PlayerState.PLAYLIST)

        resetProgressAndPlay(playlist.uri)
    }

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
        stopSpotifyPlayback()
    }

    private fun stopSpotifyPlayback() {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
        lastPlayedTrack = null
        lastPlayedPlaylist = null
        lastPlayedAlbum = null
    }

    override fun onSuccess() {
        Log.e("playerOperation", "Success")
    }

    override fun onError(error: Error?) {
        Log.e("playerOperation", error?.name
                ?: "error unknown")
    }

    override fun onPlaybackError(error: Error?) {
        Log.e("ERR", "onPlaybackError: ${error?.name ?: "error unknown"}")
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

    private val similarTracksFragment: SpotifyTracksFragment?
        get() = supportFragmentManager.findFragmentById(R.id.similar_tracks_fragment) as? SpotifyTracksFragment

    override fun setupObservers() {
        super.setupObservers()
        viewModel.viewState.similarTracks.observe(this, Observer { tracks ->
            tracks?.let { similarTracksFragment?.resetItems(it) }
        })
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
                if (backgroundPlaybackNotificationIsShowing) refreshPlaybackNotification()
            }

            PlayerEvent.kSpPlaybackNotifyPause -> {
                spotifyPlaybackTimer?.cancel()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.play)
                if (backgroundPlaybackNotificationIsShowing) refreshPlaybackNotification()
            }

            PlayerEvent.kSpPlaybackNotifyTrackChanged -> playerMetadata?.currentTrack?.let {
                val trackName = it.name
                val artistName = it.artistName
                val currentTrackLabelText = if (trackName != null && artistName != null) "$artistName - $trackName" else ""
                viewModel.viewState.currentTrackTitle.set(currentTrackLabelText)
                it.id?.let { id -> viewModel.getSimilarTracks(userReadPrivateAccessTokenEntity, id) }
                sliding_layout?.setDragView(spotify_player_layout)

                Picasso.with(this)
                        .load(it.albumCoverWebUrl)
                        .error(R.drawable.error_placeholder)
                        .placeholder(R.drawable.track_placeholder)
                        .into(current_track_image_view)
            }

            else -> return
        }
    }

    override fun onLoggedOut() {
        viewModel.viewState.isLoggedIn.set(false)
        viewModel.drawerViewState.user.set(null)
        Toast.makeText(this, "You logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggedIn() {
        viewModel.viewState.isLoggedIn.set(true)
        Toast.makeText(this, "You successfully logged in.", Toast.LENGTH_SHORT).show()

        if (spotifyPlayer?.isInitialized == true)
            onLoginSuccessful?.invoke()
        onLoginSuccessful = null
    }

    override fun onConnectionMessage(message: String?) {
        Log.e("onConnectionMessage: ", message ?: "Unknown connection message.")
    }

    override fun onLoginFailed(error: Error?) {
        Log.e("ERR", "onLoginFailed")
        Toast.makeText(this, "Login failed: ${error?.name
                ?: "error unknown"}", Toast.LENGTH_SHORT).show()
    }

    override fun onTemporaryError() {
        Log.e("ERR", "onTemporaryError")
    }

    private var spotifyPlayer: SpotifyPlayer? = null

    private var playerMetadata: Metadata? = null

    private var currentPlaybackState: PlaybackState? = null

    private val networkStateReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (spotifyPlayer != null) {
                    val connectivity = getNetworkConnectivity(baseContext)
                    spotifyPlayer?.setConnectivityStatus(this@MainActivity, connectivity)
                }
            }
        }
    }

    val loggedInObservable: ObservableField<Boolean>
        get() = viewModel.viewState.isLoggedIn

    val playerLoggedIn: Boolean
        get() = spotifyPlayer?.isLoggedIn == true

    private fun logOutPlayer() {
        spotifyPlayer?.logout()
    }

    private fun getNetworkConnectivity(context: Context): Connectivity {
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) Connectivity.fromNetworkType(activeNetwork.type)
        else Connectivity.OFFLINE
    }

    private fun initSpotifyPlayer() {
        registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(deleteNotificationIntentReceiver, IntentFilter(ACTION_DELETE_NOTIFICATION))
        registerReceiver(pausePlaybackIntentReceiver, IntentFilter(ACTION_PAUSE_PLAYBACK))
        registerReceiver(resumePlaybackIntentReceiver, IntentFilter(ACTION_RESUME_PLAYBACK))
        registerReceiver(prevTrackIntentReceiver, IntentFilter(ACTION_PREV_TRACK))
        registerReceiver(nextTrackIntentReceiver, IntentFilter(ACTION_NEXT_TRACK))

        spotifyPlayer?.addNotificationCallback(this)
        spotifyPlayer?.addConnectionStateCallback(this)
    }

    private val loggerSpotifyPlayerOperationCallback = object : Player.OperationCallback {
        override fun onSuccess() {
            Log.e("SUCCESS", "loggerSpotifyPlayerOperationCallback")
        }

        override fun onError(error: Error) {
            Log.e("ERR", "loggerSpotifyPlayerOperationCallback ${error.name}")
        }
    }

    private fun openLoginWindow() {
        val request = AuthenticationRequest.Builder(SpotifyClient.id, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request)
    }

    private lateinit var userReadPrivateAccessToken: String

    val userReadPrivateAccessTokenEntity: AccessTokenEntity
        get() = AccessTokenEntity(userReadPrivateAccessToken, System.currentTimeMillis())

    private fun onAuthenticationComplete(accessToken: String) {
        userReadPrivateAccessToken = accessToken
        viewModel.getCurrentUser(userReadPrivateAccessTokenEntity)

        if (spotifyPlayer == null) {
            val playerConfig = Config(applicationContext, accessToken, SpotifyClient.id)

            spotifyPlayer = Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
                override fun onInitialized(player: SpotifyPlayer) {
                    spotifyPlayer?.setConnectivityStatus(loggerSpotifyPlayerOperationCallback, getNetworkConnectivity(this@MainActivity))
                    spotifyPlayer?.addNotificationCallback(this@MainActivity)
                    spotifyPlayer?.addConnectionStateCallback(this@MainActivity)
                }

                override fun onError(error: Throwable) {
                    Log.e("ERR", "SpotifyPlayer.InitializationObserver")
                }
            })
        } else {
            spotifyPlayer?.login(accessToken)
        }
    }

    var onLoginSuccessful: (() -> Unit)? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> onAuthenticationComplete(response.accessToken)
                AuthenticationResponse.Type.ERROR -> Log.e("ERR", "Auth error: " + response.error)
                else -> Log.e("ERR", "Auth result: " + response.type)
            }
        }
    }

    // endregion

    // region search

    private var searchViewMenuItem: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchViewMenuItem = menu?.findItem(R.id.search_view_menu_item)
        val searchView = searchViewMenuItem?.actionView as? SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.search_view_menu_item -> true
        R.id.action_login -> {
            if (!playerLoggedIn) openLoginWindow()
            true
        }
        R.id.action_logout -> {
            if (playerLoggedIn) logOutPlayer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_SEARCH -> handleSearchIntent(intent)
        }
    }

    private fun handleSearchIntent(intent: Intent) {
        val query = intent.getStringExtra(SearchManager.QUERY)
        if (query.isNullOrBlank()) return

        SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE).run {
            saveRecentQuery(query, null)
        }

        searchViewMenuItem?.collapseActionView()

        val currentFragment = pagerAdapter.currentHostFragment
        currentFragment?.showFragment(SearchFragment.newInstance(query), true)
    }

    // endregion

    // region backNavigation

    override fun onBackPressed() {
        val currentFragment = pagerAdapter.currentHostFragment

        if (sliding_layout?.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            return
        }

        if (currentFragment != null && currentFragment.childFragmentManager.backStackEntryCount > 0) {
            currentFragment.topFragment?.let {
                if (it is GoesToPreviousStateOnBackPressed) {
                    it.onBackPressed()
                    return
                }
            }

            showMainToolbarOnBackPressed(currentFragment)

            currentFragment.childFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    fun backPressedOnNoPreviousFragmentState() {
        val currentFragment = pagerAdapter.currentFragment
        if (currentFragment != null && currentFragment.childFragmentManager.backStackEntryCount > 0) {
            showMainToolbarOnBackPressed(currentFragment)
            currentFragment.childFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    private fun showMainToolbarOnBackPressed(currentFragment: Fragment) {
        if (currentFragment.childFragmentManager.backStackEntryCount == 1) {
            val mainToolbar = (currentFragment as? HasMainToolbar)?.toolbar
            setSupportActionBar(mainToolbar)
            showDrawerHamburger()
        }
    }

    // endregion

    private val view: MainView by lazy {
        MainView(
                state = viewModel.viewState,
                onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                onDrawerNavigationItemSelectedListener = onDrawerNavigationItemSelectedListener,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                offScreenPageLimit = 2,
                fadeOnClickListener = fadeOnClickListener,
                slideListener = slideListener,
                initialSlidePanelState = SlidingUpPanelLayout.PanelState.HIDDEN,
                relatedVideosRecyclerViewItemView = relatedVideosRecyclerViewItemView,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener,
                onSpotifyPlayPauseBtnClickListener = onSpotifyPlayPauseBtnClickListener,
                onCloseSpotifyPlayerBtnClickListener = onCloseSpotifyPlayerBtnClickListener,
                onPreviousBtnClickListener = onPreviousBtnClickListener,
                onNextBtnClickListener = onNextBtnClickListener,
                onPlaybackSeekBarProgressChangeListener = onPlaybackSeekBarProgressChangeListener
        )
    }

    // region drawer navigation

    private val onDrawerNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.drawer_action_settings -> Intent(this, SettingsActivity::class.java).run { startActivity(this) }

            R.id.drawer_action_remove_video_search_data -> {
                viewModel.deleteAllVideoSearchData()
                Toast.makeText(this, "Video cache cleared", Toast.LENGTH_SHORT).show()
            }

            R.id.drawer_action_about -> {

            }

            R.id.drawer_action_login -> if (!playerLoggedIn) openLoginWindow()

            R.id.drawer_action_logout -> if (playerLoggedIn) logOutPlayer()
        }

        main_drawer_layout?.closeDrawer(Gravity.START)
        true
    }

    // endregion

    // region pager navigation

    private val itemIds: Array<Int> = arrayOf(R.id.action_dashboard, R.id.action_user, R.id.action_favorites)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == main_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        main_view_pager.currentItem = itemIds.indexOf(item.itemId)
        toggleToolbar()

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy { MainFragmentPagerAdapter(supportFragmentManager) }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
            toggleToolbar()
        }
    }

    private fun toggleToolbar() {
        val currentTopFragment = (pagerAdapter.currentFragment as BaseHostFragment).topFragment
        val mainToolbar = (currentTopFragment as? HasMainToolbar)?.toolbar
        setSupportActionBar(mainToolbar)
        if (currentTopFragment?.childFragmentManager?.backStackEntryCount == 0) showDrawerHamburger()
    }

    // endregion

    // region SlidingPanel

    private val fadeOnClickListener = View.OnClickListener {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private val slideListener = object : SlidingUpPanelLayout.PanelSlideListener {
        override fun onPanelSlide(panel: View?, slideOffset: Float) = updatePlayersDimensions(slideOffset)

        override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
        ) {
            when (newState) {
                SlidingUpPanelLayout.PanelState.EXPANDED -> minimizeBtn.setImageResource(R.drawable.minimize)
                SlidingUpPanelLayout.PanelState.COLLAPSED -> minimizeBtn.setImageResource(R.drawable.maximize)
                SlidingUpPanelLayout.PanelState.HIDDEN -> {
                    youTubePlayer?.pause()
                    lastPlayedVideo = null
                    lastPlayedPlaylist = null

                    spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
                    lastPlayedTrack = null
                    lastPlayedAlbum = null
                    lastPlayedPlaylist = null
                }
                else -> return
            }
        }
    }

    // endregion

    // region YoutubePlayer

    private val playerMaxVerticalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { (dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() }
    private val youtubePlayerMaxHorizontalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(screenHeight.toFloat()).toInt() }
    private val minimumPlayerHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(minimumPlayerHeightDp.toFloat()).toInt() }

    private var currentSlideOffset: Float = 0.0f

    private var youTubePlayer: YouTubePlayer? = null

    private fun initYouTubePlayerView() = with(youtube_player_view) {
        lifecycle.addObserver(this)
        initialize({ this@MainActivity.youTubePlayer = it }, true)
        playerUIController.showFullscreenButton(false)
        playerUIController.showVideoTitle(true)
    }

    private fun stopYoutubePlayback() {
        youTubePlayer?.pause()
        lastPlayedVideo = null
        lastPlayedPlaylist = null
    }

    private var lastPlayedVideo: Video? = null

    private fun pauseSpotifyPlayerAndNullifyLastPlayedItems() {
        spotifyPlayer?.pause(loggerSpotifyPlayerOperationCallback)
        lastPlayedTrack = null
        lastPlayedPlaylist = null
        lastPlayedAlbum = null
    }

    private fun setDragViewToYoutubeAndExpand() {
        sliding_layout?.setDragView(youtube_player_view_container_layout)
        sliding_layout?.expandIfHidden()
    }

    private fun playVideo(video: Video) {
        if (lifecycle.currentState == Lifecycle.State.RESUMED) youTubePlayer?.loadVideo(video.id, 0f)
        else youTubePlayer?.cueVideo(video.id, 0f)
    }

    fun loadVideo(video: Video) {
        if (video == lastPlayedVideo) return
        lastPlayedVideo = video

        pauseSpotifyPlayerAndNullifyLastPlayedItems()
        lastVideoPlaylist = null

        viewModel.viewState.playerState.set(PlayerState.VIDEO)

        setDragViewToYoutubeAndExpand()
        youTubePlayer?.removeListener(playlistYoutubePlayerStateChangeListener)
        playVideo(video)

        youtube_player_view?.playerUIController?.setVideoTitle(video.title)

        viewModel.searchRelatedVideos(video)
    }

    private var lastVideoPlaylist: VideoPlaylist? = null
    private var videosToPlay: List<Video>? = null
    private var currentVideoIndex = 0

    private val playlistYoutubePlayerStateChangeListener = object : OnYoutubePlayerStateChangeListener {
        override fun onStateChange(state: PlayerConstants.PlayerState) {
            if (state == PlayerConstants.PlayerState.ENDED) {
                if (videosToPlay?.size ?: 0 > ++currentVideoIndex) {
                    playVideo(videosToPlay!![currentVideoIndex])
                } else {
                    sliding_layout?.hideIfVisible()
                    Toast.makeText(this@MainActivity, "${lastPlayedPlaylist?.name
                            ?: "Unknown playlist"} has ended.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (videoPlaylist == lastVideoPlaylist) return

        lastVideoPlaylist = videoPlaylist

        if (videos.isEmpty()) {
            Toast.makeText(this, "Playlist is empty.", Toast.LENGTH_SHORT).show()
            return
        }

        videosToPlay = videos
        currentVideoIndex = 0

        pauseSpotifyPlayerAndNullifyLastPlayedItems()
        lastPlayedVideo = null

        viewModel.viewState.playerState.set(PlayerState.VIDEO_PLAYLIST)

        setDragViewToYoutubeAndExpand()
        youTubePlayer?.addListener(playlistYoutubePlayerStateChangeListener)

        val firstVideo = videos.first()
        playVideo(firstVideo)

        youtube_player_view?.playerUIController?.setVideoTitle(firstVideo.title)
        viewModel.searchRelatedVideos(firstVideo)
    }

    private fun updatePlayersDimensions(slideOffset: Float) {
        if (sliding_layout.panelState != SlidingUpPanelLayout.PanelState.HIDDEN && slideOffset >= 0) {
            currentSlideOffset = slideOffset
            val youtubePlayerLayoutParams = youtube_player_view_container_layout.layoutParams
            val spotifyPlayerLayoutParams = spotify_player_layout.layoutParams
            val youtubePlayerHeight = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                (playerMaxVerticalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            } else {
                (youtubePlayerMaxHorizontalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            }
            val spotifyPlayerHeight = ((dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() - minimumPlayerHeight) * slideOffset + minimumPlayerHeight

            youtubePlayerLayoutParams.height = youtubePlayerHeight.toInt()
            spotifyPlayerLayoutParams.height = spotifyPlayerHeight.toInt()

            val youtubePlayerGuidelinePercentage = (1 - minimumYoutubePlayerGuidelinePercent) * slideOffset + minimumYoutubePlayerGuidelinePercent
            youtube_player_guideline.setGuidelinePercent(youtubePlayerGuidelinePercentage)

            youtube_player_view_container_layout.requestLayout()
            spotify_player_layout.requestLayout()
            youtube_player_guideline.requestLayout()
        }
    }

    private fun updateFavouriteBtnOnConfigChange(
            newConfig: Configuration?
    ) = if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) add_to_favourites_fab?.animate()?.alpha(0f)
    else add_to_favourites_fab?.animate()?.alpha(1f)

    // endregion

    private fun updateMainContentLayoutParams() = with(main_content_layout) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layoutParams.apply {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                requestLayout()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    // region player controls

    private val minimizeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(this).apply {
            setImageResource(R.drawable.maximize)
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(10, 10, 10, 10)
            }
            setOnClickListener {
                if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                    minimizeBtn.setImageResource(R.drawable.minimize)
                } else if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                    minimizeBtn.setImageResource(R.drawable.maximize)
                }
            }
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private val closeBtn: ImageButton by lazy(LazyThreadSafetyMode.NONE) {
        ImageButton(this).apply {
            setImageResource(R.drawable.close)
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(10, 10, 10, 10)
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
            setOnClickListener {
                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
                youTubePlayer?.pause()
                lastPlayedVideo = null
            }
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun initPlayerViewControls() = findViewById<RelativeLayout>(R.id.controls_root).apply {
        addView(minimizeBtn)
        addView(closeBtn)
        val titleParams = findViewById<TextView>(R.id.video_title)?.layoutParams as RelativeLayout.LayoutParams
        titleParams.setMargins(dpToPx(20f).toInt(), 0, dpToPx(20f).toInt(), 0)
    }

    // endregion

    // region relatedVideos

    private val relatedVideosRecyclerViewItemView: RecyclerViewItemView<VideoItemView> by lazy(LazyThreadSafetyMode.NONE) {
        RecyclerViewItemView(
                RecyclerViewItemViewState(viewModel.viewState.initialVideosLoadingInProgress, viewModel.viewState.videos),
                object : ListItemView<VideoItemView>(viewModel.viewState.videos) {
                    override val itemViewBinder: ItemBinder<VideoItemView>
                        get() = ItemBinderBase(BR.videoView, R.layout.video_item)
                },
                ClickHandler { loadVideo(it.video) },
                relatedVideosItemDecoration,
                onRelatedVideosScrollListener
        )
    }

    private val onRelatedVideosScrollListener: RecyclerView.OnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() = viewModel.searchRelatedVideosWithToLastId()
        }
    }

    private val relatedVideosItemDecoration: RecyclerView.ItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SeparatorDecoration(this, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
    }

    // endregion

    // region favourites

    private var addVideoDialogFragment: AddVideoDialogFragment? = null

    private val onFavouriteBtnClickListener = View.OnClickListener { _ ->
        viewModel.viewState.playerState.get()?.let { playerState ->
            when (playerState) {
                PlayerState.VIDEO -> lastPlayedVideo?.let {
                    viewModel.getFavouriteVideoPlaylists()
                    addVideoDialogFragment = AddVideoDialogFragment().apply {
                        state = AddVideoViewState(viewModel.viewState.favouriteVideoPlaylists)
                        show(supportFragmentManager, TAG_ADD_VIDEO)
                    }
                }
                PlayerState.PLAYLIST -> lastPlayedPlaylist?.let {
                    viewModel.togglePlaylistFavouriteState(
                            it,
                            { Toast.makeText(this, "${it.name} added to favourite playlists.", Toast.LENGTH_SHORT).show() },
                            { Toast.makeText(this, "${it.name} deleted from favourite playlists.", Toast.LENGTH_SHORT).show() }
                    )
                }
                PlayerState.TRACK -> lastPlayedTrack?.let {
                    viewModel.toggleTrackFavouriteState(
                            it,
                            { Toast.makeText(this, "${it.name} added to favourite tracks.", Toast.LENGTH_SHORT).show() },
                            { Toast.makeText(this, "${it.name} deleted from favourite tracks.", Toast.LENGTH_SHORT).show() }
                    )
                }
                PlayerState.ALBUM -> lastPlayedAlbum?.let {
                    viewModel.toggleAlbumFavouriteState(
                            it,
                            { Toast.makeText(this, "${it.name} added to favourite albums.", Toast.LENGTH_SHORT).show() },
                            { Toast.makeText(this, "${it.name} deleted from favourite albums.", Toast.LENGTH_SHORT).show() }
                    )
                }
                else -> return@OnClickListener
            }
        }
    }

    fun addVideoToPlaylist(playlist: VideoPlaylist) = lastPlayedVideo?.let {
        viewModel.addVideoToPlaylist(it, playlist) {
            Toast.makeText(this, "Video added to playlist: ${playlist.name}.", Toast.LENGTH_SHORT).show()
        }
    }

    fun showNewPlaylistDialog() = lastPlayedVideo?.let {
        MaterialDialog.Builder(this)
                .title(getString(R.string.new_playlist))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.playlist_name), "") { _, input ->
                    val newPlaylistName = input.trim().toString()
                    addVideoDialogFragment?.dismiss()
                    viewModel.addVideoPlaylistWithVideo(VideoPlaylist(name = newPlaylistName), video = it) {
                        Toast.makeText(
                                this@MainActivity,
                                "Video added to playlist: $newPlaylistName.",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }.positiveText(getString(R.string.ok))
                .build()
                .apply { show() }
    }

    // endregion

    companion object {
        private const val minimumPlayerHeightDp = 110
        private const val minimumYoutubePlayerGuidelinePercent = .5f

        private const val TAG_ADD_VIDEO = "TAG_ADD_VIDEO"

        private const val LOGIN_REQUEST_CODE = 100
        private const val REDIRECT_URI = "testschema://callback"
        private val SCOPES = arrayOf(
                "user-read-private",
                "user-library-read",
                "user-top-read",
                "playlist-read",
                "playlist-read-private",
                "streaming",
                "user-read-birthdate",
                "user-read-email"
        )

        private const val PLAYBACK_NOTIFICATION_ID = 100

        private const val ACTION_PAUSE_PLAYBACK = "ACTION_PAUSE_PLAYBACK"
        private const val ACTION_RESUME_PLAYBACK = "ACTION_RESUME_PLAYBACK"
        private const val ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION"
        private const val ACTION_PREV_TRACK = "ACTION_PREV_TRACK"
        private const val ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK"
    }
}
