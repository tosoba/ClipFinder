package com.example.there.findclips.activities.trackvideos

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewCompat
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.view.player.PlayerView
import com.example.there.findclips.view.player.PlayerViewState
import com.example.there.findclips.base.BaseVMActivity
import com.example.there.findclips.databinding.ActivityTrackVideosBinding
import com.example.there.findclips.databinding.ActivityTrackVideosBindingLandImpl
import com.example.there.findclips.fragments.search.videos.VideosSearchFragment
import com.example.there.findclips.fragments.track.TrackFragment
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.util.*
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnTabSelectedListener
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_track_videos.*
import java.lang.Exception
import javax.inject.Inject


class TrackVideosActivity : BaseVMActivity<TrackVideosViewModel>(), OnTrackChangeListener, PlayerView.OnVideoSelectedListener {

    override fun onVideoSelected(video: Video) = playerView.playVideo(video)

    private val playerView: PlayerView by lazy {
        object : PlayerView(this@TrackVideosActivity) {
            override fun initYoutubeFragment(binding: ViewDataBinding) {
                youtubePlayerFragment = if (binding is ActivityTrackVideosBindingLandImpl) {
                    supportFragmentManager.findFragmentById(R.id.track_videos_player_fragment) as? YouTubePlayerSupportFragment
                } else {
                    YouTubePlayerSupportFragment()
                }

                youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
            }

            override fun initView() {
                val binding: ActivityTrackVideosBinding = DataBindingUtil.setContentView(this@TrackVideosActivity, R.layout.activity_track_videos)
                binding.view = view

                initYoutubeFragment(binding)
                if (binding is ActivityTrackVideosBindingLandImpl) {
                    if (state.isMaximized.get() == false) minimizeLandscapeFragmentContainer()
                    track_videos_player_fragment?.view?.setOnTouchListener(onYoutubeFragmentTouchListener)
                } else {
                    initDraggablePanel(binding.trackVideosDraggablePanel!!, this@TrackVideosActivity.supportFragmentManager)
                }
            }

            override val state: PlayerViewState = PlayerViewState()

            override fun maximizeLandscapeFragmentContainer() {
                if (state.isMaximized.get() == false) {
                    track_videos_player_fragment_container?.changeParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    track_videos_player_fragment_container?.changeMarginParams(0, 0, 0, 0)
                    state.isMaximized.set(true)
                }
            }

            override fun playVideo(video: Video) {
                lastVideo = video
                state.videoIsOpen.set(true)
                youtubePlayer?.loadVideo(video.id)
                track_videos_draggable_panel?.maximize()
                track_videos_draggable_panel?.relatedVideosFragment?.videoId = video.id
                hideStatusBar()
            }

            override fun minimizeLandscapeFragmentContainer() {
                if (state.isMaximized.get() == true) {
                    track_videos_player_fragment_container?.changeParams(
                            width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt(),
                            height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 225f, resources.displayMetrics).toInt())
                    track_videos_player_fragment_container?.changeMarginParams(0, 0, 20, 20)
                    state.isMaximized.set(false)
                }
            }

            override fun pause() {
                if (youtubePlayer?.isPlaying == true) {
                    showStatusBar()
                    try {
                        youtubePlayer?.pause()
                    } catch (e: Exception) {
                        Log.e(javaClass.name, "YoutubePlayer exception: ${e.message}")
                    }
                }
            }

            override fun play() {
                if (youtubePlayer?.isPlaying == false) {
                    hideStatusBar()
                    state.videoIsOpen.set(true)
                    track_videos_draggable_panel?.let { ViewCompat.setTranslationZ(it, 0f) }
                    try {
                        youtubePlayer?.play()
                    } catch (e: Exception) {
                        Log.e(javaClass.name, "YoutubePlayer exception: ${e.message}")
                    }
                }
            }

            override fun closeVideo() {
                super.closeVideo()
                showStatusBar()
                supportActionBar?.show()
            }
        }
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            track_videos_tab_layout?.getTabAt(position)?.select()
            updateCurrentFragment(viewModel.viewState.track.get()!!)
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                track_videos_viewpager.currentItem = it.position
            }
        }
    }

    override fun onTrackChanged(newTrack: Track) {
        updateCurrentFragment(newTrack)
        view.state.track.set(newTrack)
        track_videos_toolbar_layout?.title = newTrack.name
    }

    private fun updateCurrentFragment(newTrack: Track) {
        val currentFragment = pagerAdapter.currentFragment
        when (currentFragment) {
            is VideosSearchFragment -> currentFragment.query = newTrack.query
            is TrackFragment -> currentFragment.track = newTrack
        }
    }

    private val pagerAdapter: TrackVideosPagerAdapter by lazy {
        TrackVideosPagerAdapter(manager = supportFragmentManager,
                fragments = arrayOf(VideosSearchFragment.newInstanceWithQuery(intentTrack.query), TrackFragment.newInstanceWithTrack(intentTrack)))
    }

    private val intentTrack: Track by lazy { intent.getParcelableExtra(EXTRA_TRACK) as Track }

    private val view: TrackVideosView by lazy {
        TrackVideosView(
                state = viewModel.viewState,
                playerState = playerView.state,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = View.OnClickListener {
                    Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
                }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerView.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.viewState.track.set(intentTrack)
        }
        initToolbar()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        playerView.onSaveInstanceState(outState)
    }

    private fun initToolbar() {
        setSupportActionBar(track_videos_toolbar)
        track_videos_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        track_videos_toolbar.setNavigationOnClickListener { super.onBackPressed() }
        title = viewModel.viewState.track.get()!!.name
    }

    override fun onBackPressed() {
        if (playerView.state.videoIsOpen.get() == true && playerView.state.isMaximized.get() == true) {
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerView.minimizeLandscapeFragmentContainer()
            } else {
                track_videos_draggable_panel?.minimize()
                playerView.state.isMaximized.set(false)
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        playerView.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        playerView.onActivityResult(requestCode)
    }

    override fun initComponent() = app.createTrackVideosSubComponent().inject(this)

    override fun releaseComponent() = app.releaseTrackVideosSubComponent()

    @Inject
    lateinit var factory: TrackVideosVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(TrackVideosViewModel::class.java)
    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        fun start(activity: Activity, track: Track) {
            val intent = Intent(activity, TrackVideosActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
            activity.startActivity(intent)
        }
    }
}
