package com.clipfinder.core.android.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class BaseNavHostFragment : Fragment() {
    protected abstract val backStackLayoutId: Int
    protected abstract val layoutId: Int
    protected abstract val mainFragment: Fragment

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(layoutId, container, false).apply { showFragment(mainFragment, false) }

    fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) =
        with(childFragmentManager.beginTransaction()) {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace(backStackLayoutId, fragment)
            if (addToBackStack) addToBackStack(null)
            commit()
        }

    fun popAll() {
        childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
