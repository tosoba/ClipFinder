package com.example.there.findclips.main

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
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.SearchRecentSuggestions
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.findclips.BR
import com.example.there.findclips.R
import com.example.there.findclips.SpotifyClient
import com.example.there.findclips.base.activity.BaseVMActivity
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.fragment.addvideo.AddVideoDialogFragment
import com.example.there.findclips.fragment.addvideo.AddVideoViewState
import com.example.there.findclips.fragment.list.SpotifyTracksFragment
import com.example.there.findclips.fragment.search.SearchFragment
import com.example.there.findclips.fragment.search.SearchSuggestionProvider
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnSeekBarProgressChangeListener
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.*
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

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view
        binding.relatedVideosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        initYouTubePlayerView()
        addPlayerViewControls()

        initSpotifyPlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updatePlayerDimensions(currentSlideOffset)
        updateMainContentLayoutParams()
        updateFavouriteBtnOnConfigChange(newConfig)
    }

    override fun onDestroy() {
        addVideoDialogFragment = null

        unregisterReceiver(networkStateReceiver)

        spotifyPlayer?.removeNotificationCallback(this)
        spotifyPlayer?.removeConnectionStateCallback(this)
        Spotify.destroyPlayer(this)

        super.onDestroy()
    }

    // region spotify player

    fun showLoginDialog() {
        if (!playerLoggedIn) {
            MaterialDialog.Builder(this)
                    .title(R.string.spotify_login)
                    .content(R.string.playback_requires_login)
                    .positiveText(R.string.login)
                    .negativeText(R.string.cancel)
                    .onPositive { _, _ -> openLoginWindow() }
                    .build()
                    .apply { show() }
        }
    }

    private val loggedInCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, id: Int) = invalidateOptionsMenu()
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewState.isLoggedIn.addOnPropertyChangedCallback(loggedInCallback)
    }

    override fun onStop() {
        super.onStop()
        viewModel.viewState.isLoggedIn.removeOnPropertyChangedCallback(loggedInCallback)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isLoggedIn = viewModel.viewState.isLoggedIn.get() ?: false
        menu?.findItem(R.id.action_login)?.isVisible = !isLoggedIn
        menu?.findItem(R.id.action_logout)?.isVisible = isLoggedIn
        return super.onPrepareOptionsMenu(menu)
    }

    private fun resetProgressAndPlay(uri: String) {
        sliding_layout?.expandIfHidden()
        playback_seek_bar?.progress = 0
        spotifyPlayer?.playUri(spotifyPlayerOperationCallback, uri, 0, 0)
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
            spotifyPlayer?.pause(spotifyPlayerOperationCallback)
        } else {
            spotifyPlayer?.resume(spotifyPlayerOperationCallback)
        }
    }

    private val onNextBtnClickListener = View.OnClickListener {
        spotifyPlayer?.skipToNext(spotifyPlayerOperationCallback)
    }

    private val onPreviousBtnClickListener = View.OnClickListener {
        spotifyPlayer?.skipToPrevious(spotifyPlayerOperationCallback)
    }

    private val onCloseSpotifyPlayerBtnClickListener = View.OnClickListener {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        spotifyPlayer?.pause(spotifyPlayerOperationCallback)
        lastPlayedTrack = null
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
                    spotifyPlayer?.seekToPosition(spotifyPlayerOperationCallback, positionInMs)
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
        playerMetadata = spotifyPlayer?.metadata
        currentPlaybackState = spotifyPlayer?.playbackState

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
            }

            PlayerEvent.kSpPlaybackNotifyPause -> {
                spotifyPlaybackTimer?.cancel()
                spotify_player_play_pause_image_button?.setImageResource(R.drawable.play)
            }

            PlayerEvent.kSpPlaybackNotifyTrackChanged -> playerMetadata?.currentTrack?.let {
                val trackName = it.name
                val artistName = it.artistName
                val currentTrackLabelText = if (trackName != null && artistName != null) "$artistName - $trackName" else ""
                viewModel.viewState.currentTrackTitle.set(currentTrackLabelText)
                it.id?.let { id -> viewModel.getSimilarTracks(userReadPrivateAccessTokenEntity, id) }
                sliding_layout?.setDragView(spotify_player_layout)
            }

            else -> return
        }
    }

    override fun onLoggedOut() {
        viewModel.viewState.isLoggedIn.set(false)
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
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, filter)

        spotifyPlayer?.addNotificationCallback(this)
        spotifyPlayer?.addConnectionStateCallback(this)
    }

    private val spotifyPlayerOperationCallback = object : Player.OperationCallback {
        override fun onSuccess() {
            Log.e("SUCCESS", "spotifyPlayerOperationCallback")
        }

        override fun onError(error: Error) {
            Log.e("ERR", "spotifyPlayerOperationCallback ${error.name}")
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

        if (spotifyPlayer == null) {
            val playerConfig = Config(applicationContext, accessToken, SpotifyClient.id)

            spotifyPlayer = Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
                override fun onInitialized(player: SpotifyPlayer) {
                    spotifyPlayer?.setConnectivityStatus(spotifyPlayerOperationCallback, getNetworkConnectivity(this@MainActivity))
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
        handleSearchIntent(intent)
    }

    private fun handleSearchIntent(intent: Intent?) {
        fun saveQuery(query: String) {
            val suggestions = SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
            suggestions.saveRecentQuery(query, null)
        }

        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            if (query.isNullOrBlank()) return
            saveQuery(query)

            searchViewMenuItem?.collapseActionView()

            val currentFragment = pagerAdapter.currentHostFragment
            currentFragment?.showFragment(SearchFragment.newInstance(query), true)
        }
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
        if (currentTopFragment?.childFragmentManager?.backStackEntryCount == 0) {
            setSupportActionBar(mainToolbar)
            showDrawerHamburger()
        }
    }

    // endregion

    // region SlidingPanel

    private val fadeOnClickListener = View.OnClickListener {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private val slideListener = object : SlidingUpPanelLayout.PanelSlideListener {
        override fun onPanelSlide(panel: View?, slideOffset: Float) = updatePlayerDimensions(slideOffset)

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

                    spotifyPlayer?.pause(spotifyPlayerOperationCallback)
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

    private val playerMaxVerticalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { (dpToPx(screenHeight.toFloat()) / 2).toInt() }
    private val playerMaxHorizontalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(screenHeight.toFloat()).toInt() }
    private val minimumPlayerHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(minimumPlayerHeightDp.toFloat()).toInt() }

    private var currentSlideOffset: Float = 0.0f

    private var youTubePlayer: YouTubePlayer? = null

    private fun initYouTubePlayerView() {
        lifecycle.addObserver(youtube_player_view)
        youtube_player_view.initialize({ this.youTubePlayer = it }, true)
        youtube_player_view.playerUIController.showFullscreenButton(false)
    }

    private fun stopYoutubePlayback() {
        youTubePlayer?.pause()
        lastPlayedVideo = null
    }

    private var lastPlayedVideo: Video? = null

    fun loadVideo(video: Video) {
        if (video == lastPlayedVideo) return
        lastPlayedVideo = video

        spotifyPlayer?.pause(spotifyPlayerOperationCallback)
        lastPlayedTrack = null
        lastPlayedPlaylist = null
        lastPlayedAlbum = null

        viewModel.viewState.playerState.set(PlayerState.VIDEO)

        sliding_layout?.setDragView(youtube_player_view)

        sliding_layout?.expandIfHidden()
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            youTubePlayer?.loadVideo(video.id, 0f)
        else
            youTubePlayer?.cueVideo(video.id, 0f)

        viewModel.searchRelatedVideos(video)
    }

    private fun updatePlayerDimensions(slideOffset: Float) {
        if (sliding_layout.panelState != SlidingUpPanelLayout.PanelState.HIDDEN && slideOffset >= 0) {
            currentSlideOffset = slideOffset
            val layoutParams = youtube_player_view.layoutParams
            val height = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                (playerMaxVerticalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            } else {
                (playerMaxHorizontalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            }
            layoutParams.height = height.toInt()
            youtube_player_view.requestLayout()
        }
    }

    private fun updateFavouriteBtnOnConfigChange(newConfig: Configuration?) {
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            add_to_favourites_fab?.animate()?.alpha(0f)
        } else {
            add_to_favourites_fab?.animate()?.alpha(1f)
        }
    }

    // endregion

    private fun updateMainContentLayoutParams() {
        main_content_layout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                main_content_layout.layoutParams.apply {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                main_content_layout.requestLayout()
                main_content_layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
            }
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun addPlayerViewControls() = findViewById<RelativeLayout>(R.id.controls_root).apply {
        addView(minimizeBtn)
        addView(closeBtn)
    }

    // endregion

    // region relatedVideos

    private val relatedVideosRecyclerViewItemView: RecyclerViewItemView<Video> by lazy(LazyThreadSafetyMode.NONE) {
        RecyclerViewItemView(
                RecyclerViewItemViewState(viewModel.viewState.initialVideosLoadingInProgress, viewModel.viewState.videos),
                object : ListItemView<Video>(viewModel.viewState.videos) {
                    override val itemViewBinder: ItemBinder<Video>
                        get() = ItemBinderBase(BR.video, R.layout.video_item)
                },
                ClickHandler { loadVideo(it) },
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
                    //TODO: add playlist to favourites
                }
                PlayerState.TRACK -> lastPlayedTrack?.let {
                    //TODO: add track to favourites
                }
                PlayerState.ALBUM -> lastPlayedAlbum?.let {
                    //TODO: add album to favourites
                }
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
        private const val minimumPlayerHeightDp = 100

        private const val TAG_ADD_VIDEO = "TAG_ADD_VIDEO"

        private const val LOGIN_REQUEST_CODE = 100
        private const val REDIRECT_URI = "testschema://callback"
        private val SCOPES = arrayOf("user-read-private", "user-library-read", "user-top-read", "playlist-read", "playlist-read-private", "streaming")
    }
}
