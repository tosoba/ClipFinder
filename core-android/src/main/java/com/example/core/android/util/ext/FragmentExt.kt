package com.example.core.android.util.ext

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.*
import com.example.core.android.base.fragment.BaseNavHostFragment
import com.example.core.android.base.fragment.IMainContentFragment
import com.example.core.android.base.handler.BackPressedController
import kotlin.reflect.KClass

val Fragment.backPressedController: BackPressedController?
    get() = activity as? BackPressedController

inline fun <reified T> Fragment.findAncestorFragmentOfType(): T? {
    var ancestorFragment = parentFragment
    while (ancestorFragment != null) {
        if (ancestorFragment is T) return ancestorFragment
        ancestorFragment = ancestorFragment.parentFragment
    }
    return null
}

val Fragment.navHostFragment: BaseNavHostFragment?
    get() = findAncestorFragmentOfType()

val Fragment.mainContentFragment: IMainContentFragment?
    get() = findAncestorFragmentOfType()

inline fun <T> T.show(addToBackStack: Boolean = true, getFragment: () -> Fragment) where T : Fragment {
    navHostFragment?.showFragment(getFragment(), addToBackStack)
}

inline fun <T, reified VM : BaseMvRxViewModel<S>, S : MvRxState> T.parentFragmentViewModel(
    viewModelClass: KClass<VM> = VM::class,
    crossinline keyFactory: () -> String = { viewModelClass.java.name }
) where T : Fragment, T : MvRxView = lifecycleAwareLazy(this) {
    val parent = requireNotNull(parentFragment)
    val factory = MvRxFactory {
        throw IllegalStateException("ViewModel for ${requireActivity()}[${keyFactory()}] does not exist yet!")
    }
    ViewModelProviders.of(parent, factory)
        .get(keyFactory(), viewModelClass.java)
        .apply {
            subscribe(this@parentFragmentViewModel, subscriber = { postInvalidate() })
        }
}

inline fun <reified F : Fragment> newMvRxFragmentWith(arg: Any): F = F::class.java
    .newInstance()
    .apply {
        arguments = Bundle().apply {
            when (arg) {
                is Parcelable -> putParcelable(MvRx.KEY_ARG, arg)
                is String -> putString(MvRx.KEY_ARG, arg)
                is Int -> putInt(MvRx.KEY_ARG, arg)
                is Double -> putDouble(MvRx.KEY_ARG, arg)
                is Float -> putFloat(MvRx.KEY_ARG, arg)
                else -> throw IllegalArgumentException("Invalid arg type.")
            }
        }
    }
