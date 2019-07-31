package com.example.coreandroid.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseNavHostFragment : Fragment() {

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)

    protected abstract val backStackLayoutId: Int

    protected abstract val layoutId: Int

    protected abstract val mainFragment: Fragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false).apply {
        showFragment(mainFragment, false)
    }

    fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) = with(childFragmentManager.beginTransaction()) {
        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        replace(backStackLayoutId, fragment)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}