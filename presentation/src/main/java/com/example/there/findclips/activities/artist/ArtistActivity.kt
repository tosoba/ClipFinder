package com.example.there.findclips.activities.artist

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.there.findclips.databinding.ActivityArtistBinding
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.base.BaseSpotifyVMActivity
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import com.example.there.findclips.view.lists.*
import kotlinx.android.synthetic.main.activity_artist.*
import okhttp3.Route
import javax.inject.Inject

class ArtistActivity : BaseSpotifyVMActivity<ArtistViewModel>() {

    private val artist: Artist by lazy { intent.getParcelableExtra<Artist>(EXTRA_ARTIST) }

    private val albumsAdapter: AlbumsList.Adapter by lazy {
        AlbumsList.Adapter(viewModel.viewState.albums, R.layout.album_item, object : OnAlbumClickListener {
            override fun onClick(item: Album) = Router.goToAlbumAcitivity(this@ArtistActivity, album = item)
        })
    }

    private val topTracksAdapter: TracksList.Adapter by lazy {
        TracksList.Adapter(viewModel.viewState.topTracks, R.layout.track_item, object : OnTrackClickListener {
            override fun onClick(item: Track) = Router.goToTrackVideosActivity(this@ArtistActivity, track = item)
        })
    }

    private val relatedArtistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.relatedArtists, R.layout.artist_item, object : OnArtistClickListener {
            override fun onClick(item: Artist) {
                //TODO: reload with new artist
            }
        })
    }

    private val view: ArtistView by lazy {
        ArtistView(state = viewModel.viewState,
                artist = artist,
                onFavouriteBtnClickListener = View.OnClickListener {
                    Toast.makeText(this, "Added to favourites.", Toast.LENGTH_SHORT).show()
                },
                albumsAdapter = albumsAdapter,
                topTracksAdapter = topTracksAdapter,
                relatedArtistsAdapter = relatedArtistsAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initToolbar()

        if (savedInstanceState == null) {
            viewModel.loadArtistData(accessToken, artist)
        }
    }

    private fun initView() {
        val binding: ActivityArtistBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist)
        binding.apply {
            this.view = this@ArtistActivity.view
            artistContent?.view = view
            artistContent?.artistAlbumsRecyclerView?.layoutManager = GridLayoutManager(this@ArtistActivity, 2, GridLayoutManager.HORIZONTAL, false)
            artistContent?.artistTopTracksRecyclerView?.layoutManager = GridLayoutManager(this@ArtistActivity, 2, GridLayoutManager.HORIZONTAL, false)
            artistContent?.artistRelatedArtistsRecyclerView?.layoutManager = GridLayoutManager(this@ArtistActivity, 2, GridLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(artist_toolbar)
        artist_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        artist_toolbar.setNavigationOnClickListener { onBackPressed() }
        title = artist.name
    }

    override fun initComponent() {
        app.createArtistSubComponent().inject(this)
    }

    override fun releaseComponent() {
        app.releaseArtistSubComponent()
    }

    @Inject
    lateinit var factory: ArtistVMFactory

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(ArtistViewModel::class.java)
    }

    companion object {
        private const val EXTRA_ARTIST = "EXTRA_ARTIST"

        fun start(activity: Activity, artist: Artist) {
            val intent = Intent(activity, ArtistActivity::class.java).apply {
                putExtra(EXTRA_ARTIST, artist)
            }
            activity.startActivity(intent)
        }
    }
}
