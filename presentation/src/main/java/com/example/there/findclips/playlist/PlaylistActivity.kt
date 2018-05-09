package com.example.there.findclips.playlist

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMActivity
import com.example.there.findclips.databinding.ActivityPlaylistBinding
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.listfragments.SpotifyTracksFragment
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import kotlinx.android.synthetic.main.activity_playlist.*
import javax.inject.Inject

class PlaylistActivity : BaseSpotifyVMActivity<PlaylistViewModel>() {

    private val playlist: Playlist by lazy { intent.getParcelableExtra(EXTRA_PLAYLIST) as Playlist }

    private val view: PlaylistView by lazy {
        PlaylistView(state = viewModel.viewState,
                playlist = playlist,
                onFavouriteBtnClickListener = View.OnClickListener {
                    Toast.makeText(this,"Added to favourites.", Toast.LENGTH_SHORT).show()
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()

        if (savedInstanceState == null) {
            viewModel.loadTracks(accessToken, playlist)
        }
    }

    private fun initView() {
        val binding: ActivityPlaylistBinding = DataBindingUtil.setContentView(this, R.layout.activity_playlist)
        binding.view = view
    }

    private fun initToolbar() {
        setSupportActionBar(playlist_toolbar)
        playlist_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        playlist_toolbar.setNavigationOnClickListener { onBackPressed() }
        title = playlist.name
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.tracks.observe(this, Observer {
            it?.let {
                val fragment = supportFragmentManager.findFragmentById(R.id.playlist_spotify_tracks_fragment) as SpotifyTracksFragment
                fragment.addItems(it)
            }
        })
    }

    override fun initComponent() {
        app.createPlaylistComponent().inject(this)
    }

    override fun releaseComponent() {
        app.releasePlaylistComponent()
    }

    @Inject
    lateinit var factory: PlaylistVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(PlaylistViewModel::class.java)
    }

    companion object {
        private const val EXTRA_PLAYLIST = "EXTRA_PLAYLIST"

        fun start(activity: Activity, playlist: Playlist) {
            val intent = Intent(activity, PlaylistActivity::class.java).apply {
                putExtra(EXTRA_PLAYLIST, playlist)
            }
            activity.startActivity(intent)
        }
    }
}
