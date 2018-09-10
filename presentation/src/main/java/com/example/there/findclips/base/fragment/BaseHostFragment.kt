package com.example.there.findclips.base.fragment

import android.support.v4.app.Fragment
import com.example.there.findclips.util.ext.animateHeight
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.mainActivity

abstract class BaseHostFragment : Fragment() {

    abstract val backStackLayoutId: Int

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) = with(childFragmentManager.beginTransaction()) {
        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        replace(backStackLayoutId, fragment)

        if (addToBackStack) {
            if (mainActivity?.toolbar?.height != 0)
                mainActivity?.toolbar?.animateHeight(mainActivity!!.dpToPx(48f).toInt(), 0, 250)
            addToBackStack(null)
        }

        commit()
    }

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)
}