package com.example.there.findclips

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.dashboard.DashboardFragment
import com.example.there.findclips.favourites.FavouritesFragment
import com.example.there.findclips.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        when (item.itemId) {
            R.id.action_dashboard -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DashboardFragment(), TAGS.dashboard)
                        .commitNow()
            }

            R.id.action_favorites -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FavouritesFragment(), TAGS.favourites)
                        .commitNow()
            }

            R.id.action_search -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SearchFragment(), TAGS.search)
                        .commitNow()
            }
        }

        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DashboardFragment(), TAGS.dashboard)
                    .commitNow()
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    companion object {
        private object TAGS {
            const val dashboard = "Dashboard"
            const val favourites = "Favourites"
            const val search = "Search"
        }
    }
}
