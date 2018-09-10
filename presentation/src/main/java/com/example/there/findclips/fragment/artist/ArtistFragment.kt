package com.example.there.findclips.fragment.artist

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.databinding.FragmentArtistBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.album.AlbumFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.impl.AlbumsList
import com.example.there.findclips.view.list.impl.ArtistsList
import com.example.there.findclips.view.list.impl.TracksList

class ArtistFragment : BaseSpotifyVMFragment<ArtistViewModel>(), Injectable, GoesToPreviousStateOnBackPressed {

    private val argArtist: Artist by lazy { arguments!!.getParcelable<Artist>(ARG_ARTIST) }

    private val albumsAdapter: AlbumsList.Adapter by lazy {
        AlbumsList.Adapter(viewModel.viewState.albums, R.layout.album_item)
    }

    private val topTracksAdapter: TracksList.Adapter by lazy {
        TracksList.Adapter(viewModel.viewState.topTracks, R.layout.track_item)
    }

    private val relatedArtistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.relatedArtists, R.layout.artist_item)
    }

    private val disposablesComponent = DisposablesComponent()

    private val view: ArtistView by lazy {
        ArtistView(
                state = viewModel.viewState,
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.addFavouriteArtist()
                    Toast.makeText(activity, "Added to favourites.", Toast.LENGTH_SHORT).show()
                },
                albumsAdapter = albumsAdapter,
                topTracksAdapter = topTracksAdapter,
                relatedArtistsAdapter = relatedArtistsAdapter
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                {
                    viewModel.viewState.albums.isNotEmpty() &&
                            viewModel.viewState.artist.get() != null &&
                            viewModel.viewState.topTracks.isNotEmpty() &&
                            viewModel.viewState.topTracks.isNotEmpty()
                },
                mainActivity!!.connectivitySnackbarParentView!!,
                {
                    val artistToLoad = if (viewModelInitialized && viewModel.viewState.artist.get() != null)
                        viewModel.viewState.artist.get()!!
                    else argArtist
                    viewModel.loadArtistData(activity?.accessToken, artistToLoad)
                }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        initItemClicks()
        loadData()
    }

    private fun initItemClicks() {
        disposablesComponent.add(albumsAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
        })
        disposablesComponent.add(topTracksAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
        })
        disposablesComponent.add(relatedArtistsAdapter.itemClicked.subscribe {
            viewModel.loadArtistData(activity?.accessToken, artist = it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
        return binding.apply {
            this.view = this@ArtistFragment.view
            artistContent?.view = view
            artistContent?.artistAlbumsRecyclerView?.layoutManager =
                    GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
            artistContent?.artistTopTracksRecyclerView?.layoutManager =
                    GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
            artistContent?.artistRelatedArtistsRecyclerView?.layoutManager =
                    GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
            mainActivity?.setSupportActionBar(artistToolbar)
            artistToolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            artistToolbar.setNavigationOnClickListener { mainActivity?.onBackPressed() }
        }.root
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) mainActivity?.backPressedOnNoPreviousFragmentState()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(ArtistViewModel::class.java)
    }

    private fun loadData() = viewModel.loadArtistData(activity?.accessToken, argArtist)

    companion object {
        private const val ARG_ARTIST = "ARG_ARTIST"

        fun newInstance(artist: Artist) = ArtistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ARTIST, artist)
            }
        }
    }
}
