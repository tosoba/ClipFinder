package com.example.there.findclips.trackvideos

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.databinding.ActivityTrackVideosBinding
import com.example.there.findclips.databinding.ActivityTrackVideosBindingLandImpl
import com.example.there.findclips.draggablefragment.DraggableListener
import com.example.there.findclips.draggablefragment.DraggablePanel
import com.example.there.findclips.entities.Track
import com.example.there.findclips.entities.Video
import com.example.there.findclips.search.videos.VideosSearchFragment
import com.example.there.findclips.trackdetails.TrackDetailsFragment
import com.example.there.findclips.util.*
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_track_videos.*


class TrackVideosActivity : AppCompatActivity(), VideoPlayerHost {

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            track_videos_tab_layout?.getTabAt(position)?.select()
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { track_videos_viewpager.currentItem = it.position }
        }
    }

    private val pagerAdapter: TrackVideosPagerAdapter by lazy {
        TrackVideosPagerAdapter(
                manager = supportFragmentManager,
                fragments = arrayOf(VideosSearchFragment.newInstanceWithQuery(track.query), TrackDetailsFragment()))
    }

    private val track: Track by lazy { intent.getParcelableExtra(EXTRA_TRACK) as Track }

    private val viewState: TrackVideosViewState = TrackVideosViewState()

    private val view: TrackVideosActivityView by lazy {
        TrackVideosActivityView(
                track = track,
                state = viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFromSavedState(savedInstanceState)
        initView()
        initToolbar()
    }

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            viewState.videoIsOpen.set(it.getBoolean(KEY_SAVED_VIEW_STATE))
            lastVideo = it.getParcelable(KEY_SAVED_LAST_VIDEO)
            lastSeekTime = it.getInt(KEY_SAVED_LAST_SEEK_TIME)
            isMaximized = it.getBoolean(KEY_SAVED_IS_MAXIMIZED)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(KEY_SAVED_VIEW_STATE, viewState.videoIsOpen.get() ?: false)
        outState?.putParcelable(KEY_SAVED_LAST_VIDEO, lastVideo)
        outState?.putInt(KEY_SAVED_LAST_SEEK_TIME, lastSeekTime)
        outState?.putBoolean(KEY_SAVED_IS_MAXIMIZED, isMaximized)
    }

    private fun initView() {
        val binding: ActivityTrackVideosBinding = DataBindingUtil.setContentView(this, R.layout.activity_track_videos)
        binding.view = view

        initYoutubeFragment(binding)
        if (binding is ActivityTrackVideosBindingLandImpl) {
            if (!isMaximized) minimizeLandscapeFragmentContainer()

            enlarge_player_btn?.setOnClickListener {
                if (!isMaximized) {
                    maximizeLandscapeFragmentContainer()
                    isMaximized = true
                }
            }

            close_player_btn?.setOnClickListener {
                pause()
                view.state.videoIsOpen.set(false)
            }
        } else {
            initDraggablePanel(binding.draggablePanel!!)
        }
    }

    private fun maximizeLandscapeFragmentContainer() {
        player_fragment_container?.changeParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        player_fragment_container?.changeMarginParams(0, 0, 0, 0)
        enlarge_player_btn?.visibility = View.GONE
        close_player_btn?.visibility = View.GONE
        youtubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
    }

    private fun initYoutubeFragment(binding: ActivityTrackVideosBinding) {
        youtubePlayerFragment = if (binding is ActivityTrackVideosBindingLandImpl) {
            supportFragmentManager.findFragmentById(R.id.player_fragment) as? YouTubePlayerSupportFragment
        } else {
            YouTubePlayerSupportFragment()
        }

        youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
    }

    private val draggableListener = object : DraggableListener {
        override fun onMaximized() {
            play()
            isMaximized = true
        }

        override fun onMinimized() {
            isMaximized = false
        }

        override fun onClosedToLeft() {
            pause()
            view.state.videoIsOpen.set(false)
        }

        override fun onClosedToRight() {
            pause()
            view.state.videoIsOpen.set(false)
        }
    }

    private fun pause() {
        if (youtubePlayer?.isPlaying == true) {
            showStatusBar()
            youtubePlayer?.pause()
        }
    }

    private fun play() {
        if (youtubePlayer?.isPlaying == false) {
            hideStatusBar()
            view.state.videoIsOpen.set(true)
            draggable_panel?.let { ViewCompat.setTranslationZ(it, 0f) }
            youtubePlayer?.play()
        }
    }

    private fun initDraggablePanel(draggablePanel: DraggablePanel) = with(draggablePanel) {
        setFragmentManager(supportFragmentManager)
        setTopFragment(youtubePlayerFragment)
        val bottomFragment = TrackDetailsFragment()
        setBottomFragment(bottomFragment)

        setDraggableListener(draggableListener)
        isClickToMaximizeEnabled = true

        setXScaleFactor(resources.getDimenFloat(R.dimen.x_scale_factor))
        setYScaleFactor(resources.getDimenFloat(R.dimen.y_scale_factor))

        setTopViewHeight(resources.getDimensionPixelSize(R.dimen.video_player_fragment_height))
        setTopFragmentMarginRight(resources.getDimensionPixelSize(R.dimen.top_fragment_margin))
        setTopFragmentMarginBottom(resources.getDimensionPixelSize(R.dimen.top_fragment_margin))

        initializeView()
    }

    private fun initToolbar() {
        setSupportActionBar(track_videos_toolbar)
        track_videos_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        track_videos_toolbar.setNavigationOnClickListener { super.onBackPressed() }
        title = track.name
    }

    private var lastVideo: Video? = null
    private var lastSeekTime = 0
    private var isMaximized = true

    override fun playVideo(video: Video) {
        lastVideo = video
        view.state.videoIsOpen.set(true)
        youtubePlayer?.loadVideo(video.id)
        draggable_panel?.maximize()
        hideStatusBar()
    }

    private var youtubePlayer: YouTubePlayer? = null
    private var youtubePlayerFragment: YouTubePlayerSupportFragment? = null

    private val onPlayerInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
            youtubePlayer = player
            youtubePlayer?.setShowFullscreenButton(false)

            if (viewState.videoIsOpen.get() == true && lastVideo != null) {
                youtubePlayer?.loadVideo(lastVideo!!.id, lastSeekTime)
                play()
                if (!isMaximized) draggable_panel?.minimize()
            }
        }

        override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
            if (error.isUserRecoverableError) {
                error.getErrorDialog(this@TrackVideosActivity, RECOVERY_DIALOG_REQUEST).show()
            } else {
                val errorMessage = String.format(getString(R.string.error_player), error.toString())
                Toast.makeText(this@TrackVideosActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (view.state.videoIsOpen.get() == true) lastSeekTime = youtubePlayer?.currentTimeMillis ?: 0
        youtubePlayer = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
        }
    }

    private fun minimizeLandscapeFragmentContainer() {
        player_fragment_container?.changeParams(
                width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt(),
                height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 225f, resources.displayMetrics).toInt())
        player_fragment_container?.changeMarginParams(0, 0, 20, 20)
        enlarge_player_btn?.visibility = View.VISIBLE
        close_player_btn?.visibility = View.VISIBLE
        youtubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
    }

    override fun onBackPressed() {
        if (view.state.videoIsOpen.get() == true && isMaximized) {
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                minimizeLandscapeFragmentContainer()
            } else {
                draggable_panel?.minimize()
            }
            isMaximized = false
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        private const val KEY_SAVED_VIEW_STATE = "KEY_SAVED_VIEW_STATE"
        private const val KEY_SAVED_LAST_VIDEO = "KEY_SAVED_LAST_VIDEO"
        private const val KEY_SAVED_LAST_SEEK_TIME = "KEY_SAVED_LAST_SEEK_TIME"
        private const val KEY_SAVED_IS_MAXIMIZED = "KEY_SAVED_IS_MAXIMIZED"

        private const val RECOVERY_DIALOG_REQUEST = 1

        fun start(activity: Activity, track: Track) {
            val intent = Intent(activity, TrackVideosActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
            activity.startActivity(intent)
        }
    }
}
