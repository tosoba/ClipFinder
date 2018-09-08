package com.example.there.findclips.base.fragment

import android.support.v4.app.Fragment
import com.example.there.findclips.util.ext.mainActivity

abstract class BaseHostFragment : Fragment() {

    abstract val backStackLayoutId: Int

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = childFragmentManager.beginTransaction().replace(backStackLayoutId, fragment)

        if (addToBackStack) {
            mainActivity?.addBackNavigationToToolbar()
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)
}