package com.example.there.findclips.base.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseNavHostFragment : Fragment() {

    val topFragment: Fragment?
        get() = childFragmentManager.findFragmentById(backStackLayoutId)

    protected abstract val backStackLayoutId: Int

    protected abstract val layoutId: Int

    protected abstract val mainFragment: Fragment

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showFragment(mainFragment, false)
    }

    fun showFragment(fragment: Fragment, addToBackStack: Boolean) = with(childFragmentManager.beginTransaction()) {
        setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        replace(backStackLayoutId, fragment)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}