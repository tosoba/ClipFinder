package com.example.there.findclips.fragment.spotifyitem.artist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.there.findclips.databinding.FragmentArtistBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.spotifyitem.album.AlbumFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_artist.*

class ArtistFragment :
        BaseVMFragment<ArtistViewModel>(ArtistViewModel::class.java),
        Injectable,
        GoesToPreviousStateOnBackPressed {

    private val argArtist: Artist by lazy { arguments!!.getParcelable<Artist>(ARG_ARTIST) }

    private val artistAdapter: ArtistAdapter by lazy {
        ArtistAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.albumsLoadingInProgress, viewModel.viewState.albums),
                        object : ListItemView<Album>(viewModel.viewState.albums) {
                            override val itemViewBinder: ItemBinder<Album>
                                get() = ItemBinderBase(BR.album, R.layout.album_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener {
                            viewModel.loadAlbumsFromArtist(artistToLoad.id)
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.topTracksLoadingInProgress, viewModel.viewState.topTracks),
                        object : ListItemView<Track>(viewModel.viewState.topTracks) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.track, R.layout.track_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener {
                            viewModel.loadTopTracksFromArtist(artistToLoad.id)
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.relatedArtistsLoadingInProgress, viewModel.viewState.relatedArtists),
                        object : ListItemView<Artist>(viewModel.viewState.relatedArtists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.artist, R.layout.artist_item)
                        },
                        ClickHandler {
                            viewModel.loadArtistData( artist = it)
                            loadCollapsingToolbarBackgroundGradient(it.iconUrl)
                        },
                        null,
                        null,
                        View.OnClickListener {
                            viewModel.loadRelatedArtists(artistToLoad.id)
                        }
                )
        )
    }

    private val view: ArtistView by lazy {
        ArtistView(
                state = viewModel.viewState,
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.lastArtist?.let {
                        if (viewModel.viewState.isSavedAsFavourite.get() == true) {
                            viewModel.deleteFavouriteArtist()
                            Toast.makeText(activity, "${it.name} deleted from favourite artists.", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addFavouriteArtist()
                            Toast.makeText(activity, "${it.name} added to favourite artists.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                artistAdapter = artistAdapter
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
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                {
                    viewModel.loadArtistData( artistToLoad)
                },
                true
        )
    }

    private val artistToLoad: Artist
        get() = if (viewModelInitialized && viewModel.viewState.artist.get() != null) viewModel.viewState.artist.get()!!
        else argArtist

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.artistFavouriteFab.hideAndShow()
        })
        return binding.apply {
            view = this@ArtistFragment.view
            loadCollapsingToolbarBackgroundGradient(argArtist.iconUrl)
            artistRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            artistToolbar.setupWithBackNavigation(appCompatActivity, ::onBackPressed)
        }.root
    }

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) {
            backPressedWithNoPreviousStateHandler?.onBackPressedWithNoPreviousState()
        } else {
            loadCollapsingToolbarBackgroundGradient(viewModel.viewState.artist.get()!!.iconUrl)
        }
    }

    private fun loadCollapsingToolbarBackgroundGradient(
            url: String
    ) = disposablesComponent.add(Picasso.with(context).getBitmapSingle(url, {
        it.generateColorGradient {
            artist_toolbar_gradient_background_view?.background = it
            artist_toolbar_gradient_background_view?.invalidate()
        }
    }))

    private fun loadData() = viewModel.loadArtistData(argArtist)

    companion object {
        private const val ARG_ARTIST = "ARG_ARTIST"

        fun newInstance(artist: Artist) = ArtistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ARTIST, artist)
            }
        }
    }
}
