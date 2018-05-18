package com.example.there.findclips.activities.trackvideos

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
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
import com.example.there.findclips.activities.player.BasePlayerActivity
import com.example.there.findclips.activities.player.PlayerViewState
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


class TrackVideosActivity : BasePlayerActivity(), OnTrackChangeListener {

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            track_videos_tab_layout?.getTabAt(position)?.select()
            updateCurrentFragment(trackVideosViewState.track.get()!!)
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

    override val viewState: PlayerViewState by lazy { TrackVideosViewState(track = ObservableField(intentTrack)) }

    private val trackVideosViewState: TrackVideosViewState
        get() = viewState as TrackVideosViewState

    private val view: TrackVideosView by lazy {
        TrackVideosView(
                state = viewState as TrackVideosViewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = View.OnClickListener {
                    Toast.makeText(this,"Added to favourites.", Toast.LENGTH_SHORT).show()
                }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { trackVideosViewState.track.set(it.getParcelable(KEY_SAVED_TRACK)) }
        initToolbar()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(KEY_SAVED_TRACK, trackVideosViewState.track.get())
    }

    override fun initView() {
        val binding: ActivityTrackVideosBinding = DataBindingUtil.setContentView(this, R.layout.activity_track_videos)
        binding.view = view

        initYoutubeFragment(binding)
        if (binding is ActivityTrackVideosBindingLandImpl) {
            if (viewState.isMaximized.get() == false) minimizeLandscapeFragmentContainer()
            track_videos_player_fragment?.view?.setOnTouchListener(onYoutubeFragmentTouchListener)
        } else {
            initDraggablePanel(binding.trackVideosDraggablePanel!!)
        }
    }

    override fun maximizeLandscapeFragmentContainer() {
        if (viewState.isMaximized.get() == false) {
            track_videos_player_fragment_container?.changeParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            track_videos_player_fragment_container?.changeMarginParams(0, 0, 0, 0)
            viewState.isMaximized.set(true)
        }
    }

    override fun initYoutubeFragment(binding: ViewDataBinding) {
        youtubePlayerFragment = if (binding is ActivityTrackVideosBindingLandImpl) {
            supportFragmentManager.findFragmentById(R.id.track_videos_player_fragment) as? YouTubePlayerSupportFragment
        } else {
            YouTubePlayerSupportFragment()
        }

        youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
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
            view.state.videoIsOpen.set(true)
            track_videos_draggable_panel?.let { ViewCompat.setTranslationZ(it, 0f) }
            try {
                youtubePlayer?.play()
            } catch (e: Exception) {
                Log.e(javaClass.name, "YoutubePlayer exception: ${e.message}")
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(track_videos_toolbar)
        track_videos_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        track_videos_toolbar.setNavigationOnClickListener { super.onBackPressed() }
        title = trackVideosViewState.track.get()!!.name
    }

    override fun playVideo(video: Video) {
        lastVideo = video
        view.state.videoIsOpen.set(true)
        youtubePlayer?.loadVideo(video.id)
        track_videos_draggable_panel?.maximize()
        track_videos_draggable_panel?.relatedVideosFragment?.videoId = video.id
        hideStatusBar()
    }

    override fun minimizeLandscapeFragmentContainer() {
        if (viewState.isMaximized.get() == true) {
            track_videos_player_fragment_container?.changeParams(
                    width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt(),
                    height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 225f, resources.displayMetrics).toInt())
            track_videos_player_fragment_container?.changeMarginParams(0, 0, 20, 20)
            viewState.isMaximized.set(false)
        }
    }

    override fun onBackPressed() {
        if (view.state.videoIsOpen.get() == true && viewState.isMaximized.get() == true) {
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                minimizeLandscapeFragmentContainer()
            } else {
                track_videos_draggable_panel?.minimize()
                viewState.isMaximized.set(false)
            }
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        private const val KEY_SAVED_TRACK = "KEY_SAVED_TRACK"

        fun start(activity: Activity, track: Track) {
            val intent = Intent(activity, TrackVideosActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
            activity.startActivity(intent)
        }
    }
}
