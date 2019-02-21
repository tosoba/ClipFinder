package com.example.there.findclips.settings

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.there.findclips.R


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()

        setupActionBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() = supportActionBar?.apply {
        setDisplayShowHomeEnabled(true)
        setDisplayHomeAsUpEnabled(true)
        setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SettingsActivity, R.color.colorPrimaryDark)))
    }
}
