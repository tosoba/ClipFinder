package com.example.there.findclips.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        when (item.itemId) {
            R.id.action_dashboard -> MainRouter.goToDashboardFragment(this)
            R.id.action_favorites -> MainRouter.goToFavouritesFragment(this)
            R.id.action_search -> MainRouter.goToSearchFragment(this)
        }

        return@OnNavigationItemSelectedListener true
    }

    private val view: MainActivityView by lazy { MainActivityView(onNavigationItemSelectedListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view

        if (savedInstanceState == null) {
            MainRouter.goToDashboardFragment(this)
        }
    }

    fun updateBottomNavigationSelectedItemId(id: Int) {
        bottom_navigation_view?.menu?.findItem(id)?.isChecked = true
    }
}
