package com.example.spotifyaccount.saved

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.navHostFragment
import com.example.coreandroid.util.ext.spotifyLoginController
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.spotifyaccount.R
import com.example.spotifyaccount.databinding.FragmentAccountSavedBinding

class AccountSavedFragment :
        com.example.coreandroid.base.fragment.BaseVMFragment<com.example.spotifyaccount.saved.AccountSavedViewModel>(com.example.spotifyaccount.saved.AccountSavedViewModel::class.java),
        com.example.coreandroid.di.Injectable,
        com.example.spotifyaccount.TracksDataLoaded {

    override val isDataLoaded: Boolean
        get() = viewModelInitialized
                && viewModel.viewState.albums.isNotEmpty()
                && viewModel.viewState.tracks.isNotEmpty()

    private val view: com.example.spotifyaccount.saved.AccountSavedView by lazy {
        com.example.spotifyaccount.saved.AccountSavedView(
                viewModel.viewState,
                AccountSavedAdapter(
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.albumsLoadingInProgress, viewModel.viewState.albums, viewModel.viewState.albumsLoadingErrorOccurred),
                                object : ListItemView<Album>(viewModel.viewState.albums) {
                                    override val itemViewBinder: ItemBinder<Album>
                                        get() = ItemBinderBase(BR.imageListItem, com.example.coreandroid.R.layout.named_image_list_item)
                                },
                                ClickHandler {
                                    navHostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
                                },
                                onScrollListener = object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() = viewModel.loadAlbums()
                                }
                        ),
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.tracks, viewModel.viewState.tracksLoadingErrorOccurred),
                                object : ListItemView<Track>(viewModel.viewState.tracks) {
                                    override val itemViewBinder: ItemBinder<Track>
                                        get() = ItemBinderBase(BR.imageListItem, com.example.coreandroid.R.layout.named_image_list_item)
                                },
                                ClickHandler {
                                    navHostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
                                },
                                onScrollListener = object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() {
                                        viewModel.loadTracks()
                                    }
                                }
                        )
                )
        )
    }

    private val loginCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.viewState.userLoggedIn.get() == true && !isDataLoaded)
                    loadData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.userLoggedIn, loginCallback))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: com.example.spotifyaccount.databinding.FragmentAccountSavedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_saved, container, false)
        return binding.apply {
            view = this@AccountSavedFragment.view
            accountSavedRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun AccountSavedViewModel.onInitialized() {
        viewState = AccountSavedViewState(spotifyLoginController!!.loggedInObservable)
    }

    private fun loadData() {
        viewModel.loadTracks()
        viewModel.loadAlbums()
    }
}