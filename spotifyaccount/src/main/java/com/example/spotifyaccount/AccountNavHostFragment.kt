package com.example.spotifyaccount

import androidx.fragment.app.Fragment
import com.example.coreandroid.base.fragment.BaseNavHostFragment

class AccountNavHostFragment : BaseNavHostFragment() {
    override val layoutId: Int = R.layout.fragment_account_host
    override val mainFragment: Fragment get() = AccountFragment()
    override val backStackLayoutId: Int = R.id.user_back_stack_layout
}
