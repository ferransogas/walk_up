package com.github.ferransogas.walk_up.model

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.AlarmManager
import com.github.ferransogas.walk_up.data.AlarmDataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AlarmResetWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    @OptIn(DelicateCoroutinesApi::class)
    override fun doWork(): Result {
        val alarm = runBlocking(Dispatchers.IO) {
            AlarmDataStore.getAlarm(applicationContext).first()
        }
        if (alarm.enabled) {
            val alarmViewModel = AlarmViewModel(
                context = applicationContext,
                alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                coroutineScope = kotlinx.coroutines.GlobalScope,
                navController = null
            )
            alarmViewModel.toggleAlarm(enabled = true)
        }

        return Result.success()
    }
}