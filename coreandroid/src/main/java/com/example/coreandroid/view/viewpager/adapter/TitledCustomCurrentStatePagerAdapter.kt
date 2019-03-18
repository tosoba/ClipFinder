package com.example.coreandroid.view.viewpager.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class TitledCustomCurrentStatePagerAdapter(
        fragmentManager: FragmentManager,
        private val titledFragments: Array<Pair<String, Fragment>>
) : CustomCurrentStatePagerAdapter(
        fragmentManager,
        titledFragments.map { (_, fragment) -> fragment }.toTypedArray()
) {

    override fun getPageTitle(position: Int): CharSequence? = titledFragments[position].first
}