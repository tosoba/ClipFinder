package com.clipfinder.core.android.spotify.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.clipfinder.core.android.spotify.R
import com.clipfinder.core.spotify.auth.ISpotifyTokensHolder
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import net.openid.appauth.AuthState

class SpotifyPreferences(context: Context) : ISpotifyTokensHolder {
    private val countryPrefKey = context.getString(R.string.country_pref_key)
    private val languagePrefKey = context.getString(R.string.language_pref_key)

    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context).apply {
            if (!contains(countryPrefKey) || !contains(languagePrefKey)) {
                edit {
                    if (!contains(countryPrefKey)) putString(countryPrefKey, DEFAULT_COUNTRY)
                    if (!contains(languagePrefKey)) putString(languagePrefKey, DEFAULT_LOCALE)
                }
            }
        }

    private val rxPreferences: RxSharedPreferences = RxSharedPreferences.create(preferences)

    val countryObservable: Observable<String>
        get() = rxPreferences.getString(countryPrefKey).asObservable()

    val localeObservable: Observable<String>
        get() = rxPreferences.getString(languagePrefKey).asObservable()

    var country: String
        get() = preferences.getString(countryPrefKey, DEFAULT_COUNTRY) ?: DEFAULT_COUNTRY
        set(value) = preferences.edit { putString(countryPrefKey, value) }

    var locale: String
        get() = preferences.getString(languagePrefKey, DEFAULT_LOCALE) ?: DEFAULT_LOCALE
        set(value) = preferences.edit { putString(languagePrefKey, value) }

    var authState: AuthState?
        get() =
            preferences
                .getString(Keys.PREF_KEY_AUTH_STATE.name, null)
                ?.let(AuthState::jsonDeserialize)
        set(value) =
            preferences.edit {
                value?.let { putString(Keys.PREF_KEY_AUTH_STATE.name, it.jsonSerializeString()) }
                    ?: remove(Keys.PREF_KEY_AUTH_STATE.name)
            }

    override val privateAccessToken: String?
        get() {
            val privateAuthState = authState
            val expirationTime = privateAuthState?.accessTokenExpirationTime
            return if (expirationTime != null && System.currentTimeMillis() >= expirationTime) null
            else privateAuthState?.accessToken
        }

    override var publicAccessToken: String?
        get() {
            val token = preferences.getString(Keys.PREF_KEY_PUBLIC_TOKEN.name, null)
            return if (System.currentTimeMillis() >= publicAccessTokenExpiryTimestamp) null
            else token
        }
        set(value) =
            preferences.edit {
                value?.let { putString(Keys.PREF_KEY_PUBLIC_TOKEN.name, it) }
                    ?: remove(Keys.PREF_KEY_PUBLIC_TOKEN.name)
            }

    var publicAccessTokenExpiryTimestamp: Long
        get() = preferences.getLong(Keys.PREF_KEY_PUBLIC_TOKEN_EXPIRY_TIMESTAMP.name, -1L)
        set(value) =
            preferences.edit { putLong(Keys.PREF_KEY_PUBLIC_TOKEN_EXPIRY_TIMESTAMP.name, value) }

    val isPrivateAuthorizedObservable: Observable<Boolean>
        get() =
            rxPreferences
                .getString(Keys.PREF_KEY_AUTH_STATE.name, "")
                .asObservable()
                .filter(CharSequence::isNotBlank)
                .map { serializedAuthState ->
                    val authState = AuthState.jsonDeserialize(serializedAuthState)
                    val expirationTime = authState.accessTokenExpirationTime
                    val accessToken = authState.accessToken
                    accessToken != null &&
                        expirationTime != null &&
                        System.currentTimeMillis() < expirationTime
                }
                .onErrorReturnItem(false)
                .distinctUntilChanged()

    private enum class Keys {
        PREF_KEY_PUBLIC_TOKEN,
        PREF_KEY_PUBLIC_TOKEN_EXPIRY_TIMESTAMP,
        PREF_KEY_AUTH_STATE,
    }

    companion object {
        private const val DEFAULT_COUNTRY = "US"
        private const val DEFAULT_LOCALE = "en_us"
    }
}
