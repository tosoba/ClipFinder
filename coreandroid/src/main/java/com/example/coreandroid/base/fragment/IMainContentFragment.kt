package com.example.coreandroid.base.fragment

import android.support.v4.app.Fragment

interface IMainContentFragment {
    val currentNavHostFragment: BaseNavHostFragment?
    val currentFragment: Fragment?
}