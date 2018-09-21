package com.example.there.findclips.util.ext

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.R

val Activity.app: FindClipsApp
    get() = this.application as FindClipsApp

private const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
private const val PREF_KEY_ACCESS_TOKEN_TIMESTAMP = "PREF_KEY_ACCESS_TOKEN_TIMESTAMP"

val Activity.accessToken: AccessTokenEntity?
    get() {
        val preferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val token = preferences?.getString(PREF_KEY_ACCESS_TOKEN, null)
        val timestamp = preferences?.getLong(PREF_KEY_ACCESS_TOKEN_TIMESTAMP, 0L)
        return if (token == null || timestamp == null) null
        else AccessTokenEntity(token, timestamp)
    }

fun Activity.saveAccessToken(accessToken: AccessTokenEntity) {
    val preferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    with(preferences.edit()) {
        putString(PREF_KEY_ACCESS_TOKEN, accessToken.token)
        putLong(PREF_KEY_ACCESS_TOKEN_TIMESTAMP, accessToken.timestamp)
        apply()
    }
}

fun Activity.registerFragmentLifecycleCallbacks(callbacks: FragmentManager.FragmentLifecycleCallbacks, recursive: Boolean) =
        (this as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(callbacks, recursive)

fun AppCompatActivity.showDrawerHamburger() = supportActionBar?.apply {
    setDisplayHomeAsUpEnabled(true)
    setHomeAsUpIndicator(R.drawable.drawer_menu)
}
