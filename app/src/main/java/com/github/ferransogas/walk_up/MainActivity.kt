package com.github.ferransogas.walk_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme
import com.github.ferransogas.walk_up.ui.screens.alarmScreen
import com.github.ferransogas.walk_up.ui.screens.editAlarmScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var isEditingAlarm: Boolean by mutableStateOf(false)

        setContent {
            WalkUpTheme {
                if (isEditingAlarm) {
                    editAlarmScreen(
                        onSave = { isEditingAlarm = false },
                        onCancel = { isEditingAlarm = false }
                    )
                } else {
                    alarmScreen(
                        onTapBehaviour = { isEditingAlarm = true }
                    )
                }
            }
        }
    }
}