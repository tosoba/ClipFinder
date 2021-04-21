package com.clipfinder.spotify.account.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.account.R
import com.clipfinder.spotify.account.databinding.FragmentSpotifyAccountTopBinding
import com.clipfinder.core.android.largeTextCenter
import com.clipfinder.core.model.Empty
import com.clipfinder.core.android.spotify.model.clickableListItem
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.show
import com.clipfinder.core.android.view.epoxy.Column
import com.clipfinder.core.android.view.epoxy.injectedTypedController
import com.clipfinder.core.android.view.epoxy.loadableCarouselWithHeader
import org.koin.android.ext.android.inject

class SpotifyAccountTopFragment : BaseMvRxFragment() {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyAccountTopViewModel by fragmentViewModel()
    private lateinit var binding: FragmentSpotifyAccountTopBinding

    private val epoxyController: TypedEpoxyController<SpotifyAccountTopState> by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SpotifyAccountTopState> { (userLoggedIn, tracks, artists) ->
            fun <Item> Collection<Item>.column(buildItem: (Item) -> EpoxyModel<*>): Column = Column(map(buildItem))

            if (!userLoggedIn && tracks is Empty && artists is Empty) largeTextCenter {
                id("spotify-account-top-user-not-logged-in")
                text(getString(R.string.spotify_login_required))
            } else {
                fun <T> chunkedIntoColumns(collection: Collection<T>): List<List<T>> = collection.chunked(2)

                loadableCarouselWithHeader(
                    requireContext(),
                    artists,
                    R.string.artists,
                    "top-artists",
                    viewModel::loadArtists,
                    viewModel::clearArtistsError,
                    ::chunkedIntoColumns
                ) { chunk ->
                    chunk.column { artist ->
                        artist.clickableListItem {
                            show { factory.newSpotifyArtistFragment(artist) }
                        }
                    }
                }

                loadableCarouselWithHeader(
                    requireContext(),
                    tracks,
                    R.string.track_videos,
                    "top-tracks",
                    viewModel::loadTracks,
                    viewModel::clearTracksError,
                    ::chunkedIntoColumns
                ) { chunk ->
                    chunk.column { track ->
                        track.clickableListItem {
                            show { factory.newSpotifyTrackVideosFragment(track) }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSpotifyAccountTopBinding.inflate(inflater, container, false)
        .also(::binding::set)
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spotifyAccountTopRecyclerView.setController(epoxyController)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)
}
