package com.github.ferransogas.walk_up.model

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.github.ferransogas.walk_up.DismissAlarmActivity
import com.github.ferransogas.walk_up.R

class AlarmReceiver : BroadcastReceiver() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.github.ferransogas.walk_up.ALARM_TRIGGERED") {
            showNotification(context)
            playSound(context)
            // Alarm already disabled, just toggle the switch button to off
            context.toggleAlarmState(false)
        }
    }

    private fun playSound(context: Context) {
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: Uri.parse("android.resource://${context.packageName}/${R.raw.fallback_alarm}")

        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, alarmUri)
            isLooping = true
            prepare()
            start()
        }
    }

    private fun showNotification(context: Context) {
        val fullScreenIntent = Intent(context, DismissAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            1,
            fullScreenIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channel = NotificationChannel(
            "alarm_channel",
            "Alarm Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for alarm notifications"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Walk Up")
            .setContentText("ALARM ALARM ALARM")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(true)
            .build()

        notificationManager.notify(1, notification)
    }
}