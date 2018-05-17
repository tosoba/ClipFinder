package com.example.there.findclips.activities.album

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.base.BaseSpotifyVMActivity
import com.example.there.findclips.databinding.ActivityAlbumBinding
import com.example.there.findclips.fragments.lists.SpotifyTracksFragment
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.OnArtistClickListener
import kotlinx.android.synthetic.main.activity_album.*
import javax.inject.Inject

class AlbumActivity : BaseSpotifyVMActivity<AlbumViewModel>() {

    private val album: Album by lazy { intent.getParcelableExtra<Album>(EXTRA_ALBUM) }

    private val artistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item, object : OnArtistClickListener {
            override fun onClick(item: Artist) {
                Router.goToArtistActivity(this@AlbumActivity, artist = item)
            }
        })
    }

    private val view: AlbumView by lazy {
        AlbumView(state = viewModel.viewState,
                album = album,
                onFavouriteBtnClickListener = View.OnClickListener {
                    Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
                },
                artistsAdapter = artistsAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()

        if (savedInstanceState == null) {
            viewModel.loadAlbumData(accessToken, album)
        }
    }

    private fun initView() {
        val binding: ActivityAlbumBinding = DataBindingUtil.setContentView(this, R.layout.activity_album)
        binding.apply {
            this.view = this@AlbumActivity.view
            albumContent?.view = view
            albumContent?.albumArtistsRecyclerView?.layoutManager = LinearLayoutManager(this@AlbumActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(album_toolbar)
        album_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        album_toolbar.setNavigationOnClickListener { super.onBackPressed() }
        title = album.name
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.tracks.observe(this, Observer {
            it?.let {
                val fragment = supportFragmentManager.findFragmentById(R.id.album_tracks_fragment) as? SpotifyTracksFragment
                fragment?.addItems(it)
            }
        })
    }

    override fun initComponent() {
        app.createAlbumSubComponent().inject(this)
    }

    override fun releaseComponent() {
        app.releaseAlbumSubComponent()
    }

    @Inject
    lateinit var factory: AlbumVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(AlbumViewModel::class.java)
    }

    companion object {
        private const val EXTRA_ALBUM = "EXTRA_ALBUM"

        fun start(activity: Activity, album: Album) {
            val intent = Intent(activity, AlbumActivity::class.java).apply {
                putExtra(EXTRA_ALBUM, album)
            }
            activity.startActivity(intent)
        }
    }
}
