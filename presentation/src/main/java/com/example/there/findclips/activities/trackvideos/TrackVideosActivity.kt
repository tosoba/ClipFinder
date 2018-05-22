package com.example.there.findclips.activities.trackvideos

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseVMActivity
import com.example.there.findclips.databinding.ActivityTrackVideosBinding
import com.example.there.findclips.fragments.search.videos.VideosSearchFragment
import com.example.there.findclips.fragments.track.TrackFragment
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.app
import com.example.there.findclips.view.OnPageChangeListener
import com.example.there.findclips.view.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_track_videos.*
import javax.inject.Inject


class TrackVideosActivity : BaseVMActivity<TrackVideosViewModel>(), OnTrackChangeListener {

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

    private val onFavouriteBtnClickListener = View.OnClickListener {
        viewModel.viewState.track.get()?.let {
            viewModel.addFavouriteTrack(it)
            Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
        }
    }

    private val intentTrack: Track by lazy { intent.getParcelableExtra(EXTRA_TRACK) as Track }

    private val view: TrackVideosView by lazy {
        TrackVideosView(state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = onFavouriteBtnClickListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTrackVideosBinding = DataBindingUtil.setContentView(this, R.layout.activity_track_videos)
        binding.view = view
        if (savedInstanceState == null) {
            viewModel.viewState.track.set(intentTrack)
        }
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(track_videos_toolbar)
        track_videos_toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        track_videos_toolbar?.setNavigationOnClickListener { super.onBackPressed() }
        title = viewModel.viewState.track.get()?.name ?: intentTrack.name
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
