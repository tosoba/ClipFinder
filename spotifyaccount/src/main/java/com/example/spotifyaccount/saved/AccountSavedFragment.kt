package com.example.spotifyaccount.saved

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
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Track
import com.example.core.android.util.ext.navHostFragment
import com.example.core.android.util.ext.spotifyLoginController
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemView
import com.example.core.android.view.recyclerview.item.RecyclerViewItemViewState
import com.example.core.android.view.recyclerview.listener.ClickHandler
import com.example.core.android.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.spotifyaccount.R
import com.example.spotifyaccount.databinding.FragmentAccountSavedBinding
import org.koin.android.ext.android.inject

class AccountSavedFragment : BaseVMFragment<AccountSavedViewModel>(AccountSavedViewModel::class) {

    private val fragmentFactory: IFragmentFactory by inject()

    private val view: AccountSavedView by lazy {
        AccountSavedView(
            viewModel.viewState,
            AccountSavedAdapter(
                RecyclerViewItemView(
                    RecyclerViewItemViewState(
                        viewModel.viewState.albumsLoadingInProgress,
                        viewModel.viewState.albums,
                        viewModel.viewState.albumsLoadingErrorOccurred
                    ),
                    object : ListItemView<Album>(viewModel.viewState.albums) {
                        override val itemViewBinder: ItemBinder<Album>
                            get() = ItemBinderBase(
                                BR.imageListItem,
                                com.example.core.android.R.layout.named_image_list_item
                            )
                    },
                    ClickHandler {
                        navHostFragment?.showFragment(
                            fragmentFactory.newSpotifyAlbumFragment(album = it),
                            true
                        )
                    },
                    onScrollListener = EndlessRecyclerOnScrollListener { viewModel.loadAlbums() }
                ),
                RecyclerViewItemView(
                    RecyclerViewItemViewState(
                        viewModel.viewState.tracksLoadingInProgress,
                        viewModel.viewState.tracks,
                        viewModel.viewState.tracksLoadingErrorOccurred
                    ),
                    object : ListItemView<Track>(viewModel.viewState.tracks) {
                        override val itemViewBinder: ItemBinder<Track>
                            get() = ItemBinderBase(
                                BR.imageListItem,
                                com.example.core.android.R.layout.named_image_list_item
                            )
                    },
                    ClickHandler {
                        navHostFragment?.showFragment(
                            fragmentFactory.newSpotifyTrackVideosFragment(track = it),
                            true
                        )
                    },
                    onScrollListener = EndlessRecyclerOnScrollListener { viewModel.loadTracks() }
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.userLoggedIn.observe(this, Observer { userLoggedIn ->
            if (userLoggedIn
                && viewModel.viewState.albums.isNotEmpty()
                && viewModel.viewState.tracks.isNotEmpty()) loadData()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAccountSavedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_saved, container, false)
        return binding.apply {
            view = this@AccountSavedFragment.view
            accountSavedRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }.root
    }

    override fun AccountSavedViewModel.onInitialized() {
        viewState = AccountSavedViewState(spotifyLoginController!!.isLoggedIn)
    }

    private fun loadData() {
        viewModel.loadTracks()
        viewModel.loadAlbums()
    }
}
