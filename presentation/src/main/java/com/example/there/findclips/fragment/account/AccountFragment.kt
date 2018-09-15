package com.example.there.findclips.fragment.account

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.HasMainToolbar
import com.example.there.findclips.databinding.FragmentAccountBinding
import com.example.there.findclips.fragment.account.playlists.AccountPlaylistsFragment
import com.example.there.findclips.fragment.account.saved.AccountSavedFragment
import com.example.there.findclips.fragment.account.top.AccountTopFragment
import com.example.there.findclips.util.ext.mainActivity
import com.example.there.findclips.view.OnTabSelectedListener
import com.example.there.findclips.view.viewpager.adapter.TitledCustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment(), HasMainToolbar {

    override val toolbar: Toolbar
        get() = account_toolbar

    private val onTabSelectedListener: TabLayout.OnTabSelectedListener by lazy {
        object : OnTabSelectedListener {
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
            mainActivity?.setSupportActionBar(accountToolbar)
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false
}
