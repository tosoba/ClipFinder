package com.example.core.android.spotify.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.clipfinder.core.spotify.provider.AccessTokenProvider
import com.example.core.SpotifyDefaults
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable

class SpotifyPreferences(context: Context) : AccessTokenProvider {

    private val preferences: SharedPreferences
    private val rxPreferences: RxSharedPreferences

    private val countryRx: Preference<String>
    private val languageRx: Preference<String>

    var country: String
        get() = preferences.getString(PREF_KEY_COUNTRY, SpotifyDefaults.COUNTRY)
            ?: SpotifyDefaults.COUNTRY
        set(value) = preferences.edit().putString(PREF_KEY_COUNTRY, value).apply()

    var locale: String
        get() = preferences.getString(PREF_KEY_LANGUAGE, SpotifyDefaults.LOCALE)
            ?: SpotifyDefaults.LOCALE
        set(value) = preferences.edit().putString(PREF_KEY_LANGUAGE, value).apply()

    override var token: String?
        get() = preferences.getString(PREF_KEY_TOKEN, null)
        set(value) {
            if (value == null) return
            with(preferences.edit()) {
                putString(PREF_KEY_TOKEN, value)
                apply()
            }
        }

    var accessToken: AccessTokenEntity?
        get() {
            val token = preferences.getString(PREF_KEY_ACCESS_TOKEN, null)
            val timestamp = preferences.getLong(PREF_KEY_ACCESS_TOKEN_TIMESTAMP, 0L)
            return if (token == null) null
            else AccessTokenEntity(token, timestamp)
        }
        set(value) {
            if (value == null) return
            with(preferences.edit()) {
                putString(PREF_KEY_ACCESS_TOKEN, value.token)
                putLong(PREF_KEY_ACCESS_TOKEN_TIMESTAMP, value.timestamp)
                apply()
            }
        }

    var userPrivateAccessToken: AccessTokenEntity?
        get() {
            val token = preferences.getString(PREF_KEY_USER_PRIVATE_ACCESS_TOKEN, null)
            val timestamp = preferences.getLong(PREF_KEY_USER_PRIVATE_ACCESS_TOKEN_TIMESTAMP, 0L)
            return if (token == null) null
            else AccessTokenEntity(token, timestamp)
        }
        set(value) {
            if (value == null) return
            with(preferences.edit()) {
                putString(PREF_KEY_USER_PRIVATE_ACCESS_TOKEN, value.token)
                putLong(PREF_KEY_USER_PRIVATE_ACCESS_TOKEN_TIMESTAMP, value.timestamp)
                apply()
            }
        }

    val countryObservable: Observable<String>
        get() = countryRx.asObservable()

    val localeObservable: Observable<String>
        get() = languageRx.asObservable()

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context).apply {
            if (!contains(PREF_KEY_COUNTRY)) {
                edit().putString(PREF_KEY_COUNTRY, SpotifyDefaults.COUNTRY).apply()
            }
            if (!contains(PREF_KEY_LANGUAGE)) {
                edit().putString(PREF_KEY_LANGUAGE, SpotifyDefaults.LOCALE).apply()
            }
        }
        rxPreferences = RxSharedPreferences.create(preferences)

        countryRx = rxPreferences.getString(PREF_KEY_COUNTRY)
        languageRx = rxPreferences.getString(PREF_KEY_LANGUAGE)
    }

    sealed class SavedAccessTokenEntity {
        class Valid(val token: String) : SavedAccessTokenEntity()
        object Invalid : SavedAccessTokenEntity()
        object NoValue : SavedAccessTokenEntity()
    }

    companion object {
        private const val PREF_KEY_TOKEN = "PREF_KEY_TOKEN"

        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_ACCESS_TOKEN_TIMESTAMP = "PREF_KEY_ACCESS_TOKEN_TIMESTAMP"

        private const val PREF_KEY_USER_PRIVATE_ACCESS_TOKEN = "PREF_KEY_USER_PRIVATE_ACCESS_TOKEN"
        private const val PREF_KEY_USER_PRIVATE_ACCESS_TOKEN_TIMESTAMP = "PREF_KEY_USER_PRIVATE_ACCESS_TOKEN_TIMESTAMP"

        private const val PREF_KEY_COUNTRY = "Country"
        private const val PREF_KEY_LANGUAGE = "Language"
    }
}
