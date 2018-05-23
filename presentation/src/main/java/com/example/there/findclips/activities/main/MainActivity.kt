package com.example.there.findclips.activities.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.R
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.util.checkItem
import com.example.there.findclips.view.OnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == main_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        main_view_pager.currentItem = itemIds.indexOf(item.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy { MainFragmentPagerAdapter(supportFragmentManager) }

    private val itemIds: Array<Int> = arrayOf(R.id.action_dashboard, R.id.action_favorites, R.id.action_search)

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
        }
    }


    private val view: MainView by lazy {
        MainView(onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view
        binding.mainViewPager.offscreenPageLimit = 2
    }
}
