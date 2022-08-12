package com.clipfinder.main

import android.Manifest
import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.mvrx.BaseMvRxActivity
import com.airbnb.mvrx.viewModel
import com.clipfinder.core.android.base.fragment.*
import com.clipfinder.core.android.base.handler.*
import com.clipfinder.core.android.base.provider.IntentProvider
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack
import com.clipfinder.core.android.model.videos.Video
import com.clipfinder.core.android.model.videos.VideoPlaylist
import com.clipfinder.core.android.spotify.auth.SpotifyManualAuth
import com.clipfinder.core.android.spotify.base.SpotifyAuthController
import com.clipfinder.core.android.spotify.base.SpotifyPlayerController
import com.clipfinder.core.android.spotify.base.SpotifyTrackChangeHandler
import com.clipfinder.core.android.spotify.fragment.ISpotifyPlayerFragment
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.*
import com.clipfinder.core.android.view.OnNavigationDrawerClosedListener
import com.clipfinder.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.clipfinder.core.model.WithValue
import com.clipfinder.core.notification.PlaybackNotification
import com.clipfinder.main.databinding.ActivityMainBinding
import com.clipfinder.main.databinding.DrawerHeaderBinding
import com.clipfinder.main.soundcloud.SoundCloudMainFragment
import com.clipfinder.main.spotify.SpotifyMainFragment
import com.clipfinder.settings.SettingsActivity
import com.clipfinder.spotify.track.SpotifyTrackFragment
import com.google.android.material.navigation.NavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity :
    BaseMvRxActivity(),
    SlidingPanelController,
    SpotifyPlayerController,
    SoundCloudPlayerController,
    YoutubePlayerController,
    SpotifyTrackChangeHandler,
    BackPressedController,
    SpotifyAuthController,
    NavigationDrawerController,
    ToolbarController,
    IntentProvider {
    private val viewModel: MainViewModel by viewModel()
    private val fragmentFactory: ISpotifyFragmentsFactory by inject()

    override val providedIntent: Intent
        get() = Intent(this, MainActivity::class.java)

    override val slidingPanel: SlidingUpPanelLayout?
        get() = sliding_layout

    private val spotifyMainFragment: SpotifyMainFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? SpotifyMainFragment

    private val soundCloudMainFragment: SoundCloudMainFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? SoundCloudMainFragment

    private val mainContentFragment: IMainContentFragment?
        get() = mainContentViewPagerAdapter.currentFragment as? IMainContentFragment

    private val youtubePlayerFragment: IYoutubePlayerFragment?
        get() =
            supportFragmentManager.findFragmentById(R.id.youtube_player_fragment)
                as? IYoutubePlayerFragment

    private val spotifyPlayerFragment: ISpotifyPlayerFragment?
        get() =
            supportFragmentManager.findFragmentById(R.id.spotify_player_fragment)
                as? ISpotifyPlayerFragment

    private val soundCloudPlayerFragment: ISoundCloudPlayerFragment?
        get() =
            supportFragmentManager.findFragmentById(R.id.sound_cloud_player_fragment)
                as? ISoundCloudPlayerFragment

    private val spotifyTrackFragment: SpotifyTrackFragment?
        get() =
            supportFragmentManager.findFragmentById(R.id.spotify_track_fragment)
                as? SpotifyTrackFragment

    private val relatedVideosFragment: ISearchFragment?
        get() =
            supportFragmentManager.findFragmentById(R.id.related_videos_fragment)
                as? ISearchFragment

    private val view: MainView by
        lazy(LazyThreadSafetyMode.NONE) {
            MainView(
                onDrawerNavigationItemSelectedListener = onDrawerNavigationItemSelectedListener,
                fadeOnClickListener = fadeOnClickListener,
                slideListener = slideListener,
                initialSlidePanelState = SlidingUpPanelLayout.PanelState.HIDDEN,
                onFavouriteBtnClickListener = {},
                pagerAdapter = mainContentViewPagerAdapter,
                offScreenPageLimit = mainContentViewPagerAdapter.count - 1
            )
        }

    private var searchViewMenuItem: MenuItem? = null

    private val mainContentViewPagerAdapter: CustomCurrentStatePagerAdapter by
        lazy(LazyThreadSafetyMode.NONE) {
            CustomCurrentStatePagerAdapter(
                supportFragmentManager,
                arrayOf(SpotifyMainFragment(), SoundCloudMainFragment())
            )
        }

    private val binding: ActivityMainBinding by
        lazy(LazyThreadSafetyMode.NONE) {
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        }

    private val drawerHeaderBinding: DrawerHeaderBinding by
        lazy(LazyThreadSafetyMode.NONE) {
            DrawerHeaderBinding.inflate(
                LayoutInflater.from(this),
                binding.drawerNavigationView,
                false
            )
        }

    private val loginDrawerClosedListener: OnNavigationDrawerClosedListener by
        lazy(LazyThreadSafetyMode.NONE) {
            object : OnNavigationDrawerClosedListener {
                override fun onDrawerClosed(drawerView: View) {
                    if (!isPlayerLoggedIn) startLoginActivity()
                    main_drawer_layout?.removeDrawerListener(loginDrawerClosedListener)
                }
            }
        }

    private val logoutDrawerClosedListener: OnNavigationDrawerClosedListener by
        lazy(LazyThreadSafetyMode.NONE) {
            object : OnNavigationDrawerClosedListener {
                override fun onDrawerClosed(drawerView: View) {
                    if (isPlayerLoggedIn) {
                        viewModel.onLoggedOut()
                        logOutPlayer()
                    }
                    main_drawer_layout?.removeDrawerListener(logoutDrawerClosedListener)
                }
            }
        }

    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.drawer_action_show_spotify_main -> {
                    spotifyMainFragment?.let {
                        return@OnNavigationItemSelectedListener true
                    }
                        ?: run { main_content_view_pager?.currentItem = 0 }

                    viewModel.setMainContent(MainContent.SPOTIFY)

                    if (binding.drawerNavigationView.headerCount == 0) {
                        binding.drawerNavigationView.addHeaderView(drawerHeaderBinding.root)
                    }

                    binding.drawerNavigationView.menu.clear()
                    binding.drawerNavigationView.inflateMenu(R.menu.spotify_drawer_menu)

                    binding.drawerNavigationView.menu.apply {
                        val isLoggedIn = viewModel.isPrivateAuthorized
                        findItem(R.id.drawer_action_login)?.isVisible = !isLoggedIn
                        findItem(R.id.drawer_action_logout)?.isVisible = isLoggedIn
                    }
                }
                R.id.drawer_action_show_soundcloud_main -> {
                    soundCloudMainFragment?.let {
                        return@OnNavigationItemSelectedListener true
                    }
                        ?: run { main_content_view_pager?.currentItem = 1 }

                    viewModel.setMainContent(MainContent.SOUNDCLOUD)

                    if (binding.drawerNavigationView.headerCount > 0) {
                        binding.drawerNavigationView.removeHeaderView(drawerHeaderBinding.root)
                    }

                    binding.drawerNavigationView.menu.clear()
                    binding.drawerNavigationView.inflateMenu(R.menu.sound_cloud_drawer_menu)
                }
                R.id.drawer_action_about -> {
                    // TODO: AboutActivity or (probably better) AboutFragment in the same container
                    // as Spotify and SoundCloud main fragments
                }
                R.id.drawer_action_settings ->
                    Intent(this, SettingsActivity::class.java).run { startActivity(this) }
                R.id.drawer_action_login ->
                    main_drawer_layout?.addDrawerListener(loginDrawerClosedListener)
                R.id.drawer_action_logout ->
                    main_drawer_layout?.addDrawerListener(logoutDrawerClosedListener)
            }

            item.isChecked = false
            main_drawer_layout?.closeDrawer(Gravity.LEFT)
            true
        }

    private val fadeOnClickListener: View.OnClickListener =
        View.OnClickListener {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

    private val slideListener =
        object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) =
                updatePlayersDimensions(slideOffset)

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                if (previousState == newState) return
                when (newState) {
                    SlidingUpPanelLayout.PanelState.DRAGGING -> {
                        youtubePlayerFragment?.onDragging()
                        if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            spotifyPlayerFragment?.onDragging()
                        }
                    }
                    SlidingUpPanelLayout.PanelState.EXPANDED -> {
                        youtubePlayerFragment?.onExpanded()
                        spotifyPlayerFragment?.onExpanded()
                    }
                    SlidingUpPanelLayout.PanelState.COLLAPSED ->
                        youtubePlayerFragment?.onCollapsed()
                    SlidingUpPanelLayout.PanelState.HIDDEN -> {
                        youtubePlayerFragment?.onHidden()
                        spotifyPlayerFragment?.onHidden()
                        soundCloudPlayerFragment?.onHidden()
                    }
                    else -> {}
                }
            }
        }

    private var currentSlideOffset: Float = 0.0f

    private val playerMaxVerticalHeight: Int by
        lazy(LazyThreadSafetyMode.NONE) { (dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() }
    private val minimumPlayerHeight: Int by
        lazy(LazyThreadSafetyMode.NONE) { dpToPx(minimumPlayerHeightDp.toFloat()).toInt() }

    private val youtubePlayerMaxHorizontalHeight: Int by
        lazy(LazyThreadSafetyMode.NONE) { dpToPx(screenHeight.toFloat()).toInt() }

    private val spotifyAuth: SpotifyManualAuth by inject()
    override val isPlayerLoggedIn: Boolean
        get() = spotifyPlayerFragment?.isPlayerLoggedIn == true
    override var onLoginSuccessful: (() -> Unit)?
        get() = viewModel.onLoginSuccessful
        set(value) {
            viewModel.onLoginSuccessful = value
        }

    private lateinit var deleteNotificationReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBindings()
        observeLoggedIn()
        observePrivateAuthorization()
        deleteNotificationReceiver =
            createAndRegisterReceiverFor(
                IntentFilter(PlaybackNotification.ACTION_DELETE_NOTIFICATION)
            ) { _, _ -> sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.HIDDEN }
    }

    override fun onDestroy() {
        unregisterReceiver(deleteNotificationReceiver)
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_SEARCH -> handleSearchIntent(intent)
        }
    }

    override fun onBackPressed() {
        mainContentFragment?.let {
            if (sliding_layout?.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                sliding_layout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                return
            }

            val currentNavHost = it.currentNavHostFragment
            if (currentNavHost != null &&
                    currentNavHost.childFragmentManager.backStackEntryCount > 0
            ) {
                when (val topFragment = currentNavHost.topFragment) {
                    is BackPressedHandler -> {
                        topFragment.onBackPressed()
                        return
                    }
                }

                showMainToolbarOnBackPressed(currentNavHost)
                currentNavHost.childFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        }
            ?: run { super.onBackPressed() }
    }

    override fun onBackPressedWithNoPreviousState() {
        mainContentFragment?.let {
            val currentFragment = it.currentFragment
            if (currentFragment != null &&
                    currentFragment.childFragmentManager.backStackEntryCount > 0
            ) {
                showMainToolbarOnBackPressed(currentFragment)
                currentFragment.childFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                fun onError(msg: String) {
                    Timber.e(msg)
                    Toast.makeText(this, R.string.failed_to_login, Toast.LENGTH_LONG).show()
                }
                data?.let { intent ->
                    viewModel.onLoginActivityResult(intent) {
                        onError(it.message ?: "Unknown error.")
                    }
                }
                    ?: run { onError(getString(R.string.failed_to_login)) }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updatePlayersDimensions(currentSlideOffset)
        main_content_view_pager?.updateLayoutParams()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.search_view_menu_item -> true
            else -> super.onOptionsItemSelected(item)
        }

    override fun openDrawer() {
        main_drawer_layout?.openDrawer(GravityCompat.START)
    }

    override fun toggleToolbar() {
        spotifyMainFragment?.let {
            val currentTopFragment = it.currentNavHostFragment?.topFragment
            val mainToolbar = (currentTopFragment as? HasMainToolbar)?.toolbar
            setSupportActionBar(mainToolbar)
            if (currentTopFragment?.childFragmentManager?.backStackEntryCount == 0) {
                showDrawerHamburger()
            }
        }
    }

    private fun checkPlaybackPermission(onGranted: () -> Unit) {
        RxPermissions(this)
            .request(Manifest.permission.RECORD_AUDIO)
            .subscribe(
                { granted ->
                    if (granted) {
                        onGranted()
                    } else {
                        MaterialDialog.Builder(this)
                            .title(R.string.permission_required)
                            .content(R.string.playback_requires_permission)
                            .positiveText(R.string.retry)
                            .negativeText(R.string.cancel)
                            .onPositive { _, _ -> checkPlaybackPermission(onGranted) }
                            .build()
                            .apply(MaterialDialog::show)
                    }
                },
                Timber::e
            )
            .disposeOnDestroy(this)
    }

    private fun observePrivateAuthorization() {
        viewModel.selectSubscribe(this, MainState::isPrivateAuthorized) { isPrivateAuthorized ->
            if (!isPrivateAuthorized) return@selectSubscribe
            val privateAccessToken = viewModel.privateAccessToken ?: return@selectSubscribe
            spotifyPlayerFragment?.initializePlayer(privateAccessToken) {
                onLoginSuccessful?.invoke()
                onLoginSuccessful = null
            }
        }
    }

    override fun loadTrack(track: SoundCloudTrack) {
        // TODO: use databinding to make play button invisible in SoundCloudTrackVideosFragment if
        // track's streamUrl == null
        if (track.streamUrl == null) {
            Toast.makeText(this, "Track is not streamable.", Toast.LENGTH_SHORT).show()
            return
        }

        sliding_layout?.setDragView(soundCloudPlayerFragment?.playerView)
        showCollapsedIfHidden()
        spotifyPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.stopPlayback()
        soundCloudPlayerFragment?.loadTrack(track)
        viewModel.setPlayerState(PlayerState.SOUND_CLOUD_TRACK)
    }

    override fun loadTrack(track: Track) {
        checkPlaybackPermission {
            setupSpotifyPlayback()
            spotifyPlayerFragment?.loadTrack(track)
            viewModel.setPlayerState(PlayerState.TRACK)
        }
    }

    override fun loadAlbum(album: Album) {
        checkPlaybackPermission {
            setupSpotifyPlayback()
            spotifyPlayerFragment?.loadAlbum(album)
            viewModel.setPlayerState(PlayerState.ALBUM)
        }
    }

    override fun loadPlaylist(playlist: Playlist) {
        checkPlaybackPermission {
            setupSpotifyPlayback()
            spotifyPlayerFragment?.loadPlaylist(playlist)
            viewModel.setPlayerState(PlayerState.PLAYLIST)
        }
    }

    private fun setupSpotifyPlayback() {
        sliding_layout?.setDragView(spotifyPlayerFragment?.playerView)
        showCollapsedIfHidden()
        soundCloudPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.stopPlayback()
    }

    override fun onTrackChanged(trackId: String) {
        spotifyTrackFragment?.onNewTrack(trackId)
    }

    override fun loadVideo(video: Video) {
        spotifyPlayerFragment?.stopPlayback()
        soundCloudPlayerFragment?.stopPlayback()
        viewModel.setPlayerState(PlayerState.VIDEO)
        youtubePlayerFragment?.loadVideo(video)
        sliding_layout?.setDragView(youtubePlayerFragment?.playerView)
        expandIfHidden()
        relatedVideosFragment?.search(video.id)
    }

    override fun loadVideoPlaylist(videoPlaylist: VideoPlaylist, videos: List<Video>) {
        if (videos.isEmpty()) {
            Toast.makeText(this, "Playlist is empty.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.setPlayerState(PlayerState.VIDEO_PLAYLIST)
        spotifyPlayerFragment?.stopPlayback()
        soundCloudPlayerFragment?.stopPlayback()
        youtubePlayerFragment?.loadVideoPlaylist(videoPlaylist, videos)
        sliding_layout?.setDragView(youtubePlayerFragment?.playerView)
        expandIfHidden()
        relatedVideosFragment?.search(videos.first().id)
    }

    override fun showLoginDialog() {
        if (isPlayerLoggedIn) return
        MaterialDialog.Builder(this)
            .title(R.string.spotify_login)
            .content(R.string.playback_requires_login)
            .positiveText(R.string.login)
            .negativeText(R.string.cancel)
            .onPositive { _, _ -> startLoginActivity() }
            .build()
            .apply(MaterialDialog::show)
    }

    private fun startLoginActivity() {
        startActivityForResult(spotifyAuth.authRequestIntent, LOGIN_REQUEST_CODE)
    }

    private fun logOutPlayer() {
        spotifyPlayerFragment?.logOutPlayer()
    }

    private fun observeLoggedIn() {
        viewModel.selectSubscribe(this, MainState::isPrivateAuthorized) { isPrivateAuthorized ->
            with(binding.drawerNavigationView.menu) {
                findItem(R.id.drawer_action_login)?.isVisible = !isPrivateAuthorized
                findItem(R.id.drawer_action_logout)?.isVisible = isPrivateAuthorized
            }
        }
    }

    private fun initViewBindings() {
        binding.mainActivityView = view
        viewModel.selectSubscribe(this, MainState::itemFavouriteState) {
            binding.addToFavouritesFab.hideAndShow()
        }
        viewModel.selectSubscribe(this, MainState::user) { user ->
            drawerHeaderBinding.user = if (user is WithValue) user.value else null
        }
        viewModel.selectSubscribe(this, MainState::playerState) { playerState ->
            binding.playerState = playerState
        }
        viewModel.selectSubscribe(this, MainState::mainContent) { mainContent ->
            binding.mainContent = mainContent
        }
        viewModel.selectSubscribe(this, MainState::itemFavouriteState) { itemFavouriteState ->
            binding.itemFavouriteState = itemFavouriteState
        }
        binding.drawerNavigationView.addHeaderView(drawerHeaderBinding.root)
        binding.drawerNavigationView.menu.findItem(R.id.drawer_action_logout)?.isVisible = false
    }

    private fun handleSearchIntent(intent: Intent) {
        val query = intent.getStringExtra(SearchManager.QUERY)
        if (query.isNullOrBlank()) return

        SearchRecentSuggestions(
                this,
                SearchSuggestionProvider.AUTHORITY,
                SearchSuggestionProvider.MODE
            )
            .run { saveRecentQuery(query, null) }

        searchViewMenuItem?.collapseActionView()

        spotifyMainFragment?.let {
            val currentFragment = it.currentNavHostFragment
            currentFragment?.showFragment(fragmentFactory.newSpotifySearchMainFragment(query), true)
        }

        // TODO: handle soundcloud search here
    }

    private fun showMainToolbarOnBackPressed(currentFragment: Fragment) {
        if (currentFragment.childFragmentManager.backStackEntryCount != 1) return
        val mainToolbar = (currentFragment as? HasMainToolbar)?.toolbar
        setSupportActionBar(mainToolbar)
        showDrawerHamburger()
    }

    private fun updatePlayersDimensions(slideOffset: Float) {
        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN || slideOffset < 0)
            return
        currentSlideOffset = slideOffset

        val youtubePlayerLayoutParams = youtube_player_fragment?.view?.layoutParams
        val spotifyPlayerLayoutParams = spotify_player_fragment?.view?.layoutParams
        val youtubePlayerHeight =
            if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                (playerMaxVerticalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            } else {
                (youtubePlayerMaxHorizontalHeight - minimumPlayerHeight) * slideOffset +
                    minimumPlayerHeight
            }
        val spotifyPlayerHeight =
            ((dpToPx(screenHeight.toFloat()) / 5 * 2).toInt() - minimumPlayerHeight) * slideOffset +
                minimumPlayerHeight

        youtubePlayerLayoutParams?.height = youtubePlayerHeight.toInt()
        spotifyPlayerLayoutParams?.height = spotifyPlayerHeight.toInt()

        youtubePlayerFragment?.onPlayerDimensionsChange(slideOffset)

        youtube_player_fragment?.view?.requestLayout()
        spotify_player_fragment?.view?.requestLayout()
    }

    private fun updateFavouriteBtnOnConfigChange(newConfig: Configuration) {
        add_to_favourites_fab
            ?.animate()
            ?.alpha(if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 0f else 1f)
    }

    companion object {
        private const val minimumPlayerHeightDp = 120
        private const val LOGIN_REQUEST_CODE = 100
    }
}
