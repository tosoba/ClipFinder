package com.example.spotifyplaylist

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.playlist.PlaylistView
import com.example.coreandroid.base.playlist.PlaylistViewState
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.isEmptyAndLastLoadingFailed
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.epoxy.itemListController
import com.example.spotifyplaylist.databinding.FragmentPlaylistBinding
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class PlaylistFragment : BaseMvRxFragment(), NavigationCapable {

    private val playlist: Playlist by args()

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    //TODO: scroll listener
    private val epoxyController by lazy {
        itemListController(builder, differ, viewModel,
                PlaylistViewState<Playlist, Track>::tracks, "Tracks",
                reloadClicked = viewModel::loadTracks
        ) {
            it.clickableListItem { show { newSpotifyTrackVideosFragment(it) } }
        }
    }

    override val factory: IFragmentFactory by inject()

    private val viewModel: PlaylistViewModel by fragmentViewModel()

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(viewModel::loadTracks) {
            withState(viewModel) { state -> state.tracks.isEmptyAndLastLoadingFailed() }
        }
    }

    private val view: PlaylistView<Playlist> by lazy {
        PlaylistView(
                playlist = playlist,
                onFavouriteBtnClickListener = View.OnClickListener { viewModel.togglePlaylistFavouriteState() }
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPlaylistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false)
        enableSpotifyPlayButton { loadPlaylist(playlist) }
        return binding.apply {
            view = this@PlaylistFragment.view
            playlistToolbarGradientBackgroundView.loadBackgroundGradient(playlist.iconUrl, disposablesComponent)
            spotifyPlaylistRecyclerView.apply {
                setController(epoxyController)
                layoutManager = GridLayoutManager(context,
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3)
                setItemSpacingDp(5)
                //TODO: animation

                //TODO: change layout manager onConfigurationChanged (same in SoundCloud) + use grid layout items
            }
            playlistToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(this, PlaylistViewState<Playlist, Track>::isSavedAsFavourite) {
            playlist_favourite_fab?.setImageDrawable(ContextCompat.getDrawable(view.context,
                    if (it.value) R.drawable.delete else R.drawable.favourite))
            playlist_favourite_fab?.hideAndShow()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    companion object {
        fun newInstance(playlist: Playlist) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, playlist)
            }
        }
    }
}