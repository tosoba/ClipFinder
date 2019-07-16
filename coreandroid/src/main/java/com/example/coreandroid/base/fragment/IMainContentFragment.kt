package com.example.coreandroid.base.fragment

interface IMainContentFragment {
    val currentNavHostFragment: BaseNavHostFragment?
    val currentFragment: androidx.fragment.app.Fragment?
}