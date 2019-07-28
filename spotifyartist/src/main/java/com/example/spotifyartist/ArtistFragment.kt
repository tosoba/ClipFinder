package com.example.spotifyartist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.base.fragment.GoesToPreviousStateOnBackPressed
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
import kotlinx.android.synthetic.main.fragment_artist.*
import org.koin.android.ext.android.inject

class ArtistFragment :
        BaseVMFragment<ArtistViewModel>(ArtistViewModel::class),
        GoesToPreviousStateOnBackPressed {

    private val fragmentFactory: IFragmentFactory by inject()

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
                            viewModel.loadArtistData(artist = it)
                            artist_toolbar_gradient_background_view?.loadBackgroundGradient(it.iconUrl, disposablesComponent)
                        },
                        onReloadBtnClickListener = View.OnClickListener {
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
        reloadingConnectivityComponent({ viewModel.loadArtistData(artistToLoad) }) {
            viewModel.viewState.albums.isEmpty() ||
                    viewModel.viewState.artist.get() == null ||
                    viewModel.viewState.topTracks.isEmpty() ||
                    viewModel.viewState.topTracks.isEmpty()
        }
    }

    private val artistToLoad: Artist
        get() = if (viewModel.viewState.artist.get() != null) viewModel.viewState.artist.get()!!
        else argArtist

    private val disposablesComponent = DisposablesComponent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.artistFavouriteFab.hideAndShow()
        })
        mainContentFragment?.disablePlayButton()
        return binding.apply {
            view = this@ArtistFragment.view
            artistToolbarGradientBackgroundView.loadBackgroundGradient(argArtist.iconUrl, disposablesComponent)
            artistRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
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
        if (!viewModel.onBackPressed()) backPressedWithNoPreviousStateController?.onBackPressedWithNoPreviousState()
        else viewModel.viewState.artist.get()?.iconUrl?.let {
            artist_toolbar_gradient_background_view?.loadBackgroundGradient(it, disposablesComponent)
        }
    }

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
