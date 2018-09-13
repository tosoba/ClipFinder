package com.example.there.findclips.main

import android.app.SearchManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
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
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.findclips.R
import com.example.there.findclips.SpotifyClient
import com.example.there.findclips.base.activity.BaseVMActivity
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.fragment.addvideo.AddVideoDialogFragment
import com.example.there.findclips.fragment.addvideo.AddVideoViewState
import com.example.there.findclips.fragment.search.SearchFragment
import com.example.there.findclips.fragment.search.SearchSuggestionProvider
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.list.impl.RelatedVideosList
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


class MainActivity : BaseVMActivity<MainViewModel>(), HasSupportFragmentInjector, Player.NotificationCallback, ConnectionStateCallback, Player.OperationCallback {

    val connectivitySnackbarParentView: View?
        get() = findViewById(R.id.main_view_pager)

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(disposablesComponent)
        initItemClicks()

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view
        binding.relatedVideosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.relatedVideosRecyclerView.addOnInitialUserScrollListener()

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

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    // region spotify player

    fun showLoginDialog() {
        if (!loggedIn) {
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

    private var lastPlayedTrack: Track? = null

    fun loadTrack(track: Track) {
        if (viewModel.viewState.isLoggedIn.get() == false || lastPlayedTrack == track) return

        youTubePlayer?.pause()
        lastPlayedVideo = null

        //TODO: nullify lastPlayedPlaylist and lastPlayedAlbum

        lastPlayedTrack = track
        viewModel.viewState.playerState.set(PlayerState.TRACK)

        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        spotifyPlayer?.playUri(spotifyPlayerOperationCallback, TRACK_URI, 0, 0)
    }

    private val onSpotifyPlayPauseBtnClickListener = View.OnClickListener {
        if (currentPlaybackState != null && currentPlaybackState!!.isPlaying) {
            spotifyPlayer?.pause(spotifyPlayerOperationCallback)
        } else {
            spotifyPlayer?.resume(spotifyPlayerOperationCallback)
        }
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

    override fun onPlaybackEvent(event: PlayerEvent) {
        Log.e("PlaybackEvent", event.toString())
        playerMetadata = spotifyPlayer?.metadata
        currentPlaybackState = spotifyPlayer?.playbackState
    }

    override fun onLoggedOut() {
        viewModel.viewState.isLoggedIn.set(false)
        Toast.makeText(this, "You logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggedIn() {
        Toast.makeText(this, "You successfully logged in.", Toast.LENGTH_SHORT).show()
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

    val loggedIn: Boolean
        get() = spotifyPlayer?.isLoggedIn == true

    fun logOutPlayer() {
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
            Log.e("TEST", "ERR")
        }

        override fun onError(error: Error) {
            Log.e("TEST", "ERR")
        }
    }

    fun openLoginWindow() {
        val request = AuthenticationRequest.Builder(SpotifyClient.id, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(arrayOf("user-read-private", "playlist-read", "playlist-read-private", "streaming"))
                .build()

        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request)
    }

    private fun onAuthenticationComplete(accessToken: String) {
        if (spotifyPlayer == null) {
            val playerConfig = Config(applicationContext, accessToken, SpotifyClient.id)

            spotifyPlayer = Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
                override fun onInitialized(player: SpotifyPlayer) {
                    spotifyPlayer?.setConnectivityStatus(spotifyPlayerOperationCallback, getNetworkConnectivity(this@MainActivity))
                    spotifyPlayer?.addNotificationCallback(this@MainActivity)
                    spotifyPlayer?.addConnectionStateCallback(this@MainActivity)
                }

                override fun onError(error: Throwable) {
                    Log.e("TEST", "ERR")
                }
            })
        } else {
            spotifyPlayer?.login(accessToken)
        }

        viewModel.viewState.isLoggedIn.set(true)
    }

    var onLoginSuccessful: (() -> Unit)? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    onAuthenticationComplete(response.accessToken)
                    onLoginSuccessful?.invoke()
                    onLoginSuccessful = null
                }
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
            if (!loggedIn) openLoginWindow()
            true
        }
        R.id.action_logout -> {
            if (loggedIn) logOutPlayer()
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
                onRelatedVideosScroll = onRelatedVideosScrollListener,
                itemDecoration = relatedVideosItemDecoration,
                relatedVideosAdapter = relatedVideosAdapter,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener,
                onSpotifyPlayPauseBtnClickListener = onSpotifyPlayPauseBtnClickListener,
                onCloseSpotifyPlayerBtnClickListener = onCloseSpotifyPlayerBtnClickListener
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
            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                minimizeBtn.setImageResource(R.drawable.minimize)
            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                minimizeBtn.setImageResource(R.drawable.maximize)
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

    private var lastPlayedVideo: Video? = null

    fun loadVideo(video: Video) {
        if (video == lastPlayedVideo) return
        lastPlayedVideo = video

        spotifyPlayer?.pause(spotifyPlayerOperationCallback)
        //TODO: nullify all spotify playback objects
        lastPlayedTrack = null

        viewModel.viewState.playerState.set(PlayerState.VIDEO)

        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            youTubePlayer?.loadVideo(video.id, 0f)
        else
            youTubePlayer?.cueVideo(video.id, 0f)

        viewModel.searchRelatedVideos(video) {
            if (!relatedVideosAdapter.userHasScrolled)
                related_videos_recycler_view?.scrollToPosition(0)
        }
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
            add_video_to_favourites_fab?.animate()?.alpha(0f)
        } else {
            add_video_to_favourites_fab?.animate()?.alpha(1f)
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

    private val relatedVideosAdapter: RelatedVideosList.Adapter by lazy(LazyThreadSafetyMode.NONE) {
        RelatedVideosList.Adapter(viewModel.viewState.videos, R.layout.related_video_item, viewModel.viewState.loadingMoreVideosInProgress)
    }

    private val onRelatedVideosScrollListener: RecyclerView.OnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerOnScrollListener(returnFromOnScrolledItemCount = 1) {
            override fun onLoadMore() = viewModel.searchRelatedVideosWithToLastId()
        }
    }

    private val relatedVideosItemDecoration: RecyclerView.ItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SeparatorDecoration(this, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
    }

    private fun initItemClicks() {
        disposablesComponent.add(relatedVideosAdapter.itemClicked.subscribe { loadVideo(it) })
    }

    // endregion

    // region favourites

    private var addVideoDialogFragment: AddVideoDialogFragment? = null

    private val onFavouriteBtnClickListener = View.OnClickListener {
        viewModel.getFavouriteVideoPlaylists()
        addVideoDialogFragment = AddVideoDialogFragment().apply {
            state = AddVideoViewState(viewModel.viewState.favouriteVideoPlaylists)
            show(supportFragmentManager, TAG_ADD_VIDEO)
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
        private const val TRACK_URI = "spotify:track:6KywfgRqvgvfJc3JRwaZdZ"
    }
}
