package com.example.spotify.account.saved.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.largeTextCenter
import com.example.core.android.model.Initial
import com.example.core.android.model.spotify.clickableListItem
import com.example.core.android.util.ext.NavigationCapable
import com.example.core.android.util.ext.show
import com.example.core.android.util.ext.spotifyAuthController
import com.example.core.android.view.epoxy.Column
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.pagedDataListCarouselWithHeader
import com.example.spotify.account.R
import com.example.spotify.account.databinding.FragmentSpotifyAccountSavedBinding
import org.koin.android.ext.android.inject

class SpotifyAccountSavedFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val viewModel: SpotifyAccountSavedViewModel by fragmentViewModel()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyAccountSavedState> { (userLoggedIn, tracks, albums) ->
            fun <Item> Collection<Item>.column(
                buildItem: (Item) -> EpoxyModel<*>
            ): Column = Column(map(buildItem))

            if (!userLoggedIn && tracks.status is Initial && albums.status is Initial) {
                largeTextCenter {
                    id("spotify-account-saved-user-not-logged-in")
                    text(getString(R.string.spotify_login_required))
                }
            } else {
                pagedDataListCarouselWithHeader(
                    requireContext(),
                    albums,
                    R.string.albums,
                    "saved-albums",
                    viewModel::loadAlbums,
                    { it.chunked(2) }
                ) { chunk ->
                    chunk.column { album ->
                        album.clickableListItem {
                            show { newSpotifyAlbumFragment(album) }
                        }
                    }
                }

                pagedDataListCarouselWithHeader(
                    requireContext(),
                    tracks,
                    R.string.tracks,
                    "saved-tracks",
                    viewModel::loadTracks,
                    { it.chunked(2) }
                ) { chunk ->
                    chunk.column { track ->
                        track.clickableListItem {
                            show { newSpotifyTrackVideosFragment(track) }
                        }
                    }
                }
            }
        }
    }

    private lateinit var binding: FragmentSpotifyAccountSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifyAccountSavedBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spotifySavedRecyclerView.setController(epoxyController)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireNotNull(spotifyAuthController)
            .isLoggedIn
            .observe(this, Observer { userLoggedIn ->
                viewModel.setUserLoggedIn(userLoggedIn)
            })
    }
}
