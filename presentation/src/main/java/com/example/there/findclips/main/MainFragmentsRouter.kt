package com.example.there.findclips.main

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.dashboard.DashboardFragment
import com.example.there.findclips.favourites.FavouritesFragment
import com.example.there.findclips.search.SearchFragment

object MainFragmentsRouter {

    fun goToDashboardFragment(activity: MainActivity?) {
        val dashboardFragment = DashboardFragment()
        goToFragment(activity, dashboardFragment)
        activity?.title = "Dashboard"
    }

    fun goToFavouritesFragment(activity: MainActivity?) {
        val favouritesFragment = FavouritesFragment()
        goToFragment(activity, favouritesFragment)
        activity?.title = "Favourites"
    }

    fun goToSearchFragment(activity: MainActivity?) {
        val searchFragment = SearchFragment()
        goToFragment(activity, searchFragment)
        activity?.title = "Search"
    }

    private fun goToFragment(activity: MainActivity?, fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.commit()
    }
}