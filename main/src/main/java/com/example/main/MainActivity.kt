package com.example.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.activity.BaseVMActivity
import com.example.coreandroid.base.activity.IntentProvider
import com.example.coreandroid.base.fragment.*
import com.example.coreandroid.base.handler.*
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.model.videos.Video
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.OnNavigationDrawerClosedListerner
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.itemlist.spotify.SpotifyTracksFragment
import com.example.main.databinding.ActivityMainBinding
import com.example.main.databinding.DrawerHeaderBinding
import com.example.main.soundcloud.SoundCloudMainFragment
import com.example.main.spotify.SpotifyMainFragment
import com.example.settings.SettingsActivity
import com.example.spotifyapi.SpotifyAuth
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.youtubeaddvideo.AddVideoDialogFragment
import com.example.youtubeaddvideo.AddVideoViewState
import com.google.android.material.navigation.NavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.ConnectionStateCallback
import com.spotify.sdk.android.player.Error
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity :
        BaseVMActivity<MainViewModel>(MainViewModel::class),
        ConnectionStateCallback,
        SlidingPanelController,
        VideoPlaylistController,
        SpotifyPlayerController,
        SoundCloudPlayerController,
        YoutubePlayerController,
        SpotifyTrackChangeHandler,
        BackPressedWithNoPreviousStateController,
        SpotifyLoginController,
        ConnectivitySnackbarHost,
        NavigationDrawerController,
        ToolbarController,
        IntentProvider {

    private val fragmentFactory: IFragmentFactory by inject()

    override val slidingPanel: SlidingUpPanelLayout?
        get() = sliding_layout

    private val spotifyMainFragment: SpotifyMainFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? SpotifyMainFragment

    private val soundCloudMainFragment: SoundCloudMainFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? SoundCloudMainFragment

    private val mainContentFragment: IMainContentFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? IMainContentFragment

    private val youtubePlayerFragment: IYoutubePlayerFragment?
        get() = supportFragmentManager.findFragmentById(R.id.youtube_player_fragment)
                as? IYoutubePlayerFragment

    private val spotifyPlayerFragment: ISpotifyPlayerFragment?
        get() = supportFragmentManager.findFragmentById(R.id.spotify_player_fragment)
                as? ISpotifyPlayerFragment

    private val soundCloudPlayerFragment: ISoundCloudPlayerFragment?
        get() = supportFragmentManager.findFragmentById(R.id.sound_cloud_player_fragment)
                as? ISoundCloudPlayerFragment

    private val similarTracksFragment: SpotifyTracksFragment?
        get() = supportFragmentManager.findFragmentById(R.id.similar_tracks_fragment)
                as? SpotifyTracksFragment

    private val relatedVideosFragment: IRelatedVideosSearchFragment?
        get() = supportFragmentManager.findFragmentById(R.id.related_videos_fragment)
                as? IRelatedVideosSearchFragment

    override val snackbarParentView: View?
        get() = when {
            spotifyMainFragment != null -> findViewById(R.id.spotify_play_fab)
            soundCloudMainFragment != null -> findViewById(R.id.sound_cloud_play_fab)
            else -> null
        }

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

    private val appPreferences: SpotifyPreferences by inject()

    private var searchViewMenuItem: MenuItem? = null

    private val mainContentFragments: Array<Fragment> by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf<Fragment>(SpotifyMainFragment(), SoundCloudMainFragment())
    }

    private val mainContentViewPagerAdapter: CustomCurrentStatePagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CustomCurrentStatePagerAdapter(supportFragmentManager, mainContentFragments)
    }

    private var addVideoDialogFragment: AddVideoDialogFragment? = null

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val drawerHeaderBinding: DrawerHeaderBinding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.inflate<DrawerHeaderBinding>(LayoutInflater.from(this), R.layout.drawer_header, binding.drawerNavigationView, false)
    }

    private val loginDrawerClosedListener: OnNavigationDrawerClosedListerner by lazy(LazyThreadSafetyMode.NONE) {
        object : OnNavigationDrawerClosedListerner {
            override fun onDrawerClosed(drawerView: View) {
                if (!isPlayerLoggedIn) openLoginWindow()
                main_drawer_layout?.removeDrawerListener(loginDrawerClosedListener)
            }
        }
    }

    private val logoutDrawerClosedListener: OnNavigationDrawerClosedListerner by lazy(LazyThreadSafetyMode.NONE) {
        object : OnNavigationDrawerClosedListerner {
            override fun onDrawerClosed(drawerView: View) {
                if (isPlayerLoggedIn) logOutPlayer()
                main_drawer_layout?.removeDrawerListener(logoutDrawerClosedListener)
            }
        }
    }

    private val onDrawerNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.drawer_action_show_spotify_main -> {
                spotifyMainFragment?.let {
                    return@OnNavigationItemSelectedListener true
                } ?: run { main_content_view_pager?.currentItem = 0 }

                viewModel.viewState.mainContent.set(MainContent.SPOTIFY)

                if (binding.drawerNavigationView.headerCount == 0) {
                    binding.drawerNavigationView.addHeaderView(drawerHeaderBinding.root)
                }

                binding.drawerNavigationView.menu.clear()
                binding.drawerNavigationView.inflateMenu(R.menu.spotify_drawer_menu)

                val isLoggedIn = viewModel.viewState.isLoggedIn.get() ?: false
                binding.drawerNavigationView.menu?.apply {
                    findItem(R.id.drawer_action_login)?.isVisible = !isLoggedIn
                    findItem(R.id.drawer_action_logout)?.isVisible = isLoggedIn
                }
            }

            R.id.drawer_action_show_soundcloud_main -> {
                soundCloudMainFragment?.let {
                    return@OnNavigationItemSelectedListener true
                } ?: run { main_content_view_pager?.currentItem = 1 }

                viewModel.viewState.mainContent.set(MainContent.SOUNDCLOUD)

                if (binding.drawerNavigationView.headerCount > 0) {
                    binding.drawerNavigationView.removeHeaderView(drawerHeaderBinding.root)
                }

                binding.drawerNavigationView.menu.clear()
                binding.drawerNavigationView.inflateMenu(R.menu.sound_cloud_drawer_menu)
            }

            R.id.drawer_action_about -> {
                //TODO: AboutActivity or (probably better) AboutFragment in the same container as Spotify and SoundCloud main fragments
            }

            R.id.drawer_action_settings -> Intent(this, SettingsActivity::class.java).run {
                startActivity(this)
            }

            R.id.drawer_action_remove_video_search_data -> {
                viewModel.clearAllVideoSearchData()
                Toast.makeText(this, "Video cache cleared", Toast.LENGTH_SHORT).show()
            }

            R.id.drawer_action_login -> main_drawer_layout?.addDrawerListener(loginDrawerClosedListener)

            R.id.drawer_action_logout -> main_drawer_layout?.addDrawerListener(logoutDrawerClosedListener)
        }

        it.isChecked = false
        main_drawer_layout?.closeDrawer(Gravity.LEFT)
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

        override fun onPanelStateChanged(panel: View?,
                                         previousState: SlidingUpPanelLayout.PanelState?,
                                         newState: SlidingUpPanelLayout.PanelState?) {
            if (previousState == newState) return
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
                    soundCloudPlayerFragment?.onHidden()
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
        override fun onPropertyChanged(observable: Observable, id: Int) {
            @Suppress("UNCHECKED_CAST") val o = observable as ObservableField<Boolean>
            binding.drawerNavigationView.menu.findItem(R.id.drawer_action_login)?.isVisible = !o.get()!!
            binding.drawerNavigationView.menu.findItem(R.id.drawer_action_logout)?.isVisible = o.get()!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clearAllVideoSearchData()

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
        (mainContentViewPagerAdapter.currentFragment as? IMainContentFragment)?.let {
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
        mainContentFragment?.let {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        searchViewMenuItem = menu?.findItem(R.id.search_view_menu_item)
        val searchView = searchViewMenuItem?.actionView as? SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.search_view_menu_item -> true
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

    override fun loadTrack(track: SoundCloudTrack) {
        //TODO: use databinding to make play button invisible in SoundCloudTrackVideosFragment if track's streamUrl == null
        if (track.streamUrl == null) {
            Toast.makeText(this, "Track is not streamable.", Toast.LENGTH_SHORT).show()
            return
        }

        sliding_layout?.setDragView(soundCloudPlayerFragment?.playerView)
        showCollapsedIfHidden()
        spotifyPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.stopPlayback()
        soundCloudPlayerFragment?.loadTrack(track)
        viewModel.viewState.playerState.set(PlayerState.SOUND_CLOUD_TRACK)
    }

    override fun loadTrack(track: Track) {
        if (viewModel.viewState.isLoggedIn.get() == false) {
            Toast.makeText(this, "You need to login to use spotify playback", Toast.LENGTH_SHORT).show()
            return
        }

        sliding_layout?.setDragView(spotifyPlayerFragment?.playerView)
        showCollapsedIfHidden()
        soundCloudPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadTrack(track)
        viewModel.viewState.playerState.set(PlayerState.TRACK)
    }

    override fun loadAlbum(album: Album) {
        if (viewModel.viewState.isLoggedIn.get() == false) {
            Toast.makeText(this, "You need to login to use spotify playback", Toast.LENGTH_SHORT).show()
            return
        }

        sliding_layout?.setDragView(spotifyPlayerFragment?.playerView)
        showCollapsedIfHidden()
        soundCloudPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadAlbum(album)
        viewModel.viewState.playerState.set(PlayerState.ALBUM)
    }

    override fun loadPlaylist(playlist: Playlist) {
        if (viewModel.viewState.isLoggedIn.get() == false) {
            Toast.makeText(this, "You need to login to use spotify playback", Toast.LENGTH_SHORT).show()
            return
        }

        sliding_layout?.setDragView(spotifyPlayerFragment?.playerView)
        showCollapsedIfHidden()
        soundCloudPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.stopPlayback()
        spotifyPlayerFragment?.loadPlaylist(playlist)
        viewModel.viewState.playerState.set(PlayerState.PLAYLIST)
    }

    override fun onTrackChanged(trackId: String) {
        viewModel.loadSimilarTracks(trackId)
    }

    override fun loadVideo(video: Video) {
        spotifyPlayerFragment?.stopPlayback()
        soundCloudPlayerFragment?.stopPlayback()
        viewModel.viewState.playerState.set(PlayerState.VIDEO)
        youtubePlayerFragment?.loadVideo(video)
        sliding_layout?.setDragView(youtubePlayerFragment?.playerView)
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
        soundCloudPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.loadVideoPlaylist(videoPlaylist, videos)
        sliding_layout?.setDragView(youtubePlayerFragment?.playerView)
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

    override val providedIntent: Intent
        get() = Intent(this, MainActivity::class.java)

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

    private fun setupNavigationFromSimilarTracks() {
        similarTracksFragment?.onItemClick = {
            sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            spotifyMainFragment?.currentNavHostFragment
                    ?.showFragment(fragmentFactory.newSpotifyTrackVideosFragment(it), true)
        }
    }

    private fun logOutPlayer() {
        spotifyPlayerFragment?.logOutPlayer()
    }

    private fun openLoginWindow() {
        val request = AuthenticationRequest.Builder(SpotifyAuth.id, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(SCOPES)
                .build()

        AuthenticationClient.openLoginActivity(this, LOGIN_REQUEST_CODE, request)
    }

    private fun addVideoToFavourites() {
        youtubePlayerFragment?.lastPlayedVideo?.let {
            viewModel.loadFavouriteVideoPlaylists()
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
            when ((observable as ObservableField<PlayerState>).get()!!) {
                PlayerState.TRACK -> spotifyPlayerFragment?.lastPlayedTrack?.let { viewModel.updateTrackFavouriteState(it) }
                PlayerState.PLAYLIST -> spotifyPlayerFragment?.lastPlayedPlaylist?.let { viewModel.updatePlaylistFavouriteState(it) }
                PlayerState.ALBUM -> spotifyPlayerFragment?.lastPlayedAlbum?.let { viewModel.updateAlbumFavouriteState(it) }
                else -> viewModel.viewState.itemFavouriteState.set(false)
            }
        })
    }

    private fun initViewBindings() {
        binding.mainActivityView = view
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.itemFavouriteState) { _, _ ->
            binding.addToFavouritesFab.hideAndShow()
        })

        drawerHeaderBinding.viewState = viewModel.drawerViewState
        binding.drawerNavigationView.addHeaderView(drawerHeaderBinding.root)
        binding.drawerNavigationView.menu.findItem(R.id.drawer_action_logout)?.isVisible = false
    }

    private fun onSpotifyAuthenticationComplete(accessToken: String) {
        appPreferences.userPrivateAccessToken = AccessTokenEntity(accessToken, System.currentTimeMillis())
        viewModel.loadCurrentUser()

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
            currentFragment?.showFragment(fragmentFactory.newSpotifySearchMainFragment(query), true)
        }

        //TODO: handle soundcloud search here
    }

    private fun showMainToolbarOnBackPressed(currentFragment: androidx.fragment.app.Fragment) {
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
