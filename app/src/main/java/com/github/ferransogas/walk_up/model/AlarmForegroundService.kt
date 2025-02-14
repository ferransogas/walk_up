package com.github.ferransogas.walk_up.model

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import androidx.core.app.NotificationCompat
import com.github.ferransogas.walk_up.DismissAlarmActivity
import com.github.ferransogas.walk_up.R

class AlarmForegroundService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, showNotification(this))

        playSound(this)
        vibrate(this)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun vibrate(context: Context) {
        val vibrator: Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(VibratorManager::class.java)
            vibrator = vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (vibrator.hasVibrator()) {
            val pattern = longArrayOf(0, 500, 200, 1000, 200)
            val effect = VibrationEffect.createWaveform(pattern, 1)
            vibrator.vibrate(effect)
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

    private fun showNotification(context: Context) : Notification {
        val fullScreenIntent = Intent(context, DismissAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            1,
            fullScreenIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val deleteIntent = Intent(this, DismissReceiver::class.java)
        val deletePendingIntent = PendingIntent.getBroadcast(
            this,
            400,
            deleteIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channel = NotificationChannel(
            "alarm_channel",
            "Alarm Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for alarm notifications"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(null, null)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Walk Up")
            .setContentText("ALARM ALARM ALARM")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(false)
            .setDeleteIntent(deletePendingIntent)
            .build()
    }
}