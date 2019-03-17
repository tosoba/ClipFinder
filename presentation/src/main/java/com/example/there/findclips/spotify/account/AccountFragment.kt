package com.example.there.findclips.spotify.account

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.there.findclips.R
import com.example.there.findclips.spotify.account.playlists.AccountPlaylistsFragment
import com.example.there.findclips.spotify.account.saved.AccountSavedFragment
import com.example.there.findclips.spotify.account.top.AccountTopFragment
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.coreandroid.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment(), com.example.coreandroid.base.fragment.HasMainToolbar {

    override val toolbar: Toolbar
        get() = account_toolbar

    private val onTabSelectedListener: TabLayout.OnTabSelectedListener by lazy {
        object : com.example.coreandroid.view.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                account_view_pager?.currentItem = tab.position
            }
        }
    }

    private val viewPagerAdapter: FragmentStatePagerAdapter by lazy {
        TitledCustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
                "Playlists" to AccountPlaylistsFragment(), "Saved" to AccountSavedFragment(), "Top" to AccountTopFragment()
        ))
    }

    private val view: AccountView by lazy(LazyThreadSafetyMode.NONE) {
        AccountView(onTabSelectedListener, viewPagerAdapter, 2)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        return binding.apply {
            view = this@AccountFragment.view
            accountTabLayout.setupWithViewPager(accountViewPager)
            appCompatActivity?.setSupportActionBar(accountToolbar)
            appCompatActivity?.showDrawerHamburger()
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(
            item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false
}
