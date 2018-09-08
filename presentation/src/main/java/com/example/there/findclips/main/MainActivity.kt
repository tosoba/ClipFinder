package com.example.there.findclips.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.there.findclips.R
import com.example.there.findclips.base.activity.BaseVMActivity
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.fragment.addvideo.AddVideoDialogFragment
import com.example.there.findclips.fragment.addvideo.AddVideoViewState
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.checkItem
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.screenHeight
import com.example.there.findclips.util.ext.screenOrientation
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.list.OnVideoClickListener
import com.example.there.findclips.view.list.RelatedVideosList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseVMActivity<MainViewModel>(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view
        binding.relatedVideosRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setSupportActionBar(main_toolbar)

        initYouTubePlayerView()
        addPlayerViewControls()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updatePlayerDimensions(currentSlideOffset)
        updateMainContentLayoutParams()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleSearchIntent(intent)
    }

    private fun handleSearchIntent(intent: Intent?) {
//        fun saveQuery(query: String) {
//            val suggestions = SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
//            suggestions.saveRecentQuery(query, null)
//        }
//
//        if (Intent.ACTION_SEARCH == intent?.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            saveQuery(query)
//
//            val currentFragment = pagerAdapter.currentFragment
//            currentFragment?.let { (it as? SearchFragment)?.search(query) }
//        }
    }

    override fun onBackPressed() {
        val currentFragment = pagerAdapter.currentFragment
        if (currentFragment != null && currentFragment.childFragmentManager.backStackEntryCount > 0) {
            currentFragment.childFragmentManager.popBackStackImmediate()
//            updateToolbarTitle(currentFragment)
//            updateToolbarBackNavigation(currentFragment)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addVideoDialogFragment = null
    }

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
                onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    // region pager navigation

    private val itemIds: Array<Int> = arrayOf(R.id.action_dashboard, R.id.action_favorites, R.id.action_search)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == main_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        main_view_pager.currentItem = itemIds.indexOf(item.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy { MainFragmentPagerAdapter(supportFragmentManager) }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
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
        lifecycle.addObserver(player_view)
        player_view.initialize({ this.youTubePlayer = it }, true)
        player_view.playerUIController.showFullscreenButton(false)
    }

    private var lastPlayedVideo: Video? = null

    fun loadVideo(video: Video) {
        if (video == lastPlayedVideo) return
        lastPlayedVideo = video

        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN)
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        if (lifecycle.currentState == Lifecycle.State.RESUMED)
            youTubePlayer?.loadVideo(video.id, 0f)
        else
            youTubePlayer?.cueVideo(video.id, 0f)

        viewModel.searchRelatedVideos(video)
    }

    private fun updatePlayerDimensions(slideOffset: Float) {
        if (sliding_layout.panelState != SlidingUpPanelLayout.PanelState.HIDDEN && slideOffset >= 0) {
            currentSlideOffset = slideOffset
            val layoutParams = player_view.layoutParams
            val height = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                (playerMaxVerticalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            } else {
                (playerMaxHorizontalHeight - minimumPlayerHeight) * slideOffset + minimumPlayerHeight
            }
            layoutParams.height = height.toInt()
            player_view.requestLayout()
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
                setMargins(5, 5, 5, 5)
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
                setMargins(5, 5, 5, 5)
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
        RelatedVideosList.Adapter(viewModel.viewState.videos, R.layout.related_video_item, onVideoItemClickListener)
    }

    private val onVideoItemClickListener = object : OnVideoClickListener {
        override fun onClick(item: Video) {
            lastPlayedVideo = item
            loadVideo(item)
        }
    }

    private val onRelatedVideosScrollListener: RecyclerView.OnScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() = viewModel.searchRelatedVideosWithToLastId()
        }
    }

    private val relatedVideosItemDecoration: RecyclerView.ItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SeparatorDecoration(this, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
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
    }
}
