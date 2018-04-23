package com.example.there.findclips.util

import android.app.Activity
import android.content.Context
import com.example.there.findclips.FindClipsApp

val Activity.app: FindClipsApp
    get() = this.application as FindClipsApp

private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"

val Activity.accessToken: String?
    get() = getPreferences(Context.MODE_PRIVATE)?.getString(PREF_KEY_ACCESS_TOKEN, null)

fun Activity.saveAccessToken(token: String) {
    val sharedPref = getPreferences(Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putString(PREF_KEY_ACCESS_TOKEN, token)
        apply()
    }
}

fun String?.orDefault(message: String): String {
    return this ?: message
}