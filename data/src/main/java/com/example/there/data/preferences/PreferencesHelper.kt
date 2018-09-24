package com.example.there.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.there.data.api.spotify.SpotifyApi
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class PreferencesHelper @Inject constructor(context: Context) {

    private val preferences: SharedPreferences
    private val rxPreferences: RxSharedPreferences

    private val countryRx: Preference<String>
    private val languageRx: Preference<String>

    var country: String
        get() = preferences.getString(PREF_KEY_COUNTRY, SpotifyApi.DEFAULT_COUNTRY)
                ?: SpotifyApi.DEFAULT_COUNTRY
        set(value) = preferences.edit().putString(PREF_KEY_COUNTRY, value).apply()

    var language: String
        get() = preferences.getString(PREF_KEY_LANGUAGE, SpotifyApi.DEFAULT_LOCALE)
                ?: SpotifyApi.DEFAULT_LOCALE
        set(value) = preferences.edit().putString(PREF_KEY_LANGUAGE, value).apply()

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context).apply {
            if (!contains(PREF_KEY_COUNTRY)) edit().putString(PREF_KEY_COUNTRY, SpotifyApi.DEFAULT_COUNTRY).apply()
            if (!contains(PREF_KEY_LANGUAGE)) edit().putString(PREF_KEY_LANGUAGE, SpotifyApi.DEFAULT_LOCALE).apply()
        }
        rxPreferences = RxSharedPreferences.create(preferences)

        countryRx = rxPreferences.getString(PREF_KEY_COUNTRY)
        languageRx = rxPreferences.getString(PREF_KEY_LANGUAGE)
    }

    val countryObservable: Observable<String>
        get() = countryRx.asObservable()

    val languageObservable: Observable<String>
        get() = languageRx.asObservable()

    companion object {
        private const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"

        private const val PREF_KEY_COUNTRY = "Country"
        private const val PREF_KEY_LANGUAGE = "Language"
    }
}