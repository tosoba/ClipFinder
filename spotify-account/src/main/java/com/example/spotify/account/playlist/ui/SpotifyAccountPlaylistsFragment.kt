package com.example.spotify.account.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.largeTextCenter
import com.example.core.android.model.Initial
import com.example.core.android.model.spotify.clickableListItem
import com.example.core.android.util.ext.NavigationCapable
import com.example.core.android.util.ext.show
import com.example.core.android.util.ext.spotifyLoginController
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.spotify.account.R
import com.example.spotify.account.databinding.FragmentSpotifyAccountPlaylistsBinding
import org.koin.android.ext.android.inject

class SpotifyAccountPlaylistsFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val viewModel: SpotifyAccountPlaylistsViewModel by fragmentViewModel()

    private lateinit var binding: FragmentSpotifyAccountPlaylistsBinding

    private val epoxyController: TypedEpoxyController<SpotifyAccountPlaylistState> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        injectedItemListController(
            SpotifyAccountPlaylistState::playlists,
            loadMore = viewModel::loadPlaylists,
            shouldOverrideBuildModels = { (userLoggedIn, playlists) ->
                !userLoggedIn && playlists.status is Initial
            },
            overrideBuildModels = {
                largeTextCenter {
                    id("spotify-account-playlists-user-not-logged-in")
                    text(getString(R.string.spotify_login_required))
                }
            },
            reloadClicked = viewModel::loadPlaylists,
            buildItem = { playlist ->
                playlist.clickableListItem {
                    show { factory.newSpotifyPlaylistFragment(playlist) }
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireNotNull(spotifyLoginController)
            .isLoggedIn
            .observe(this, Observer { userLoggedIn ->
                viewModel.setUserLoggedIn(userLoggedIn)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifyAccountPlaylistsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spotifyAccountPlaylistsRecyclerView.setController(epoxyController)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)
}
