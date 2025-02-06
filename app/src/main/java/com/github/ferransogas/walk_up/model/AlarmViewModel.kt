package com.github.ferransogas.walk_up.model

import android.app.AlarmManager
import android.content.Context
import com.github.ferransogas.walk_up.data.AlarmDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val coroutineScope: CoroutineScope
) {

    fun setAlarm(hour: Int, minute: Int) {
        coroutineScope.launch {
            AlarmDataStore.saveAlarm(
                context = context,
                hour = hour,
                minute = minute,
                enabled = true // Always activate the alarm after saving it
            )
        }
        scheduleAlarm()
    }

    fun toggleAlarm(enabled: Boolean) {
        coroutineScope.launch {
            AlarmDataStore.toggleAlarm(
                context = context,
                enabled = enabled
            )
        }
        if (enabled) {
            scheduleAlarm()
        } else {
            cancelAlarm()
        }
    }

    private fun cancelAlarm() {
        //TODO
        return
    }

    private fun scheduleAlarm() {
        //TODO
        return
    }
}