package com.example.there.findclips.util

import android.app.Activity
import android.content.Context
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.findclips.FindClipsApp

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

fun Throwable.messageOrDefault(message: String = "Unknown error."): String {
    return this.message ?: message
}

val Context.screenOrientation: Int
    get() = resources.configuration.orientation