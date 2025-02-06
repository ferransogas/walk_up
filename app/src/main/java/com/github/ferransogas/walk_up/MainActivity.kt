package com.github.ferransogas.walk_up

import android.app.AlarmManager
import android.app.Service
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.github.ferransogas.walk_up.data.AlarmData
import com.github.ferransogas.walk_up.data.AlarmDataStore
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme
import com.github.ferransogas.walk_up.ui.screens.alarmScreen
import com.github.ferransogas.walk_up.ui.screens.editAlarmScreen
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.ferransogas.walk_up.model.AlarmViewModel


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

/*
sealed class AppScreen {
    data object Alarm : AppScreen()
    data object Edit : AppScreen()
}
 */

@Composable
private fun mainScreen() {
    /* TODO: Test and decide which mainScreen code is more efficient
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Alarm) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val alarmData by produceState<AlarmData?>(initialValue = null) {
        AlarmDataStore.getAlarm(context).collect {
            value = it
        }
    }

    alarmScreen(
        alarmData = alarmData,
        onTapBehaviour = {
            currentScreen = AppScreen.Edit
        },
        onToggleAlarm = { enabled ->
            coroutineScope.launch {
                alarmData?.let { alarmData ->
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

    if (currentScreen is AppScreen.Edit) {
        editAlarmScreen(
            alarmData = alarmData,
            onSave = { hour, minute ->
                coroutineScope.launch {
                    AlarmDataStore.saveAlarm(
                        context = context,
                        hour = hour,
                        minute = minute,
                        enabled = true
                    )
                }
                currentScreen = AppScreen.Alarm
            },
            onCancel = { currentScreen = AppScreen.Alarm }
        )
    }
     */

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        val context = LocalContext.current

        val alarmData by produceState<AlarmData?>(initialValue = null) {
            AlarmDataStore.getAlarm(context).collect {
                value = it
            }
        }

        val alarmViewModel = AlarmViewModel(
            context = context,
            alarmManager = context.getSystemService(Service.ALARM_SERVICE) as AlarmManager,
            coroutineScope = rememberCoroutineScope()
        )

        NavHost(
            navController = navController,
            startDestination = "alarm"
        ) {
            composable(route = "alarm") {
                alarmScreen(
                    alarmData = alarmData,
                    onTapBehaviour = {
                        navController.navigate(route = "edit")
                    },
                    onToggleAlarm = { enabled ->
                        alarmViewModel.toggleAlarm(
                            enabled = enabled
                        )
                    }
                )
            }
            composable(route = "edit") {
                editAlarmScreen(
                    alarmData = alarmData,
                    onSave = { hour, minute ->
                        alarmViewModel.setAlarm(
                            hour = hour,
                            minute = minute
                        )
                        navController.navigateUp()
                    },
                    onCancel = { navController.navigateUp() }
                )
            }
        }
    }
}
