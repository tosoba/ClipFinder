package com.example.there.findclips.activities.playlist

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
import com.example.there.findclips.base.activity.BaseSpotifyVMActivity
import com.example.there.findclips.databinding.ActivityPlaylistBinding
import com.example.there.findclips.fragments.lists.SpotifyTracksFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.util.accessToken
import kotlinx.android.synthetic.main.activity_playlist.*

class PlaylistActivity : BaseSpotifyVMActivity<PlaylistViewModel>() {

    private val playlist: Playlist by lazy { intent.getParcelableExtra(EXTRA_PLAYLIST) as Playlist }

    private val view: PlaylistView by lazy {
        PlaylistView(state = viewModel.viewState,
                playlist = playlist,
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.addFavouritePlaylist(playlist)
                    Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
                })
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                this,
                viewModel.tracks.value != null,
                playlist_root_layout,
                ::loadData
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        lifecycle.addObserver(connectivityComponent)
        initToolbar()

        if (savedInstanceState == null)
            loadData()
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
                fragment.updateItems(it)
            }
        })
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(PlaylistViewModel::class.java)
    }

    private fun loadData() = viewModel.loadTracks(accessToken, playlist)

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
