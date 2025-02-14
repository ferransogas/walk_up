package com.github.ferransogas.walk_up.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.github.ferransogas.walk_up.ALARM_TRIGGERED") {
            val serviceIntent = Intent(context, AlarmForegroundService::class.java)
            context.startForegroundService(serviceIntent)
            // Alarm already disabled, just toggle the switch button to off
            context.toggleAlarmState(false)
        }
    }
}