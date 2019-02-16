package com.example.there.findclips.main

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.data.api.SpotifyClient
import com.example.there.data.preferences.AppPreferences
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.findclips.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.activity.BaseVMActivity
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.databinding.DrawerHeaderBinding
import com.example.there.findclips.fragment.list.SpotifyTracksFragment
import com.example.there.findclips.fragment.player.spotify.SpotifyPlayerFragment
import com.example.there.findclips.fragment.player.youtube.YoutubePlayerFragment
import com.example.there.findclips.fragment.search.SearchFragment
import com.example.there.findclips.fragment.search.SearchSuggestionProvider
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.settings.SettingsActivity
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.list.item.VideoItemView
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.ConnectionStateCallback
import com.spotify.sdk.android.player.Error
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity :
        BaseVMActivity<MainViewModel>(MainViewModel::class.java),
        HasSupportFragmentInjector,
        ConnectionStateCallback {

    private val youtubePlayerFragment: YoutubePlayerFragment?
        get() = supportFragmentManager?.findFragmentById(R.id.youtube_player_fragment) as? YoutubePlayerFragment

    private val spotifyPlayerFragment: SpotifyPlayerFragment?
        get() = supportFragmentManager?.findFragmentById(R.id.spotify_player_fragment) as? SpotifyPlayerFragment

    val connectivitySnackbarParentView: View?
        get() = findViewById(R.id.main_view_pager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.deleteAllVideoSearchData()

        initViewBindings()

        setupNavigationFromSimilarTracks()

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
                PlayerState.TRACK -> spotifyPlayerFragment?.lastPlayedTrack?.let { viewModel.updateTrackFavouriteState(it) }
                PlayerState.PLAYLIST -> spotifyPlayerFragment?.lastPlayedPlaylist?.let { viewModel.updatePlaylistFavouriteState(it) }
                PlayerState.ALBUM -> spotifyPlayerFragment?.lastPlayedAlbum?.let { viewModel.updateAlbumFavouriteState(it) }
                else -> viewModel.viewState.itemFavouriteState.set(false)
            }
        })
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


    fun loadTrack(track: Track) {
        //TODO: check if player is actually in the middle of playing the track (maybe) same with album and playlist
        if (viewModel.viewState.isLoggedIn.get() == false) return //TODO: maybe show a toast with you need to be logged in msg
        youtubePlayerFragment?.stopPlayback()

        spotifyPlayerFragment?.loadTrack(track)

        viewModel.viewState.playerState.set(PlayerState.TRACK)
    }


    fun loadAlbum(album: Album) {
        if (viewModel.viewState.isLoggedIn.get() == false) return

        youtubePlayerFragment?.stopPlayback()

        spotifyPlayerFragment?.loadAlbum(album)

        viewModel.viewState.playerState.set(PlayerState.ALBUM)
    }


    fun loadPlaylist(playlist: Playlist) {
        if (viewModel.viewState.isLoggedIn.get() == false) return

        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadPlaylist(playlist)

        viewModel.viewState.playerState.set(PlayerState.PLAYLIST)
    }

    //TODO: move this to an interface
    fun hidePlayer() {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private val similarTracksFragment: SpotifyTracksFragment?
        get() = supportFragmentManager.findFragmentById(R.id.similar_tracks_fragment) as? SpotifyTracksFragment

    override fun setupObservers() {
        super.setupObservers()
        viewModel.viewState.similarTracks.observe(this, Observer { tracks ->
            tracks?.let { similarTracksFragment?.resetItems(it) }
        })
    }

    fun onSpotifyTrackChanged(id: String) {
        viewModel.getSimilarTracks(id)
        sliding_layout?.setDragView(spotifyPlayerFragment?.view)
    }

    override fun onLoggedOut() {
        viewModel.viewState.isLoggedIn.set(false)
        viewModel.drawerViewState.user.set(null)
        Toast.makeText(this, "You logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggedIn() {
        viewModel.viewState.isLoggedIn.set(true)
        Toast.makeText(this, "You successfully logged in.", Toast.LENGTH_SHORT).show()

        if (spotifyPlayerFragment?.isPlayerInitialized == true)
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


    val loggedInObservable: ObservableField<Boolean>
        get() = viewModel.viewState.isLoggedIn

    val playerLoggedIn: Boolean
        get() = spotifyPlayerFragment?.isPlayerLoggedIn == true

    private fun logOutPlayer() {
        spotifyPlayerFragment?.logOutPlayer()
    }


    private fun openLoginWindow() {
        val request = AuthenticationRequest.Builder(SpotifyClient.id, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request)
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    private fun onAuthenticationComplete(accessToken: String) {
        appPreferences.userPrivateAccessToken = AccessTokenEntity(accessToken, System.currentTimeMillis())
        viewModel.getCurrentUser()

        spotifyPlayerFragment?.onAuthenticationComplete(accessToken)
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
                onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    // region drawer navigation

    private val onDrawerNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.drawer_action_settings -> Intent(this, SettingsActivity::class.java).run {
                startActivity(this)
            }

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
                SlidingUpPanelLayout.PanelState.DRAGGING -> {
                    youtubePlayerFragment?.onDragging()
                }
                SlidingUpPanelLayout.PanelState.EXPANDED -> {
                    youtubePlayerFragment?.onExpanded()
                }
                SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                    youtubePlayerFragment?.onCollapsed()
                }
                SlidingUpPanelLayout.PanelState.HIDDEN -> {
                    youtubePlayerFragment?.onHidden()
                    spotifyPlayerFragment?.onHidden()
                }
                else -> return
            }
        }
    }

    // endregion

    // region YoutubePlayer

    val playerMaxVerticalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { (dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() }
    val minimumPlayerHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(minimumPlayerHeightDp.toFloat()).toInt() }

    private var currentSlideOffset: Float = 0.0f

    fun onVideoPlaylistEnded() {
        sliding_layout?.hideIfVisible()
    }

    fun loadVideo(video: Video) {
        spotifyPlayerFragment?.stopPlayback()
        viewModel.viewState.playerState.set(PlayerState.VIDEO)
        youtubePlayerFragment?.loadVideo(video)
        sliding_layout?.setDragView(youtubePlayerFragment?.view)
        maximizePlayer()
        viewModel.searchRelatedVideos(video)
    }

    fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (videos.isEmpty()) {
            Toast.makeText(this, "Playlist is empty.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.viewState.playerState.set(PlayerState.VIDEO_PLAYLIST)
        spotifyPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.loadVideoPlaylist(videoPlaylist, videos)
        sliding_layout?.setDragView(youtubePlayerFragment?.view)
        maximizePlayer()
        viewModel.searchRelatedVideos(videos.first())
    }


    fun maximizePlayer() {
        sliding_layout?.expandIfHidden()
    }

    private val youtubePlayerMaxHorizontalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(screenHeight.toFloat()).toInt() }


    private fun updatePlayersDimensions(slideOffset: Float) {
        if (sliding_layout.panelState != SlidingUpPanelLayout.PanelState.HIDDEN && slideOffset >= 0) {
            currentSlideOffset = slideOffset

            val youtubePlayerLayoutParams = youtube_player_fragment?.view?.layoutParams
            val spotifyPlayerLayoutParams = spotify_player_fragment?.view?.layoutParams
            val youtubePlayerHeight = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                (playerMaxVerticalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            } else {
                (youtubePlayerMaxHorizontalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            }
            val spotifyPlayerHeight = ((dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() - minimumPlayerHeight) * slideOffset + minimumPlayerHeight

            youtubePlayerLayoutParams?.height = youtubePlayerHeight.toInt()
            spotifyPlayerLayoutParams?.height = spotifyPlayerHeight.toInt()

            youtubePlayerFragment?.onPlayerDimensionsChange(slideOffset)

            youtube_player_fragment?.view?.requestLayout()
            spotify_player_fragment?.view?.requestLayout()
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

    private val onFavouriteBtnClickListener = View.OnClickListener { _ ->
        viewModel.viewState.playerState.get()?.let { playerState ->
            when (playerState) {
                PlayerState.VIDEO -> youtubePlayerFragment?.onFavouriteBtnClick()
                PlayerState.PLAYLIST -> togglePlaylistFavouriteState()
                PlayerState.TRACK -> toggleTrackFavouriteState()
                PlayerState.ALBUM -> toggleAlbumFavouriteState()
                else -> return@OnClickListener
            }
        }
    }

    fun addVideoToPlaylist(playlist: VideoPlaylist) {
        youtubePlayerFragment?.addVideoToPlaylist(playlist)
    }

    fun showNewPlaylistDialog() {
        youtubePlayerFragment?.showNewPlaylistDialog()
    }

    // endregion

    private fun togglePlaylistFavouriteState() {
        spotifyPlayerFragment?.lastPlayedPlaylist?.let {
            viewModel.togglePlaylistFavouriteState(
                    it,
                    { Toast.makeText(this, "${it.name} added to favourite playlists.", Toast.LENGTH_SHORT).show() },
                    { Toast.makeText(this, "${it.name} deleted from favourite playlists.", Toast.LENGTH_SHORT).show() }
            )
        }
    }

    private fun toggleTrackFavouriteState() {
        spotifyPlayerFragment?.lastPlayedTrack?.let {
            viewModel.toggleTrackFavouriteState(
                    it,
                    { Toast.makeText(this, "${it.name} added to favourite tracks.", Toast.LENGTH_SHORT).show() },
                    { Toast.makeText(this, "${it.name} deleted from favourite tracks.", Toast.LENGTH_SHORT).show() }
            )
        }
    }

    private fun toggleAlbumFavouriteState() {
        spotifyPlayerFragment?.lastPlayedAlbum?.let {
            viewModel.toggleAlbumFavouriteState(
                    it,
                    { Toast.makeText(this, "${it.name} added to favourite albums.", Toast.LENGTH_SHORT).show() },
                    { Toast.makeText(this, "${it.name} deleted from favourite albums.", Toast.LENGTH_SHORT).show() }
            )
        }
    }

    companion object {
        private const val minimumPlayerHeightDp = 120

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
    }
}

