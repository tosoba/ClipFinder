package com.clipfinder.spotify.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.clipfinder.core.ext.castAs
import com.clipfinder.spotify.account.databinding.FragmentSpotifyAccountBinding
import com.clipfinder.spotify.account.playlist.SpotifyAccountPlaylistsFragment
import com.clipfinder.spotify.account.saved.SpotifyAccountSavedFragment
import com.clipfinder.spotify.account.top.SpotifyAccountTopFragment
import com.clipfinder.core.android.base.fragment.HasMainToolbar
import com.clipfinder.core.android.base.handler.NavigationDrawerController
import com.clipfinder.core.android.util.ext.mainContentFragment
import com.clipfinder.core.android.util.ext.showDrawerHamburger
import com.clipfinder.core.android.view.binding.viewBinding
import com.clipfinder.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter

class SpotifyAccountFragment : Fragment(R.layout.fragment_spotify_account), HasMainToolbar {
    private val binding: FragmentSpotifyAccountBinding by viewBinding(FragmentSpotifyAccountBinding::bind)
    override val toolbar: Toolbar get() = binding.accountToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainContentFragment?.disablePlayButton()
        with(binding) {
            requireActivity().castAs<AppCompatActivity>()?.apply {
                setSupportActionBar(accountToolbar)
                showDrawerHamburger()
            }
            val titledFragments: Array<Pair<String, Fragment>> = arrayOf(
                getString(R.string.playlists) to SpotifyAccountPlaylistsFragment(),
                getString(R.string.saved) to SpotifyAccountSavedFragment(),
                getString(R.string.top) to SpotifyAccountTopFragment()
            )
            accountViewPager.adapter = TitledCustomCurrentStatePagerAdapter(
                childFragmentManager, titledFragments
            )
            accountViewPager.offscreenPageLimit = titledFragments.size - 1
            accountTabLayout.setupWithViewPager(accountViewPager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (toolbar.menu?.size() == 0) {
            activity?.castAs<AppCompatActivity>()?.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = if (
        item.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0
    ) {
        activity?.castAs<NavigationDrawerController>()?.openDrawer()
        true
    } else false
}
