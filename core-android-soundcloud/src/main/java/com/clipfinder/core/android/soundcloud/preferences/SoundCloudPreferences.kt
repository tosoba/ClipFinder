package com.clipfinder.core.android.soundcloud.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences

class SoundCloudPreferences(context: Context) : ISoundCloudPreferences {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    override var clientId: String?
        get() = preferences.getString(PREF_KEY_SOUND_CLOUD_CLIENT_ID, null)
        set(value) {
            requireNotNull(value)
            preferences.edit().putString(PREF_KEY_SOUND_CLOUD_CLIENT_ID, value).apply()
        }

    companion object {
        private const val PREF_KEY_SOUND_CLOUD_CLIENT_ID = "PREF_KEY_SOUND_CLOUD_CLIENT_ID"
    }
}
