package com.example.core.android.spotify.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.example.core.SpotifyDefaults
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable

class SpotifyPreferences(context: Context) : SpotifyTokensHolder {
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

    override val token: String
        get() = requireNotNull(preferences.getString(PREF_KEY_TOKEN, null)) { "AccessToken is null." }

    override val refreshToken: String
        get() = requireNotNull(preferences.getString(PREF_KEY_REFRESH_TOKEN, null)) { "RefreshToken is null." }

    override val tokensPrivate: Boolean
        get() = preferences.getBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, false)

    val hasTokens: Boolean
        get() = preferences.contains(PREF_KEY_TOKEN)

    override fun setPrivateTokens(accessToken: String, refreshToken: String) {
        with(preferences.edit()) {
            putString(PREF_KEY_TOKEN, accessToken)
            putString(PREF_KEY_REFRESH_TOKEN, refreshToken)
            putBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, true)
            apply()
        }
    }

    override fun setToken(accessToken: String) {
        with(preferences.edit()) {
            putString(PREF_KEY_TOKEN, accessToken)
            putBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, false)
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

    companion object {
        private const val PREF_KEY_TOKEN = "PREF_KEY_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_ARE_TOKENS_PRIVATE = "PREF_KEY_IS_TOKEN_PRIVATE"

        private const val PREF_KEY_USER_PRIVATE_ACCESS_TOKEN = "PREF_KEY_USER_PRIVATE_ACCESS_TOKEN"
        private const val PREF_KEY_USER_PRIVATE_ACCESS_TOKEN_TIMESTAMP = "PREF_KEY_USER_PRIVATE_ACCESS_TOKEN_TIMESTAMP"

        private const val PREF_KEY_COUNTRY = "Country"
        private const val PREF_KEY_LANGUAGE = "Language"
    }
}
