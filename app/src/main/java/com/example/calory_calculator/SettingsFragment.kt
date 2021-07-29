package com.example.calory_calculator

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val growth_preference = findPreference("growth") as EditTextPreference?
        val weight_preference = findPreference("weight") as EditTextPreference?
        val age_preference = findPreference("age") as EditTextPreference?

        growth_preference!!.setOnBindEditTextListener {
            editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        weight_preference!!.setOnBindEditTextListener {
            editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        age_preference!!.setOnBindEditTextListener {
            editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        }
    }
