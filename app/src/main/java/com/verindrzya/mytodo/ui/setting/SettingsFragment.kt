package com.verindrzya.mytodo.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.work.NotificationWorker
import java.util.concurrent.TimeUnit

class SettingsFragment: PreferenceFragmentCompat() {

    companion object {
        private const val UNIQUE_NOTIF_WORK_NAME = "UNIQUE_NOTIF_WORK_NAME"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        val notificationPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.notification_key))
        notificationPreference?.setOnPreferenceChangeListener { _, newValue ->

            val workManager = context?.let { WorkManager.getInstance(it) }

            if(newValue == true) {
                val notifWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(20, TimeUnit.MINUTES)
                    .setInitialDelay(20, TimeUnit.MINUTES)
                    .build()

                workManager?.enqueueUniquePeriodicWork(
                    UNIQUE_NOTIF_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    notifWorkRequest
                )
            } else {
                workManager?.cancelUniqueWork(UNIQUE_NOTIF_WORK_NAME)
            }

            true
        }
    }
}