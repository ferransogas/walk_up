package com.github.ferransogas.walk_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.github.ferransogas.walk_up.data.AlarmData
import com.github.ferransogas.walk_up.data.AlarmDataStore
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme
import com.github.ferransogas.walk_up.ui.screens.alarmScreen
import com.github.ferransogas.walk_up.ui.screens.editAlarmScreen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WalkUpTheme {
                mainScreen()
            }
        }
    }
}

sealed class AppScreen {
    object Alarm : AppScreen()
    object Edit : AppScreen()
}

@Composable
private fun mainScreen() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Alarm) }
    val alarmDataState = remember { mutableStateOf<AlarmData?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        AlarmDataStore.getAlarm(context).collect { alarmData ->
            alarmDataState.value = alarmData
        }
    }

    when (currentScreen) {
        is AppScreen.Alarm -> {
            alarmScreen(
                alarmData = alarmDataState.value,
                onTapBehaviour = {
                    currentScreen = AppScreen.Edit
                },
                onToggleAlarm = { enabled ->
                    coroutineScope.launch {
                        alarmDataState.value?.let { alarmData ->
                            AlarmDataStore.saveAlarm(
                                context = context,
                                hour = alarmData.hour,
                                minute = alarmData.minute,
                                enabled = enabled
                            )
                        }
                    }
                }
            )
        }
        is AppScreen.Edit -> {
            editAlarmScreen(
                alarmData = alarmDataState.value,
                onSave = { hour, minute ->
                    coroutineScope.launch {
                        AlarmDataStore.saveAlarm(
                            context = context,
                            hour = hour,
                            minute = minute,
                            enabled = true
                        )
                        AlarmDataStore.getAlarm(context).collect { alarmData ->
                            alarmDataState.value = alarmData
                        }
                    }
                    currentScreen = AppScreen.Alarm
                },
                onCancel = { currentScreen = AppScreen.Alarm }
            )
        }
    }
}