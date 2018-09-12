package com.example.there.findclips.base.fragment

import android.support.v4.app.Fragment

abstract class BaseHostFragment : Fragment() {

    abstract val backStackLayoutId: Int

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) = with(childFragmentManager.beginTransaction()) {
        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        replace(backStackLayoutId, fragment)
        if (addToBackStack) addToBackStack(null)
        commit()
    }

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)
}