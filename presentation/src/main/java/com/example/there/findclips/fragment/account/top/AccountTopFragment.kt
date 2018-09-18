package com.example.there.findclips.fragment.account.top

import android.arch.lifecycle.ViewModelProviders
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
import com.example.there.findclips.databinding.FragmentAccountTopBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.account.TracksDataLoaded
import com.example.there.findclips.fragment.album.AlbumAdapter
import com.example.there.findclips.fragment.artist.ArtistFragment
import com.example.there.findclips.fragment.trackvideos.TrackVideosFragment
import com.example.there.findclips.model.entity.Artist
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


class AccountTopFragment : BaseVMFragment<AccountTopViewModel>(), Injectable, TracksDataLoaded {

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(AccountTopViewModel::class.java)
        viewModel.viewState = AccountTopViewState(mainActivity!!.loggedInObservable)
    }

    override val isDataLoaded: Boolean
        get() = viewModelInitialized
                && viewModel.viewState.artists.isNotEmpty()
                && viewModel.viewState.topTracks.isNotEmpty()

    private val view: AccountTopView by lazy {
        AccountTopView(
                viewModel.viewState,
                AlbumAdapter(
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists),
                                object : ListItemView<Artist>(viewModel.viewState.artists) {
                                    override val itemViewBinder: ItemBinder<Artist>
                                        get() = ItemBinderBase(BR.artist, R.layout.artist_item)
                                },
                                ClickHandler {
                                    hostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
                                },
                                null,
                                object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() {
                                        mainActivity?.userReadPrivateAccessTokenEntity?.let {
                                            viewModel.loadArtists(it)
                                        }
                                    }
                                }
                        ),
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.topTracks),
                                object : ListItemView<Track>(viewModel.viewState.topTracks) {
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
        val binding: FragmentAccountTopBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_top, container, false)
        return binding.apply {
            view = this@AccountTopFragment.view
            accountTopRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    private val loginCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.viewState.userLoggedIn.get() == true && !isDataLoaded)
                    loadData()
            }
        }
    }

    private fun loadData() = with(mainActivity!!.userReadPrivateAccessTokenEntity) {
        viewModel.loadTracks(this)
        viewModel.loadArtists(this)
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