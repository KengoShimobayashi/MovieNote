package com.movienote.android.ui.main

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.movienote.android.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}