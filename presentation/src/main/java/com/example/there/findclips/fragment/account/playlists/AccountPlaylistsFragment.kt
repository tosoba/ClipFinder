package com.example.there.findclips.fragment.account.playlists

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentAccountPlaylistsBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.account.TracksDataLoaded
import com.example.there.findclips.fragment.list.SpotifyPlaylistsFragment
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.util.ext.mainActivity


class AccountPlaylistsFragment :
        BaseVMFragment<AccountPlaylistsViewModel>(AccountPlaylistsViewModel::class.java),
        Injectable,
        TracksDataLoaded {

    override val isDataLoaded: Boolean
        get() = viewModelInitialized && viewModel.viewState.playlists.isNotEmpty()

    private val playlistsFragment: SpotifyPlaylistsFragment
        get() = childFragmentManager.findFragmentById(R.id.account_spotify_playlists_fragment) as SpotifyPlaylistsFragment

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
        val binding: FragmentAccountPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_playlists, container, false)
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
        viewState = AccountPlaylistViewState(mainActivity!!.loggedInObservable)
    }

    private fun loadData() = viewModel.loadPlaylists()
}
