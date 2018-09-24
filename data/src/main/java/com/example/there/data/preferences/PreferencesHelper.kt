package com.example.there.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.there.data.api.spotify.SpotifyApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class PreferencesHelper @Inject constructor(context: Context) {

    private val bufferPref: SharedPreferences = context.getSharedPreferences(PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE)

    var country: String
        get() = bufferPref.getString(PREF_KEY_COUNTRY, SpotifyApi.DEFAULT_COUNTRY)
                ?: SpotifyApi.DEFAULT_COUNTRY
        set(value) = bufferPref.edit().putString(PREF_KEY_COUNTRY, value).apply()

    var language: String
        get() = bufferPref.getString(PREF_KEY_LANGUAGE, SpotifyApi.DEFAULT_LOCALE)
                ?: SpotifyApi.DEFAULT_LOCALE
        set(value) = bufferPref.edit().putString(PREF_KEY_LANGUAGE, value).apply()

    companion object {
        private const val PREF_BUFFER_PACKAGE_NAME = "SHARED_PREFERENCES_KEY"

        private const val PREF_KEY_COUNTRY = "Country"
        private const val PREF_KEY_LANGUAGE = "Language"
    }
}