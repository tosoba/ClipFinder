package com.example.there.findclips.fragment.artist

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.base.fragment.HasBackNavigation

import com.example.there.findclips.databinding.FragmentArtistBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.album.AlbumFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.*
import kotlinx.android.synthetic.main.fragment_artist.*

class ArtistFragment : BaseSpotifyVMFragment<ArtistViewModel>(), Injectable, HasBackNavigation, GoesToPreviousStateOnBackPressed {

    private val argArtist: Artist by lazy { arguments!!.getParcelable<Artist>(ARG_ARTIST) }

    private val albumsAdapter: AlbumsList.Adapter by lazy {
        AlbumsList.Adapter(viewModel.viewState.albums, R.layout.album_item, object : OnAlbumClickListener {
            override fun onClick(item: Album) {
                hostFragment?.showFragment(AlbumFragment.newInstance(album = item), true)
            }
        })
    }

    private val topTracksAdapter: TracksList.Adapter by lazy {
        TracksList.Adapter(viewModel.viewState.topTracks, R.layout.track_item, object : OnTrackClickListener {
            override fun onClick(item: Track) {
                hostFragment?.showFragment(TrackVideosFragment.newInstance(track = item), true)
            }
        })
    }

    private val relatedArtistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.relatedArtists, R.layout.artist_item, object : OnArtistClickListener {
            override fun onClick(item: Artist) = viewModel.loadArtistData(activity?.accessToken, artist = item)
        })
    }

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
                artist_root_layout,
                ::loadData
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycle.addObserver(connectivityComponent)

        if (savedInstanceState == null) {
            viewModel.loadArtistData(activity?.accessToken, argArtist)
        }
    }


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
