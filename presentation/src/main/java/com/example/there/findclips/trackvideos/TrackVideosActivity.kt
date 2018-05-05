package com.example.there.findclips.trackvideos

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.ActivityTrackVideosBinding
import com.example.there.findclips.entities.Track
import com.example.there.findclips.search.videos.VideosSearchFragment
import com.example.there.findclips.trackdetails.TrackDetailsFragment
import com.example.there.findclips.util.OnPageChangeListener
import com.example.there.findclips.util.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_track_videos.*

class TrackVideosActivity : AppCompatActivity() {

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

    private val view: TrackVideosActivityView by lazy {
        TrackVideosActivityView(
                state = TrackVideosViewState(track),
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initToolbar()
    }

    private fun initBinding() {
        val binding: ActivityTrackVideosBinding = DataBindingUtil.setContentView(this, R.layout.activity_track_videos)
        binding.view = view
    }

    private fun initToolbar() {
        setSupportActionBar(track_videos_toolbar)
        track_videos_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        track_videos_toolbar.setNavigationOnClickListener { onBackPressed() }
        title = track.name
    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        fun start(activity: FragmentActivity, track: Track) {
            val intent = Intent(activity, TrackVideosActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
            activity.startActivity(intent)
        }
    }
}
