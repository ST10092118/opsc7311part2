package com.opsc7311.opsc7311poepart2

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        applyCustomTheme()

        val themePreference: ListPreference? = findPreference("theme")
        themePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            when (newValue) {
                "system" -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                "dark" -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                "light" -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

//        val deleteProfilePreference: Preference? = findPreference("delete_profile")
//        deleteProfilePreference?.setOnPreferenceClickListener {
//            showDeleteConfirmationDialog()
//            true
//        }
    }

    private fun setTheme(themeMode: Int) {
        Log.d("SettingsFragment", "Setting theme to mode: $themeMode")
        AppCompatDelegate.setDefaultNightMode(themeMode)
        activity?.recreate()
    }


//    private fun showDeleteConfirmationDialog() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Delete Profile")
//            .setMessage("Are you sure you want to delete your profile? This action cannot be undone.")
//            .setPositiveButton("Delete") { _, _ ->
//                deleteUserProfile()
//            }
//            .setNegativeButton("Cancel", null)
//            .create()
//            .show()
//    }
//
//    private fun deleteUserProfile() {
//        // Implement the logic to delete the user profile from the database
//        // and log the user out
//
//        // Assuming a ViewModel is used for user data
////        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
////        userViewModel.deleteUserProfile()
////
////        // Log the user out
////        // Assuming an AuthManager class handles user authentication
////        AuthManager.logout()
////
////        // Redirect to login screen
////        val intent = Intent(requireContext(), LoginActivity::class.java)
////        startActivity(intent)
////        activity?.finish()
//    }

    private fun applyCustomTheme() {
        // Apply the custom preference theme
        context?.theme?.applyStyle(R.style.ClockWork_LightTheme, true)
    }
}
