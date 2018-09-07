package com.example.there.findclips.base.fragment

import android.support.v4.app.Fragment

abstract class BaseHostFragment : Fragment() {
    abstract val backStackLayoutId: Int

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = childFragmentManager.beginTransaction().replace(backStackLayoutId, fragment)

        if (addToBackStack) transaction.addToBackStack(null)

        transaction.commit()
    }
}