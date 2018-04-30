package com.example.there.findclips.main

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.dashboard.DashboardFragment
import com.example.there.findclips.favourites.FavouritesFragment
import com.example.there.findclips.search.SearchFragment

object MainRouter {

    fun goToDashboardFragment(activity: MainActivity?) {
        val dashboardFragment = DashboardFragment()
        goToFragment(activity, dashboardFragment, MainFragmentTags.dashboard)
        updateBottomNavigationSelectedItemId(activity, dashboardFragment)
    }

    fun goToFavouritesFragment(activity: MainActivity?) {
        val favouritesFragment = FavouritesFragment()
        goToFragment(activity, favouritesFragment, MainFragmentTags.favourites)
        updateBottomNavigationSelectedItemId(activity, favouritesFragment)
    }

    fun goToSearchFragment(activity: MainActivity?) {
        val searchFragment = SearchFragment()
        goToFragment(activity, searchFragment, MainFragmentTags.search)
        updateBottomNavigationSelectedItemId(activity, searchFragment)
    }

    fun goToSearchFragmentWithVideosQuery(activity: MainActivity?, query: String) {
        val searchFragment = SearchFragment.newInstanceVideosSearch(query)
        goToFragment(activity, searchFragment, MainFragmentTags.search)
        updateBottomNavigationSelectedItemId(activity, searchFragment)
    }

    private fun goToFragment(activity: MainActivity?, fragment: Fragment, tag: String) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment, tag)?.commit()
    }

    private fun updateBottomNavigationSelectedItemId(activity: MainActivity?, fragment: MainFragment) {
        activity?.updateBottomNavigationSelectedItemId(fragment.bottomNavigationItemId)
    }
}