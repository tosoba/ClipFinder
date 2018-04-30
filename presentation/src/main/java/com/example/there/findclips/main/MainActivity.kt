package com.example.there.findclips.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.R
import com.example.there.findclips.dashboard.DashboardFragment
import com.example.there.findclips.favourites.FavouritesFragment
import com.example.there.findclips.search.SearchFragment
import com.squareup.haha.perflib.Main
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val router: MainRouter = MainRouter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        when (item.itemId) {
            R.id.action_dashboard -> router.goToDashboardFragment(this)
            R.id.action_favorites -> router.goToFavouritesFragment(this)
            R.id.action_search -> router.goToSearchFragment(this)
        }

        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.goToDashboardFragment(this)
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun updateBottomNavigationSelectedItemId(id: Int) {
        bottom_navigation_view?.menu?.findItem(id)?.isChecked = true
    }
}
