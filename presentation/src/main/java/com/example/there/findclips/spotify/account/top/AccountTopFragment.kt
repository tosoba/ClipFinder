package com.example.there.findclips.spotify.account.top

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
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.spotify.account.TracksDataLoaded
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.spotify.trackvideos.TrackVideosFragment
import com.example.there.findclips.util.ext.navHostFragment
import com.example.there.findclips.util.ext.spotifyLoginController
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.adapter.ArtistsAndTracksAdapter
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener


class AccountTopFragment : BaseVMFragment<AccountTopViewModel>(
        AccountTopViewModel::class.java
), Injectable, TracksDataLoaded {

    override val isDataLoaded: Boolean
        get() = viewModelInitialized
                && viewModel.viewState.artists.isNotEmpty()
                && viewModel.viewState.topTracks.isNotEmpty()

    private val view: AccountTopView by lazy {
        AccountTopView(
                viewModel.viewState,
                ArtistsAndTracksAdapter(
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.artistsLoadingInProgress, viewModel.viewState.artists, viewModel.viewState.artistsLoadingErrorOccurred),
                                object : ListItemView<Artist>(viewModel.viewState.artists) {
                                    override val itemViewBinder: ItemBinder<Artist>
                                        get() = ItemBinderBase(BR.artist, R.layout.artist_item)
                                },
                                ClickHandler {
                                    navHostFragment?.showFragment(ArtistFragment.newInstance(artist = it), true)
                                },
                                onScrollListener = object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() = viewModel.loadArtists()
                                }
                        ),
                        RecyclerViewItemView(
                                RecyclerViewItemViewState(viewModel.viewState.tracksLoadingInProgress, viewModel.viewState.topTracks, viewModel.viewState.tracksLoadingErrorOccurred),
                                object : ListItemView<Track>(viewModel.viewState.topTracks) {
                                    override val itemViewBinder: ItemBinder<Track>
                                        get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_list_item)
                                },
                                ClickHandler {
                                    navHostFragment?.showFragment(TrackVideosFragment.newInstance(track = it), true)
                                },
                                onScrollListener = object : EndlessRecyclerOnScrollListener() {
                                    override fun onLoadMore() = viewModel.loadTracks()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAccountTopBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_top, container, false)
        return binding.apply {
            view = this@AccountTopFragment.view
            accountTopRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.userLoggedIn, loginCallback))
    }

    override fun AccountTopViewModel.onInitialized() {
        viewState = AccountTopViewState(spotifyLoginController!!.loggedInObservable)
    }

    private fun loadData() {
        viewModel.loadTracks()
        viewModel.loadArtists()
    }
}
