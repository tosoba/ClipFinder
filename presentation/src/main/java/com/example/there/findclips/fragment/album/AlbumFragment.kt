package com.example.there.findclips.fragment.album

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentAlbumBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.artist.ArtistFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.impl.ArtistsList
import com.example.there.findclips.view.list.impl.TracksPopularityList
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.SeparatorDecoration

class AlbumFragment : BaseSpotifyVMFragment<AlbumViewModel>(), Injectable {

    private val album: Album by lazy { arguments!!.getParcelable<Album>(ARG_ALBUM) }

    private val artistsAdapter: ArtistsList.Adapter by lazy {
        ArtistsList.Adapter(viewModel.viewState.artists, R.layout.artist_item)
    }

    private val tracksAdapter: TracksPopularityList.Adapter by lazy {
        TracksPopularityList.Adapter(viewModel.viewState.tracks, R.layout.track_popularity_item)
    }

    private val albumAdapter: AlbumAdapter by lazy {
        AlbumAdapter(
                artistsAdapter,
                tracksAdapter,
                viewModel.viewState.artistsLoadingInProgress,
                viewModel.viewState.tracksLoadingInProgress,
                object : EndlessRecyclerOnScrollListener() {
                    override fun onLoadMore() {
                        activity?.accessToken?.let { viewModel.loadTracksFromAlbum(it, album.id) }
                    }
                },
                SeparatorDecoration(activity!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    private val view: AlbumView by lazy {
        AlbumView(
                state = viewModel.viewState,
                album = album,
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.addFavouriteAlbum(album)
                    Toast.makeText(activity, "Added to favourites.", Toast.LENGTH_SHORT).show()
                },
                albumAdapter = albumAdapter
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.viewState.tracks.isNotEmpty() && viewModel.viewState.artists.isNotEmpty() },
                mainActivity!!.connectivitySnackbarParentView!!,
                ::loadData
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAlbumBinding>(inflater, R.layout.fragment_album, container, false)
        return binding.apply {
            this.view = this@AlbumFragment.view
            albumRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mainActivity?.setSupportActionBar(albumToolbar)
            albumToolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            albumToolbar.setNavigationOnClickListener { mainActivity?.onBackPressed() }
        }.root
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycle.addObserver(disposablesComponent)
        initItemClicks()

        loadData()
    }

    private fun initItemClicks() {
        disposablesComponent.add(artistsAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
        })
        disposablesComponent.add(tracksAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
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
