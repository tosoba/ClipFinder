package com.example.there.findclips.activities.album

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.activities.artist.ArtistFragment
import com.example.there.findclips.activities.trackvideos.TrackVideosFragment
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.base.fragment.HasBackNavigation
import com.example.there.findclips.databinding.FragmentAlbumBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.model.entities.Track
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.view.lists.ArtistsList
import com.example.there.findclips.view.lists.OnArtistClickListener
import com.example.there.findclips.view.lists.OnTrackClickListener
import com.example.there.findclips.view.lists.TracksPopularityList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_album.*

class AlbumFragment : BaseSpotifyVMFragment<AlbumViewModel>(), Injectable, HasBackNavigation {

    private val album: Album by lazy { arguments!!.getParcelable<Album>(ARG_ALBUM) }

    private val artistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item, object : OnArtistClickListener {
            override fun onClick(item: Artist) {
                hostFragment?.showFragment(ArtistFragment.newInstance(artist = item), true)
            }
        })
    }

    private val tracksAdapter: TracksPopularityList.Adapter by lazy {
        TracksPopularityList.Adapter(viewModel.viewState.tracks, R.layout.track_popularity_item, object : OnTrackClickListener {
            override fun onClick(item: Track) {
                hostFragment?.showFragment(TrackVideosFragment.newInstance(track = item), true)
            }
        })
    }

    private val view: AlbumView by lazy {
        AlbumView(
                state = viewModel.viewState,
                album = album,
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.addFavouriteAlbum(album)
                    Toast.makeText(activity, "Added to favourites.", Toast.LENGTH_SHORT).show()
                },
                artistsAdapter = artistsAdapter,
                tracksAdapter = tracksAdapter,
                separatorDecoration = SeparatorDecoration(activity!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                onTracksScrollListener = object : EndlessRecyclerOnScrollListener() {
                    override fun onLoadMore() {
                        activity?.accessToken?.let { viewModel.loadTracksFromAlbum(it, album.id) }
                    }
                }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.viewState.tracks.isNotEmpty() && viewModel.viewState.artists.isNotEmpty() },
                album_root_layout,
                ::loadData
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAlbumBinding>(inflater, R.layout.fragment_album, container, false)
        return binding.apply {
            this.view = this@AlbumFragment.view
            albumContent?.view = view
            albumContent?.albumArtistsRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            albumContent?.albumTracksRecyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycle.addObserver(connectivityComponent)

        if (savedInstanceState == null)
            loadData()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(AlbumViewModel::class.java)
    }

    private fun loadData() = viewModel.loadAlbumData(activity?.accessToken, album)

    companion object {
        private const val ARG_ALBUM = "ARG_ALBUM"

        fun newInstance(album: Album) = AlbumFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_ALBUM, album) }
        }
    }
}