package com.clipfinder.core.android.spotify.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.functions.Function3
import net.openid.appauth.AuthState

class SpotifyPreferences(context: Context) : SpotifyTokensHolder {
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
            putString(Keys.PREF_KEY_AUTH_STATE.name, value?.jsonSerializeString())
        }

    override val token: String
        get() = requireNotNull(preferences.getString(Keys.PREF_KEY_TOKEN.name, null)) {
            "AccessToken is null."
        }

    override val refreshToken: String
        get() = requireNotNull(preferences.getString(Keys.PREF_KEY_REFRESH_TOKEN.name, null)) {
            "RefreshToken is null."
        }

    override val tokensPrivate: Boolean
        get() = preferences.getBoolean(Keys.PREF_KEY_ARE_TOKENS_PRIVATE.name, false)

    val hasTokens: Boolean
        get() = preferences.contains(Keys.PREF_KEY_TOKEN.name)

    val isPrivateAuthorized: Observable<Boolean>
        get() = Observable.combineLatest(
            rxPreferences.getString(Keys.PREF_KEY_TOKEN.name).asObservable(),
            rxPreferences.getString(Keys.PREF_KEY_REFRESH_TOKEN.name).asObservable(),
            rxPreferences.getBoolean(Keys.PREF_KEY_ARE_TOKENS_PRIVATE.name).asObservable(),
            Function3 { token, refreshToken, private ->
                token.isNotEmpty() && refreshToken.isNotEmpty() && private
            }
        )

    override fun setPrivateTokens(accessToken: String, refreshToken: String) = preferences.edit {
        putString(Keys.PREF_KEY_TOKEN.name, accessToken)
        putString(Keys.PREF_KEY_REFRESH_TOKEN.name, refreshToken)
        putBoolean(Keys.PREF_KEY_ARE_TOKENS_PRIVATE.name, true)
    }

    override fun setToken(accessToken: String) = preferences.edit {
        putString(Keys.PREF_KEY_TOKEN.name, accessToken)
        putBoolean(Keys.PREF_KEY_ARE_TOKENS_PRIVATE.name, false)
    }

    override fun clearTokens() = preferences.edit {
        remove(Keys.PREF_KEY_TOKEN.name)
        remove(Keys.PREF_KEY_REFRESH_TOKEN.name)
        putBoolean(Keys.PREF_KEY_ARE_TOKENS_PRIVATE.name, false)
    }

    private enum class Keys {
        PREF_KEY_TOKEN,
        PREF_KEY_REFRESH_TOKEN,
        PREF_KEY_ARE_TOKENS_PRIVATE,
        PREF_KEY_AUTH_STATE,
        PREF_KEY_COUNTRY,
        PREF_KEY_LANGUAGE
    }

    companion object {
        private const val DEFAULT_COUNTRY = "US"
        private const val DEFAULT_LOCALE = "en_us"
    }
}
