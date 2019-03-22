package com.example.spotifyalbum

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.recyclerview.adapter.ArtistsAndTracksAdapter
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.squareup.picasso.Picasso
import javax.inject.Inject
import com.example.coreandroid.R
import com.example.coreandroid.BR
import com.example.spotifyalbum.databinding.FragmentAlbumBinding


class AlbumFragment : BaseVMFragment<AlbumViewModel>(AlbumViewModel::class.java), Injectable {

    @Inject
    lateinit var fragmentFactory: IFragmentFactory

    private val album: Album by lazy { arguments!!.getParcelable<Album>(ARG_ALBUM) }

    private val artistsAndTracksAdapter: ArtistsAndTracksAdapter by lazy {
        ArtistsAndTracksAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists, viewModel.viewState.artistsLoadingErrorOccurred),
                        object : ListItemView<Artist>(viewModel.viewState.artists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(fragmentFactory.newSpotifyArtistFragment(artist = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener {
                            viewModel.loadAlbumsArtists(album.artists.map { it.id })
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.tracks, viewModel.viewState.tracksLoadingErrorOccurred),
                        object : ListItemView<Track>(viewModel.viewState.tracks) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.track, R.layout.track_popularity_item)
                        },
                        ClickHandler {
                            navHostFragment?.showFragment(fragmentFactory.newSpotifyTrackVideosFragment(track = it), true)
                        },
                        onReloadBtnClickListener = View.OnClickListener { viewModel.loadTracksFromAlbum(album.id) }
                )
        )
    }

    private val view: com.example.spotifyalbum.AlbumView by lazy {
        com.example.spotifyalbum.AlbumView(
                state = viewModel.viewState,
                album = album,
                onFavouriteBtnClickListener = View.OnClickListener {
                    if (viewModel.viewState.isSavedAsFavourite.get() == true) {
                        viewModel.deleteFavouriteAlbum(album)
                        Toast.makeText(activity, "${album.name} deleted from favourite albums", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addFavouriteAlbum(album)
                        Toast.makeText(activity, "${album.name} added to favourite albums.", Toast.LENGTH_SHORT).show()
                    }
                },
                onPlayBtnClickListener = View.OnClickListener {
                    val playAlbum: () -> Unit = { spotifyPlayerController?.loadAlbum(album) }
                    if (spotifyPlayerController?.isPlayerLoggedIn == true) {
                        playAlbum()
                    } else {
                        spotifyLoginController?.showLoginDialog()
                        spotifyLoginController?.onLoginSuccessful = playAlbum
                    }
                },
                artistsAndTracksAdapter = artistsAndTracksAdapter
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.viewState.tracks.isNotEmpty() && viewModel.viewState.artists.isNotEmpty() },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<com.example.spotifyalbum.databinding.FragmentAlbumBinding>(inflater, com.example.spotifyalbum.R.layout.fragment_album, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.albumFavouriteFab.hideAndShow()
        })
        return binding.apply {
            view = this@AlbumFragment.view
            disposablesComponent.add(Picasso.with(context).getBitmapSingle(album.iconUrl, {
                it.generateColorGradient {
                    albumToolbarGradientBackgroundView.background = it
                    albumToolbarGradientBackgroundView.invalidate()
                }
            }))
            albumRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            albumToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private fun loadData() = viewModel.loadAlbumData(album)

    companion object {
        private const val ARG_ALBUM = "ARG_ALBUM"

        fun newInstance(album: Album) = AlbumFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_ALBUM, album) }
        }
    }
}
