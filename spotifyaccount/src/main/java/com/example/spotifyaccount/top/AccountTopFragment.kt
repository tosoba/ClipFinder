package com.example.spotifyaccount.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.android.BR
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.BaseVMFragment
import com.example.core.android.model.spotify.Artist
import com.example.core.android.model.spotify.Track
import com.example.core.android.util.ext.navHostFragment
import com.example.core.android.util.ext.spotifyLoginController
import com.example.core.android.view.recyclerview.adapter.ArtistsAndTracksAdapter
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemViewState
import com.example.core.android.view.recyclerview.listener.ClickHandler
import com.example.core.android.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.spotifyaccount.R
import com.example.spotifyaccount.databinding.FragmentAccountTopBinding
import org.koin.android.ext.android.inject

class AccountTopFragment : BaseVMFragment<AccountTopViewModel>(AccountTopViewModel::class) {

    private val fragmentFactory: IFragmentFactory by inject()

    private val view: AccountTopView by lazy {
        AccountTopView(
            viewModel.viewState,
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
                    onScrollListener = EndlessRecyclerOnScrollListener { viewModel.loadArtists() }
                ),
                RecyclerViewItemView(
                    RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.topTracks, viewModel.viewState.tracksLoadingErrorOccurred),
                    object : ListItemView<Track>(viewModel.viewState.topTracks) {
                        override val itemViewBinder: ItemBinder<Track>
                            get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                    },
                    ClickHandler {
                        navHostFragment?.showFragment(fragmentFactory.newSpotifyTrackVideosFragment(track = it), true)
                    },
                    onScrollListener = EndlessRecyclerOnScrollListener { viewModel.loadTracks() }
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAccountTopBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_top, container, false)
        return binding.apply {
            view = this@AccountTopFragment.view
            accountTopRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.userLoggedIn.observe(this, Observer { userLoggedIn ->
            if (userLoggedIn && viewModel.viewState.artists.isNotEmpty() && viewModel.viewState.topTracks.isNotEmpty()) loadData()
        })
    }

    override fun AccountTopViewModel.onInitialized() {
        viewState = AccountTopViewState(spotifyLoginController!!.isLoggedIn)
    }

    private fun loadData() {
        viewModel.loadTracks()
        viewModel.loadArtists()
    }
}
