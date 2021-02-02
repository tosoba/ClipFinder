package com.clipfinder.spotify.account.playlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.account.R
import com.clipfinder.spotify.account.databinding.FragmentSpotifyAccountPlaylistsBinding
import com.clipfinder.core.android.LargeTextCenterBindingModel_
import com.clipfinder.core.model.Empty
import com.clipfinder.core.android.spotify.model.clickableListItem
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.show
import com.clipfinder.core.android.view.epoxy.loadableCollectionController
import org.koin.android.ext.android.inject

class SpotifyAccountPlaylistsFragment : BaseMvRxFragment() {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyAccountPlaylistsViewModel by fragmentViewModel()
    private lateinit var binding: FragmentSpotifyAccountPlaylistsBinding

    private val epoxyController: TypedEpoxyController<SpotifyAccountPlaylistState> by lazy(LazyThreadSafetyMode.NONE) {
        loadableCollectionController(
            SpotifyAccountPlaylistState::playlists,
            loadMore = viewModel::loadPlaylists,
            shouldOverrideBuildModels = { (userLoggedIn, playlists) -> !userLoggedIn && playlists is Empty },
            overrideBuildModels = {
                LargeTextCenterBindingModel_()
                    .id("spotify-account-playlists-user-not-logged-in")
                    .text(getString(R.string.spotify_login_required))
                    .spanSizeOverride { _, _, _ ->
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
                    }
                    .addTo(this)
            },
            reloadClicked = viewModel::loadPlaylists,
            clearFailure = viewModel::clearPlaylistsError,
            buildItem = { playlist ->
                playlist.clickableListItem {
                    show { factory.newSpotifyPlaylistFragment(playlist) }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSpotifyAccountPlaylistsBinding.inflate(inflater, container, false)
        .apply {
            spotifyAccountPlaylistsRecyclerView.layoutManager = layoutManagerFor(resources.configuration.orientation)
            binding = this
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spotifyAccountPlaylistsRecyclerView.setController(epoxyController)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.spotifyAccountPlaylistsRecyclerView.layoutManager = layoutManagerFor(newConfig.orientation)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager = GridLayoutManager(
        context,
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
    )
}
