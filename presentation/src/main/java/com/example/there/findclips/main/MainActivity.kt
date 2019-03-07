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
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.SearchView
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.data.api.SpotifyClient
import com.example.there.data.preferences.AppPreferences
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.findclips.R
import com.example.there.findclips.base.activity.BaseVMActivity
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.databinding.DrawerHeaderBinding
import com.example.there.findclips.fragment.addvideo.AddVideoDialogFragment
import com.example.there.findclips.fragment.addvideo.AddVideoViewState
import com.example.there.findclips.fragment.list.SpotifyTracksFragment
import com.example.there.findclips.fragment.player.spotify.SpotifyPlayerFragment
import com.example.there.findclips.fragment.player.youtube.YoutubePlayerFragment
import com.example.there.findclips.fragment.relatedvideos.RelatedVideosFragment
import com.example.there.findclips.fragment.search.SearchFragment
import com.example.there.findclips.fragment.search.SearchSuggestionProvider
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.main.controller.*
import com.example.there.findclips.main.soundcloud.SoundCloudMainFragment
import com.example.there.findclips.main.spotify.SpotifyMainFragment
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.settings.SettingsActivity
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.viewpager.adapter.CustomCurrentStatePagerAdapter
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
        ConnectionStateCallback,
        SlidingPanelController,
        VideoPlaylistController,
        SpotifyPlayerController,
        YoutubePlayerController,
        SpotifyTrackChangeHandler,
        BackPressedWithNoPreviousStateHandler,
        SpotifyLoginController,
        ConnectivitySnackbarHost,
        NavigationDrawerController,
        ToolbarController {

    override val slidingPanel: SlidingUpPanelLayout?
        get() = sliding_layout

    private val spotifyMainFragment: SpotifyMainFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? SpotifyMainFragment

    private val soundCloudMainFragment: SoundCloudMainFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? SoundCloudMainFragment

    private val youtubePlayerFragment: YoutubePlayerFragment?
        get() = supportFragmentManager.findFragmentById(R.id.youtube_player_fragment)
                as? YoutubePlayerFragment

    private val spotifyPlayerFragment: SpotifyPlayerFragment?
        get() = supportFragmentManager.findFragmentById(R.id.spotify_player_fragment)
                as? SpotifyPlayerFragment

    private val similarTracksFragment: SpotifyTracksFragment?
        get() = supportFragmentManager.findFragmentById(R.id.similar_tracks_fragment)
                as? SpotifyTracksFragment

    private val relatedVideosFragment: RelatedVideosFragment?
        get() = supportFragmentManager.findFragmentById(R.id.related_videos_fragment)
                as? RelatedVideosFragment

    override val connectivitySnackbarParentView: View?
        get() = findViewById(R.id.main_view_pager)

    private val view: MainView by lazy {
        MainView(
                state = viewModel.viewState,
                onDrawerNavigationItemSelectedListener = onDrawerNavigationItemSelectedListener,
                fadeOnClickListener = fadeOnClickListener,
                slideListener = slideListener,
                initialSlidePanelState = SlidingUpPanelLayout.PanelState.HIDDEN,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener,
                pagerAdapter = mainContentViewPagerAdapter,
                offScreenPageLimit = 1 //TODO: change to 2 after adding AboutFragment to ViewPager
        )
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    private var searchViewMenuItem: MenuItem? = null

    private val mainContentFragments: Array<Fragment> by lazy {
        arrayOf(SpotifyMainFragment(), SoundCloudMainFragment())
    }

    private val mainContentViewPagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(supportFragmentManager, mainContentFragments)
    }

    private var addVideoDialogFragment: AddVideoDialogFragment? = null

    private val onDrawerNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.drawer_action_show_spotify_main -> {
                spotifyMainFragment?.let {
                    return@OnNavigationItemSelectedListener true
                } ?: run { main_content_view_pager?.currentItem = 0 }
            }

            R.id.drawer_action_show_soundcloud_main -> {
                soundCloudMainFragment?.let {
                    return@OnNavigationItemSelectedListener true
                } ?: run { main_content_view_pager?.currentItem = 1 }
            }

            R.id.drawer_action_about -> {
                //TODO: AboutActivity or (probably better) AboutFragment in the same container as Spotify and SoundCloud main fragments
            }

            R.id.drawer_action_settings -> Intent(this, SettingsActivity::class.java).run {
                startActivity(this)
            }

            R.id.drawer_action_remove_video_search_data -> {
                viewModel.deleteAllVideoSearchData()
                Toast.makeText(this, "Video cache cleared", Toast.LENGTH_SHORT).show()
            }

            R.id.drawer_action_login -> if (!isPlayerLoggedIn) openLoginWindow()

            R.id.drawer_action_logout -> if (isPlayerLoggedIn) logOutPlayer()
        }

        main_drawer_layout?.closeDrawer(Gravity.START)
        true
    }

    private val onFavouriteBtnClickListener = View.OnClickListener { _ ->
        viewModel.viewState.playerState.get()?.let { playerState ->
            when (playerState) {
                PlayerState.VIDEO -> addVideoToFavourites()
                PlayerState.PLAYLIST -> togglePlaylistFavouriteState()
                PlayerState.TRACK -> toggleTrackFavouriteState()
                PlayerState.ALBUM -> toggleAlbumFavouriteState()
                else -> return@OnClickListener
            }
        }
    }

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

    private val playerMaxVerticalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { (dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() }
    private val minimumPlayerHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(minimumPlayerHeightDp.toFloat()).toInt() }
    private val youtubePlayerMaxHorizontalHeight: Int by lazy(LazyThreadSafetyMode.NONE) { dpToPx(screenHeight.toFloat()).toInt() }

    private var currentSlideOffset: Float = 0.0f

    override val loggedInObservable: ObservableField<Boolean>
        get() = viewModel.viewState.isLoggedIn

    override val isPlayerLoggedIn: Boolean
        get() = spotifyPlayerFragment?.isPlayerLoggedIn == true

    override var onLoginSuccessful: (() -> Unit)? = null

    private val loggedInCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, id: Int) = invalidateOptionsMenu()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.deleteAllVideoSearchData()

        initViewBindings()
        setupNavigationFromSimilarTracks()

        addStatePropertyChangedCallbacks()
    }

    override fun onDestroy() {
        addVideoDialogFragment = null
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_SEARCH -> handleSearchIntent(intent)
        }
    }

    override fun onBackPressed() {
        spotifyMainFragment?.let {
            val currentFragment = it.currentNavHostFragment

            if (sliding_layout?.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                return
            }

            if (currentFragment != null && currentFragment.childFragmentManager.backStackEntryCount > 0) {
                currentFragment.topFragment?.let { topFragment ->
                    if (topFragment is GoesToPreviousStateOnBackPressed) {
                        topFragment.onBackPressed()
                        return
                    }
                }

                showMainToolbarOnBackPressed(currentFragment)
                currentFragment.childFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        } ?: run {
            super.onBackPressed()
        }
    }

    override fun onBackPressedWithNoPreviousState() {
        spotifyMainFragment?.let {
            val currentFragment = it.currentFragment
            if (currentFragment != null && currentFragment.childFragmentManager.backStackEntryCount > 0) {
                showMainToolbarOnBackPressed(currentFragment)
                currentFragment.childFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> onSpotifyAuthenticationComplete(response.accessToken)
                AuthenticationResponse.Type.ERROR -> Log.e("ERR", "Auth error: " + response.error)
                else -> Log.e("ERR", "Auth result: " + response.type)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updatePlayersDimensions(currentSlideOffset)
        updateMainContentLayoutParams()
        updateFavouriteBtnOnConfigChange(newConfig)
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
            if (!isPlayerLoggedIn) openLoginWindow()
            true
        }
        R.id.action_logout -> {
            if (isPlayerLoggedIn) logOutPlayer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.viewState.similarTracks.observe(this, Observer { tracks ->
            tracks?.let { similarTracksFragment?.resetItems(it) }
        })
    }

    override fun openDrawer() {
        main_drawer_layout?.openDrawer(GravityCompat.START)
    }

    override fun toggleToolbar() {
        spotifyMainFragment?.let {
            val currentTopFragment = it.currentNavHostFragment?.topFragment
            val mainToolbar = (currentTopFragment as? HasMainToolbar)?.toolbar
            setSupportActionBar(mainToolbar)
            if (currentTopFragment?.childFragmentManager?.backStackEntryCount == 0) showDrawerHamburger()
        }
    }

    override fun loadTrack(track: Track) {
        //TODO: check if player is actually in the middle of playing the track (maybe) same with album and playlist
        if (viewModel.viewState.isLoggedIn.get() == false) return //TODO: maybe show a toast with you need to be logged in msg
        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadTrack(track)
        viewModel.viewState.playerState.set(PlayerState.TRACK)
    }

    override fun loadAlbum(album: Album) {
        if (viewModel.viewState.isLoggedIn.get() == false) return
        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadAlbum(album)
        viewModel.viewState.playerState.set(PlayerState.ALBUM)
    }

    override fun loadPlaylist(playlist: Playlist) {
        if (viewModel.viewState.isLoggedIn.get() == false) return
        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadPlaylist(playlist)
        viewModel.viewState.playerState.set(PlayerState.PLAYLIST)
    }

    override fun onTrackChanged(trackId: String) {
        viewModel.getSimilarTracks(trackId)
        sliding_layout?.setDragView(spotifyPlayerFragment?.view)
    }

    override fun loadVideo(video: Video) {
        spotifyPlayerFragment?.stopPlayback()
        viewModel.viewState.playerState.set(PlayerState.VIDEO)
        youtubePlayerFragment?.loadVideo(video)
        sliding_layout?.setDragView(youtubePlayerFragment?.view)
        expandIfHidden()
        relatedVideosFragment?.searchRelatedVideos(video)
    }

    override fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (videos.isEmpty()) {
            Toast.makeText(this, "Playlist is empty.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.viewState.playerState.set(PlayerState.VIDEO_PLAYLIST)
        spotifyPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.loadVideoPlaylist(videoPlaylist, videos)
        sliding_layout?.setDragView(youtubePlayerFragment?.view)
        expandIfHidden()
        relatedVideosFragment?.searchRelatedVideos(videos.first())
    }

    override fun addVideoToPlaylist(playlist: VideoPlaylist) {
        youtubePlayerFragment?.lastPlayedVideo?.let {
            viewModel.addVideoToPlaylist(it, playlist) {
                Toast.makeText(this, "Video added to playlist: ${playlist.name}.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showNewPlaylistDialog() {
        youtubePlayerFragment?.lastPlayedVideo?.let {
            MaterialDialog.Builder(this)
                    .title(getString(R.string.new_playlist))
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(getString(R.string.playlist_name), "") { _, input ->
                        val newPlaylistName = input.trim().toString()
                        addVideoDialogFragment?.dismiss()
                        viewModel.addVideoPlaylistWithVideo(VideoPlaylist(name = newPlaylistName), video = it) {
                            Toast.makeText(
                                    this,
                                    "Video added to playlist: $newPlaylistName.",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.positiveText(getString(R.string.ok))
                    .build()
                    .apply { show() }
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

    private fun setupNavigationFromSimilarTracks() {
        similarTracksFragment?.onItemClick = {
            sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            spotifyMainFragment?.currentNavHostFragment
                    ?.showFragment(TrackVideosFragment.newInstance(it), true)
        }
    }

    override fun showLoginDialog() {
        if (!isPlayerLoggedIn) MaterialDialog.Builder(this)
                .title(R.string.spotify_login)
                .content(R.string.playback_requires_login)
                .positiveText(R.string.login)
                .negativeText(R.string.cancel)
                .onPositive { _, _ -> openLoginWindow() }
                .build()
                .apply { show() }
    }

    private fun logOutPlayer() {
        spotifyPlayerFragment?.logOutPlayer()
    }

    private fun openLoginWindow() {
        val request = AuthenticationRequest.Builder(SpotifyClient.id, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request)
    }

    private fun addVideoToFavourites() {
        youtubePlayerFragment?.lastPlayedVideo?.let {
            viewModel.getFavouriteVideoPlaylists()
            addVideoDialogFragment = AddVideoDialogFragment().apply {
                state = AddVideoViewState(viewModel.viewState.favouriteVideoPlaylists)
                show(childFragmentManager, TAG_ADD_VIDEO)
            }
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
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.itemFavouriteState) { _, _ ->
            binding.addToFavouritesFab.hideAndShow()
        })

        val drawerHeaderBinding: DrawerHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.drawer_header, binding.drawerNavigationView, false)
        drawerHeaderBinding.viewState = viewModel.drawerViewState
        binding.drawerNavigationView.addHeaderView(drawerHeaderBinding.root)
    }

    private fun onSpotifyAuthenticationComplete(accessToken: String) {
        appPreferences.userPrivateAccessToken = AccessTokenEntity(accessToken, System.currentTimeMillis())
        viewModel.getCurrentUser()

        spotifyPlayerFragment?.onAuthenticationComplete(accessToken)
    }

    private fun handleSearchIntent(intent: Intent) {
        val query = intent.getStringExtra(SearchManager.QUERY)
        if (query.isNullOrBlank()) return

        SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE).run {
            saveRecentQuery(query, null)
        }

        searchViewMenuItem?.collapseActionView()

        spotifyMainFragment?.let {
            val currentFragment = it.currentNavHostFragment
            currentFragment?.showFragment(SearchFragment.newInstance(query), true)
        }

        //TODO: handle soundcloud search here
    }

    private fun showMainToolbarOnBackPressed(currentFragment: Fragment) {
        if (currentFragment.childFragmentManager.backStackEntryCount == 1) {
            val mainToolbar = (currentFragment as? HasMainToolbar)?.toolbar
            setSupportActionBar(mainToolbar)
            showDrawerHamburger()
        }
    }

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

    private fun updateMainContentLayoutParams() = with(main_content_view_pager) {
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

        private const val TAG_ADD_VIDEO = "TAG_ADD_VIDEO"
    }
}
