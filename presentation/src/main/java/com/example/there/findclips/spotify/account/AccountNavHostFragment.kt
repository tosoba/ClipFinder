package com.example.there.findclips.spotify.account

import android.support.v4.app.Fragment
import com.example.there.findclips.R


class AccountNavHostFragment : com.example.coreandroid.base.fragment.BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_account_host

    override val mainFragment: Fragment
        get() = AccountFragment()

    override val backStackLayoutId: Int = R.id.user_back_stack_layout
}
