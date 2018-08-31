package com.example.there.findclips.activities.videoplaylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.R
import com.example.there.findclips.fragments.search.videos.VideosSearchFragment
import com.example.there.findclips.model.entities.VideoPlaylist
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_video_playlist.*
import javax.inject.Inject

class VideoPlaylistActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    private val intentPlaylist: VideoPlaylist by lazy {
        intent.getParcelableExtra<VideoPlaylist>(EXTRA_PLAYLIST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_playlist)
        initToolbar()

        supportFragmentManager.beginTransaction()
                .replace(R.id.video_playlist_fragment_container, VideosSearchFragment.newInstanceWithVideoPlaylist(intentPlaylist))
                .commit()
    }

    private fun initToolbar() {
        setSupportActionBar(video_playlist_toolbar)
        video_playlist_toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        video_playlist_toolbar?.setNavigationOnClickListener { super.onBackPressed() }
        title = intentPlaylist.name
    }

    companion object {
        private const val EXTRA_PLAYLIST = "EXTRA_PLAYLIST"

        fun start(activity: Activity, playlist: VideoPlaylist) {
            val intent = Intent(activity, VideoPlaylistActivity::class.java).apply {
                putExtra(EXTRA_PLAYLIST, playlist)
            }
            activity.startActivity(intent)
        }
    }
}
