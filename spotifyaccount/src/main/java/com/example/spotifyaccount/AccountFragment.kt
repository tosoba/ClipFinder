package com.example.spotifyaccount

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.core.android.base.fragment.HasMainToolbar
import com.example.core.android.base.handler.NavigationDrawerController
import com.example.core.android.util.ext.mainContentFragment
import com.example.core.android.util.ext.showDrawerHamburger
import com.example.core.android.view.binding.viewBinding
import com.example.core.android.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import com.example.core.ext.castAs
import com.example.spotifyaccount.databinding.FragmentAccountBinding
import com.example.spotifyaccount.playlist.AccountPlaylistsFragment
import com.example.spotifyaccount.saved.AccountSavedFragment
import com.example.spotifyaccount.top.ui.AccountTopFragment
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment(R.layout.fragment_account), HasMainToolbar {

    private val binding: FragmentAccountBinding by viewBinding(FragmentAccountBinding::bind)
    override val toolbar: Toolbar get() = account_toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainContentFragment?.disablePlayButton()
        with(binding) {
            requireActivity().castAs<AppCompatActivity>()?.apply {
                setSupportActionBar(accountToolbar)
                showDrawerHamburger()
            }
            val titledFragments: Array<Pair<String, Fragment>> = arrayOf(
                getString(R.string.playlists) to AccountPlaylistsFragment(),
                getString(R.string.saved) to AccountSavedFragment(),
                getString(R.string.top) to AccountTopFragment()
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

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean = if (
        item.itemId == android.R.id.home
        && parentFragment?.childFragmentManager?.backStackEntryCount == 0
    ) {
        activity?.castAs<NavigationDrawerController>()?.openDrawer()
        true
    } else false
}
