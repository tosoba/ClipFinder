package com.example.there.findclips.fragment.account.playlists

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentAccountPlaylistsBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.account.TracksDataLoaded
import com.example.there.findclips.fragment.list.SpotifyPlaylistsFragment
import com.example.there.findclips.util.ext.mainActivity


class AccountPlaylistsFragment : BaseSpotifyVMFragment<AccountPlaylistsViewModel>(), Injectable, TracksDataLoaded {

    override val isDataLoaded: Boolean
        get() = viewModelInitialized && viewModel.viewState.playlists.isNotEmpty()

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(AccountPlaylistsViewModel::class.java)
        viewModel.viewState = AccountPlaylistViewState(mainActivity!!.loggedInObservable)
    }

    private val playlistsFragment: SpotifyPlaylistsFragment
        get() = childFragmentManager.findFragmentById(R.id.account_spotify_playlists_fragment) as SpotifyPlaylistsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentAccountPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_playlists, container, false)
        return binding.apply { viewState = viewModel.viewState }.root
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loadedFlag.observe(this, Observer { _ ->
            playlistsFragment.updateItems(viewModel.viewState.playlists, false)
            if (playlistsFragment.loadMore == null) playlistsFragment.loadMore = ::loadData
        })
    }

    private val loginCallback: Observable.OnPropertyChangedCallback by lazy {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.viewState.userLoggedIn.get() == true && !isDataLoaded)
                    loadData()
            }
        }
    }

    private fun loadData() = viewModel.loadPlaylists(mainActivity!!.userReadPrivateAccessTokenEntity)

    override fun onStart() {
        super.onStart()
        viewModel.viewState.userLoggedIn.addOnPropertyChangedCallback(loginCallback)
    }

    override fun onStop() {
        super.onStop()
        viewModel.viewState.userLoggedIn.addOnPropertyChangedCallback(loginCallback)
    }
}
