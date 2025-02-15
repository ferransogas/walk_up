package com.github.ferransogas.walk_up.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Schedule WorkManager to reset alarms
            val workRequest = OneTimeWorkRequest.Builder(AlarmResetWorker::class.java).build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
