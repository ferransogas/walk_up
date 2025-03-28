package com.github.ferransogas.walk_up.model

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import androidx.core.app.NotificationCompat
import com.github.ferransogas.walk_up.DismissAlarmActivity
import com.github.ferransogas.walk_up.R
import com.github.ferransogas.walk_up.data.AlarmDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmForegroundService : Service() {
    private var ringtone: Ringtone? = null
    private var vibrator: Vibrator? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        CoroutineScope(Dispatchers.IO).launch {
            AlarmDataStore.setForegroundEnabled(
                context = this@AlarmForegroundService,
                enabled = true
            )
        }

        startForeground(1, showNotification(this))

        playSound(this)
        vibrate(this)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        CoroutineScope(Dispatchers.IO).launch {
            AlarmDataStore.setForegroundEnabled(
                context = this@AlarmForegroundService,
                enabled = false
            )
        }

        ringtone?.stop()
        ringtone = null

        vibrator?.cancel()
        vibrator = null
    }

    private fun vibrate(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(VibratorManager::class.java)
            vibrator = vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (vibrator!!.hasVibrator()) {
            val pattern = longArrayOf(0, 500, 200, 1000, 200)
            val effect = VibrationEffect.createWaveform(pattern, 1)
            vibrator!!.vibrate(effect)
        }
    }

    private fun playSound(context: Context) {
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: Uri.parse("android.resource://${context.packageName}/${R.raw.fallback_alarm}")

        ringtone = RingtoneManager.getRingtone(context, alarmUri).apply {
            isLooping = true
            audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            play()
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
            .setContentTitle("walk up")
            .setContentText("Open the app and walk to dismiss the alarm")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(true)
            .build()
    }
}