package com.example.there.findclips.fragment.spotifyitem.album

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentAlbumBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.*
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.adapter.ArtistsAndTracksAdapter
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.squareup.picasso.Picasso

class AlbumFragment : BaseVMFragment<AlbumViewModel>(AlbumViewModel::class.java), Injectable {

    private val album: Album by lazy { arguments!!.getParcelable<Album>(ARG_ALBUM) }

    private val artistsAndTracksAdapter: ArtistsAndTracksAdapter by lazy {
        ArtistsAndTracksAdapter(
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists),
                        object : ListItemView<Artist>(viewModel.viewState.artists) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.artist, R.layout.artist_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener {
                            viewModel.loadAlbumsArtists(album.artists.map { it.id })
                        }
                ),
                RecyclerViewItemView(
                        RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.tracks),
                        object : ListItemView<Track>(viewModel.viewState.tracks) {
                            override val itemViewBinder: ItemBinder<Track>
                                get() = ItemBinderBase(BR.track, R.layout.track_popularity_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
                        },
                        null,
                        null,
                        View.OnClickListener { viewModel.loadTracksFromAlbum(album.id) }
                )
        )
    }

    private val view: AlbumView by lazy {
        AlbumView(
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAlbumBinding>(inflater, R.layout.fragment_album, container, false)
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

    private val disposablesComponent = DisposablesComponent()

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
