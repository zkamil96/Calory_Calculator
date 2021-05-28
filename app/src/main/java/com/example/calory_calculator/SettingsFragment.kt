package com.example.calory_calculator

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
       // var growth_preference = preferenceScreen.findPreference<EditTextPreference>("growth")



        }
    }
