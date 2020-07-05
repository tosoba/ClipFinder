package com.example.spotifyaccount.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.util.ext.spotifyLoginController
import com.example.itemlist.spotify.SpotifyPlaylistsFragment
import com.example.spotifyaccount.R
import com.example.spotifyaccount.TracksDataLoaded
import com.example.spotifyaccount.databinding.FragmentAccountPlaylistsBinding

class AccountPlaylistsFragment :
    BaseVMFragment<AccountPlaylistsViewModel>(AccountPlaylistsViewModel::class),
    TracksDataLoaded {

    override val isDataLoaded: Boolean
        get() = viewModel.viewState.playlists.isNotEmpty()

    private val playlistsFragment: SpotifyPlaylistsFragment
        get() = childFragmentManager.findFragmentById(R.id.account_spotify_playlists_fragment) as SpotifyPlaylistsFragment

    private val loginCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.viewState.userLoggedIn.get() == true && !isDataLoaded) loadData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.userLoggedIn, loginCallback))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAccountPlaylistsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_account_playlists, container, false
        )
        return binding.apply { viewState = viewModel.viewState }.root
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loadedFlag.observe(this, Observer {
            playlistsFragment.updateItems(viewModel.viewState.playlists, false)
            if (playlistsFragment.loadMore == null) playlistsFragment.loadMore = ::loadData
        })
    }

    override fun AccountPlaylistsViewModel.onInitialized() {
        viewState = AccountPlaylistViewState(spotifyLoginController!!.loggedInObservable)
    }

    private fun loadData() = viewModel.loadPlaylists()
}
