package com.example.spotify.account.top.ui

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
import com.example.spotify.account.databinding.FragmentSpotifyAccountTopBinding
import org.koin.android.ext.android.inject

class SpotifyAccountTopFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val viewModel: SpotifyAccountTopViewModel by fragmentViewModel()

    private lateinit var binding: FragmentSpotifyAccountTopBinding

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyAccountTopState> { (userLoggedIn, tracks, artists) ->
            fun <Item> Collection<Item>.column(
                buildItem: (Item) -> EpoxyModel<*>
            ): Column = Column(map(buildItem))

            if (!userLoggedIn && tracks.status is Initial && artists.status is Initial) {
                largeTextCenter {
                    id("spotify-account-top-user-not-logged-in")
                    text(getString(R.string.spotify_login_required))
                }
            } else {
                pagedDataListCarouselWithHeader(
                    requireContext(),
                    artists,
                    R.string.artists,
                    "top-artists",
                    viewModel::loadArtists,
                    { it.chunked(2) }
                ) { chunk ->
                    chunk.column { artist ->
                        artist.clickableListItem {
                            show { newSpotifyArtistFragment(artist) }
                        }
                    }
                }

                pagedDataListCarouselWithHeader(
                    requireContext(),
                    tracks,
                    R.string.track_videos,
                    "top-tracks",
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifyAccountTopBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spotifyAccountTopRecyclerView.setController(epoxyController)
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