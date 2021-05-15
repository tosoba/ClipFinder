package com.clipfinder.spotify.account.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.largeTextCenter
import com.clipfinder.core.android.spotify.model.clickableListItem
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.show
import com.clipfinder.core.android.view.epoxy.Column
import com.clipfinder.core.android.view.epoxy.injectedTypedController
import com.clipfinder.core.android.view.epoxy.loadableCarouselWithHeader
import com.clipfinder.core.model.Empty
import com.clipfinder.spotify.account.R
import com.clipfinder.spotify.account.databinding.FragmentSpotifyAccountSavedBinding
import org.koin.android.ext.android.inject

class SpotifyAccountSavedFragment : BaseMvRxFragment() {
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyAccountSavedViewModel by fragmentViewModel()
    private lateinit var binding: FragmentSpotifyAccountSavedBinding

    private val epoxyController: TypedEpoxyController<SpotifyAccountSavedState> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        injectedTypedController { (userLoggedIn, tracks, albums) ->
            fun <Item> Collection<Item>.column(buildItem: (Item) -> EpoxyModel<*>): Column =
                Column(map(buildItem))

            if (!userLoggedIn && tracks is Empty && albums is Empty)
                largeTextCenter {
                    id("spotify-account-saved-user-not-logged-in")
                    text(getString(R.string.spotify_login_required))
                }
            else {
                fun <T> chunkedIntoColumns(collection: Collection<T>): List<List<T>> =
                    collection.chunked(2)

                loadableCarouselWithHeader(
                    requireContext(),
                    albums,
                    R.string.albums,
                    "saved-albums",
                    viewModel::loadAlbums,
                    viewModel::clearAlbumsError,
                    ::chunkedIntoColumns
                ) { chunk ->
                    chunk.column { album ->
                        album.clickableListItem { show { factory.newSpotifyAlbumFragment(album) } }
                    }
                }

                loadableCarouselWithHeader(
                    requireContext(),
                    tracks,
                    R.string.tracks,
                    "saved-tracks",
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentSpotifyAccountSavedBinding.inflate(inflater, container, false)
            .also(::binding::set)
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spotifySavedRecyclerView.setController(epoxyController)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)
}
