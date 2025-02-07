package com.github.ferransogas.walk_up.model

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.navigation.NavController
import com.github.ferransogas.walk_up.data.AlarmDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val coroutineScope: CoroutineScope,
    private val navController: NavController
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
        scheduleAlarm(hour, minute)
    }

    fun toggleAlarm(enabled: Boolean) {
        coroutineScope.launch {
            AlarmDataStore.toggleAlarm(
                context = context,
                enabled = enabled
            )
        }
        if (enabled) {
            coroutineScope.launch {
                val alarm = AlarmDataStore.getAlarm(context).first()
                scheduleAlarm(alarm.hour, alarm.minute)
            }
        } else {
            cancelAlarm()
        }
    }

    private fun cancelAlarm() {
        //TODO
        return
    }

    private fun scheduleAlarm(hour: Int, minute: Int) {
        val triggerAtMillis: Long = getTimeInMillis(hour, minute)

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.github.ferransogas.walk_up.ALARM_TRIGGERED"
        }
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.areNotificationsEnabled()) {
            toggleAlarm(enabled = false)
            navController.navigate("requestNotificationsPermission")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(triggerAtMillis, alarmPendingIntent),
                        alarmPendingIntent
                    )
                } else {
                    toggleAlarm(enabled = false)
                    navController.navigate("requestAlarmPermission")
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    alarmPendingIntent
                )
            }
        }
    }

    private fun getTimeInMillis(hour: Int, minute: Int): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }.timeInMillis
    }
}