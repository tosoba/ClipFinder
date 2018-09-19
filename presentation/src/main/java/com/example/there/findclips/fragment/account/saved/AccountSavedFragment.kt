package com.example.there.findclips.fragment.account.saved

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentAccountSavedBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.account.TracksDataLoaded
import com.example.there.findclips.fragment.album.AlbumFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener


class AccountSavedFragment :
        BaseVMFragment<AccountSavedViewModel>(AccountSavedViewModel::class.java),
        Injectable,
        TracksDataLoaded {

    override val onViewModelInitialized: (() -> Unit)? = {
        viewModel.viewState = AccountSavedViewState(mainActivity!!.loggedInObservable)
    }

    override val isDataLoaded: Boolean
        get() = viewModelInitialized
                && viewModel.viewState.albums.isNotEmpty()
                && viewModel.viewState.tracks.isNotEmpty()

    private val view: AccountSavedView by lazy {
        AccountSavedView(
                viewModel.viewState,
                AccountSavedAdapter(
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.albumsLoadingInProgress, viewModel.viewState.albums),
                                object : ListItemView<Album>(viewModel.viewState.albums) {
                                    override val itemViewBinder: ItemBinder<Album>
                                        get() = ItemBinderBase(BR.album, R.layout.album_item)
                                },
                                ClickHandler {
                                    hostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
                                },
                                null,
                                object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() {
                                        mainActivity?.userReadPrivateAccessTokenEntity?.let {
                                            viewModel.loadAlbums(it)
                                        }
                                    }
                                }
                        ),
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.tracks),
                                object : ListItemView<Track>(viewModel.viewState.tracks) {
                                    override val itemViewBinder: ItemBinder<Track>
                                        get() = ItemBinderBase(BR.track, R.layout.track_item)
                                },
                                ClickHandler {
                                    hostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
                                },
                                null,
                                object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() {
                                        mainActivity?.userReadPrivateAccessTokenEntity?.let {
                                            viewModel.loadTracks(it)
                                        }
                                    }
                                }
                        )
                )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAccountSavedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_saved, container, false)
        return binding.apply {
            view = this@AccountSavedFragment.view
            accountSavedRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    private fun loadData() = with(mainActivity!!.userReadPrivateAccessTokenEntity) {
        viewModel.loadTracks(this)
        viewModel.loadAlbums(this)
    }

    private val loginCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.viewState.userLoggedIn.get() == true && !isDataLoaded)
                    loadData()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewState.userLoggedIn.addOnPropertyChangedCallback(loginCallback)
    }

    override fun onStop() {
        super.onStop()
        viewModel.viewState.userLoggedIn.addOnPropertyChangedCallback(loginCallback)
    }
}
