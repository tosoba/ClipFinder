package com.example.there.findclips.base.fragment

import android.support.v4.app.Fragment
import com.example.there.findclips.util.ext.animateHeight
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.mainActivity

abstract class BaseHostFragment : Fragment() {

    abstract val backStackLayoutId: Int

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = childFragmentManager.beginTransaction().replace(backStackLayoutId, fragment)

        if (addToBackStack) {
            if (mainActivity?.toolbar?.height != 0)
                mainActivity?.toolbar?.animateHeight(mainActivity!!.dpToPx(48f).toInt(), 0, 400)
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)
}