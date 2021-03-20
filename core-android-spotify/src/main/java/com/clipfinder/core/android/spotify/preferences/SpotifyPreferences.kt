package com.clipfinder.core.android.spotify.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.clipfinder.core.spotify.auth.ISpotifyTokensHolder
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import net.openid.appauth.AuthState

class SpotifyPreferences(context: Context) : ISpotifyTokensHolder {
    private val preferences: SharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)
        .apply {
            if (!contains(Keys.PREF_KEY_COUNTRY.name) || !contains(Keys.PREF_KEY_LANGUAGE.name)) {
                edit {
                    if (!contains(Keys.PREF_KEY_COUNTRY.name)) {
                        putString(Keys.PREF_KEY_COUNTRY.name, DEFAULT_COUNTRY)
                    }
                    if (!contains(Keys.PREF_KEY_LANGUAGE.name)) {
                        putString(Keys.PREF_KEY_LANGUAGE.name, DEFAULT_LOCALE)
                    }
                }
            }
        }

    private val rxPreferences: RxSharedPreferences = RxSharedPreferences.create(preferences)

    val countryObservable: Observable<String>
        get() = rxPreferences.getString(Keys.PREF_KEY_COUNTRY.name).asObservable()

    val localeObservable: Observable<String>
        get() = rxPreferences.getString(Keys.PREF_KEY_LANGUAGE.name).asObservable()

    var country: String
        get() = preferences.getString(Keys.PREF_KEY_COUNTRY.name, DEFAULT_COUNTRY)
            ?: DEFAULT_COUNTRY
        set(value) = preferences.edit { putString(Keys.PREF_KEY_COUNTRY.name, value) }

    var locale: String
        get() = preferences.getString(Keys.PREF_KEY_LANGUAGE.name, DEFAULT_LOCALE)
            ?: DEFAULT_LOCALE
        set(value) = preferences.edit { putString(Keys.PREF_KEY_LANGUAGE.name, value) }

    var authState: AuthState?
        get() = preferences.getString(Keys.PREF_KEY_AUTH_STATE.name, null)
            ?.let(AuthState::jsonDeserialize)
        set(value) = preferences.edit {
            value?.let { putString(Keys.PREF_KEY_AUTH_STATE.name, it.jsonSerializeString()) }
                ?: remove(Keys.PREF_KEY_AUTH_STATE.name)
        }

    override val privateAccessToken: String?
        get() = authState?.accessToken

    override var publicAccessToken: String?
        get() = preferences.getString(Keys.PREF_KEY_PUBLIC_TOKEN.name, null)
        set(value) = preferences.edit {
            value?.let { putString(Keys.PREF_KEY_PUBLIC_TOKEN.name, it) }
                ?: remove(Keys.PREF_KEY_PUBLIC_TOKEN.name)
        }

    val containPublicAccessToken: Boolean
        get() = preferences.contains(Keys.PREF_KEY_PUBLIC_TOKEN.name)

    val isPrivateAuthorizedObservable: Observable<Boolean>
        get() = rxPreferences.getString(Keys.PREF_KEY_AUTH_STATE.name, "")
            .asObservable()
            .filter(CharSequence::isNotBlank)
            .map { AuthState.jsonDeserialize(it)?.accessToken != null }
            .onErrorReturnItem(false)

    val privateAccessTokenObservable: Observable<String>
        get() = rxPreferences.getString(Keys.PREF_KEY_AUTH_STATE.name, "")
            .asObservable()
            .map { if (it.isNotBlank()) AuthState.jsonDeserialize(it)?.accessToken ?: "" else "" }
            .onErrorReturnItem("")

    private enum class Keys {
        PREF_KEY_PUBLIC_TOKEN,
        PREF_KEY_AUTH_STATE,
        PREF_KEY_COUNTRY,
        PREF_KEY_LANGUAGE
    }

    companion object {
        private const val DEFAULT_COUNTRY = "US"
        private const val DEFAULT_LOCALE = "en_us"
    }
}
