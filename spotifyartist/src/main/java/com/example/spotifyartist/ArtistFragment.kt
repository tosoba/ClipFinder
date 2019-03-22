package com.example.spotifyartist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.spotifyartist.databinding.FragmentArtistBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_artist.*
import javax.inject.Inject

class ArtistFragment :
        BaseVMFragment<ArtistViewModel>(ArtistViewModel::class.java),
        Injectable,
        GoesToPreviousStateOnBackPressed {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

    private val argArtist: Artist by lazy { arguments!!.getParcelable<Artist>(ARG_ARTIST) }

    private val artistAdapter: ArtistAdapter by lazy {
        ArtistAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.albumsLoadingInProgress, viewModel.viewState.albums, viewModel.viewState.albumsLoadingErrorOccurred),
                        object : ListItemView<Album>(viewModel.viewState.albums) {
                            override val itemViewBinder: ItemBinder<Album>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(fragmentFactory.newSpotifyAlbumFragment(album = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener {
                            viewModel.loadAlbumsFromArtist(artistToLoad.id)
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.topTracksLoadingInProgress, viewModel.viewState.topTracks, viewModel.viewState.topTracksLoadingErrorOccurred),
                        object : ListItemView<Track>(viewModel.viewState.topTracks) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(fragmentFactory.newSpotifyTrackVideosFragment(track = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener {
                            viewModel.loadTopTracksFromArtist(artistToLoad.id)
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.relatedArtistsLoadingInProgress, viewModel.viewState.relatedArtists, viewModel.viewState.relatedArtistsLoadingErrorOccurred),
                        object : ListItemView<Artist>(viewModel.viewState.relatedArtists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            viewModel.loadArtistData( artist = it)
                            loadCollapsingToolbarBackgroundGradient(it.iconUrl)
                        },
                        onReloadBtnClickListener = View.OnClickListener {
                            viewModel.loadRelatedArtists(artistToLoad.id)
                        }
                )
        )
    }

    private val view: com.example.spotifyartist.ArtistView by lazy {
        com.example.spotifyartist.ArtistView(
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

    private val disposablesComponent = DisposablesComponent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: com.example.spotifyartist.databinding.FragmentArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) {
            backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
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
