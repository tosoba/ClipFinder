package com.example.there.findclips.fragment.account

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseNavHostFragment


class AccountNavHostFragment : BaseNavHostFragment() {

    override val layoutId: Int = R.layout.fragment_account_host

    override val mainFragment: Fragment
        get() = AccountFragment()

    override val backStackLayoutId: Int = R.id.user_back_stack_layout
}
