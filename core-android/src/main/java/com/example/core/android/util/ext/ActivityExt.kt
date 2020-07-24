package com.example.core.android.util.ext

import androidx.appcompat.app.AppCompatActivity
import com.example.core.android.R

fun AppCompatActivity.showDrawerHamburger() = supportActionBar?.apply {
    setDisplayHomeAsUpEnabled(true)
    setHomeAsUpIndicator(R.drawable.spotify_drawer_menu_background)
}
