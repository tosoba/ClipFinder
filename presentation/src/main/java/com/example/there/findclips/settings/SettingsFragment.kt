package com.example.there.findclips.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.example.there.findclips.R


class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }

}
