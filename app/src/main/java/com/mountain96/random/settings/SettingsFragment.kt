package com.mountain96.random.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.settings.dialog.SettingsDialog
import com.mountain96.random.ui.foods.InitSettings

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_settings)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val resetDataBtn = findPreference<Preference>("resetData")
        val dialog = SettingsDialog(childFragmentManager)

        resetDataBtn!!.setOnPreferenceClickListener {
            dialog.showDataResetDialog()
            return@setOnPreferenceClickListener true
        }
    }
}