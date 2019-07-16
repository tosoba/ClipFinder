package com.example.coreandroid.view.viewpager.adapter

class TitledCustomCurrentStatePagerAdapter(
        fragmentManager: androidx.fragment.app.FragmentManager,
        private val titledFragments: Array<Pair<String, androidx.fragment.app.Fragment>>
) : CustomCurrentStatePagerAdapter(
        fragmentManager,
        titledFragments.map { (_, fragment) -> fragment }.toTypedArray()
) {

    override fun getPageTitle(position: Int): CharSequence? = titledFragments[position].first
}