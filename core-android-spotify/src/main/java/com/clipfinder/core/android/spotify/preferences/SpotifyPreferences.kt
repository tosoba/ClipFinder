package com.clipfinder.core.android.spotify.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.functions.Function3

class SpotifyPreferences(context: Context) : SpotifyTokensHolder {
    private val preferences: SharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)
        .apply {
            if (!contains(PREF_KEY_COUNTRY) || !contains(PREF_KEY_LANGUAGE)) edit {
                if (!contains(PREF_KEY_COUNTRY)) putString(PREF_KEY_COUNTRY, DEFAULT_COUNTRY)
                if (!contains(PREF_KEY_LANGUAGE)) putString(PREF_KEY_LANGUAGE, DEFAULT_LOCALE)
            }
        }
    private val rxPreferences: RxSharedPreferences = RxSharedPreferences.create(preferences)

    val countryObservable: Observable<String>
        get() = rxPreferences.getString(PREF_KEY_COUNTRY).asObservable()
    val localeObservable: Observable<String>
        get() = rxPreferences.getString(PREF_KEY_LANGUAGE).asObservable()

    var country: String
        get() = preferences.getString(PREF_KEY_COUNTRY, DEFAULT_COUNTRY) ?: DEFAULT_COUNTRY
        set(value) = preferences.edit { putString(PREF_KEY_COUNTRY, value) }

    var locale: String
        get() = preferences.getString(PREF_KEY_LANGUAGE, DEFAULT_LOCALE) ?: DEFAULT_LOCALE
        set(value) = preferences.edit { putString(PREF_KEY_LANGUAGE, value) }

    override val token: String
        get() = requireNotNull(preferences.getString(PREF_KEY_TOKEN, null)) {
            "AccessToken is null."
        }

    override val refreshToken: String
        get() = requireNotNull(preferences.getString(PREF_KEY_REFRESH_TOKEN, null)) {
            "RefreshToken is null."
        }

    override val tokensPrivate: Boolean
        get() = preferences.getBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, false)

    val hasTokens: Boolean
        get() = preferences.contains(PREF_KEY_TOKEN)

    val isPrivateAuthorized: Observable<Boolean>
        get() = Observable.combineLatest(
            rxPreferences.getString(PREF_KEY_TOKEN).asObservable(),
            rxPreferences.getString(PREF_KEY_REFRESH_TOKEN).asObservable(),
            rxPreferences.getBoolean(PREF_KEY_ARE_TOKENS_PRIVATE).asObservable(),
            Function3 { token, refreshToken, private ->
                token.isNotEmpty() && refreshToken.isNotEmpty() && private
            }
        )

    override fun setPrivateTokens(accessToken: String, refreshToken: String) = preferences.edit {
        putString(PREF_KEY_TOKEN, accessToken)
        putString(PREF_KEY_REFRESH_TOKEN, refreshToken)
        putBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, true)
    }

    override fun setToken(accessToken: String) = preferences.edit {
        putString(PREF_KEY_TOKEN, accessToken)
        putBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, false)
    }

    override fun clearTokens() = preferences.edit {
        remove(PREF_KEY_TOKEN)
        remove(PREF_KEY_REFRESH_TOKEN)
        putBoolean(PREF_KEY_ARE_TOKENS_PRIVATE, false)
    }

    companion object {
        private const val PREF_KEY_TOKEN = "PREF_KEY_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_ARE_TOKENS_PRIVATE = "PREF_KEY_IS_TOKEN_PRIVATE"

        private const val PREF_KEY_COUNTRY = "Country"
        private const val PREF_KEY_LANGUAGE = "Language"

        private const val DEFAULT_COUNTRY = "US"
        private const val DEFAULT_LOCALE = "en_us"
    }
}
