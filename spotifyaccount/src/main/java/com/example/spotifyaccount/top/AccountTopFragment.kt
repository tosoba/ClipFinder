package com.example.spotifyaccount.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coreandroid.BR
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.util.ext.navHostFragment
import com.example.coreandroid.util.ext.spotifyLoginController
import com.example.coreandroid.view.recyclerview.adapter.ArtistsAndTracksAdapter
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemView
import com.example.coreandroid.view.recyclerview.item.RecyclerViewItemViewState
import com.example.coreandroid.view.recyclerview.listener.ClickHandler
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.spotifyaccount.R
import com.example.spotifyaccount.TracksDataLoaded
import com.example.spotifyaccount.databinding.FragmentAccountTopBinding
import org.koin.android.ext.android.inject


class AccountTopFragment : BaseVMFragment<AccountTopViewModel>(AccountTopViewModel::class),
        TracksDataLoaded {

    private val fragmentFactory: IFragmentFactory by inject()

    override val isDataLoaded: Boolean
        get() = viewModel.viewState.artists.isNotEmpty()
                && viewModel.viewState.topTracks.isNotEmpty()

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

    private val loginCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.viewState.userLoggedIn.get() == true && !isDataLoaded) loadData()
            }
        }
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
