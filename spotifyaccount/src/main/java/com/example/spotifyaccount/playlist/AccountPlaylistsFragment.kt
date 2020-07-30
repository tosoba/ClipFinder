package com.example.spotifyaccount.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.core.android.base.fragment.BaseVMFragment
import com.example.core.android.util.ext.spotifyLoginController
import com.example.itemlist.spotify.SpotifyPlaylistsFragment
import com.example.spotifyaccount.R
import com.example.spotifyaccount.databinding.FragmentAccountPlaylistsBinding

class AccountPlaylistsFragment : BaseVMFragment<AccountPlaylistsViewModel>(AccountPlaylistsViewModel::class) {

    private val playlistsFragment: SpotifyPlaylistsFragment
        get() = childFragmentManager.findFragmentById(R.id.account_spotify_playlists_fragment) as SpotifyPlaylistsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.userLoggedIn.observe(this, Observer { userLoggedIn ->
            if (userLoggedIn && viewModel.viewState.playlists.isNotEmpty()) loadData()
        })
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
        viewState = AccountPlaylistViewState(spotifyLoginController!!.isLoggedIn)
    }

    private fun loadData() = viewModel.loadPlaylists()
}
