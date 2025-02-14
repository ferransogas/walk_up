package com.github.ferransogas.walk_up.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmForegroundService::class.java)
        context.stopService(serviceIntent)
    }
}