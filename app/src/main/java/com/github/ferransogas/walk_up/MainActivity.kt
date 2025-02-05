package com.github.ferransogas.walk_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var isEditingAlarm: Boolean by mutableStateOf(false)

        setContent {
            WalkUpTheme {
                if (isEditingAlarm) {
                    editAlarmScreen()
                } else {
                    alarmScreen(onTapBehaviour = { isEditingAlarm = true })
                }
            }
        }
    }

    @Composable
    private fun alarmScreen(onTapBehaviour: () -> Unit) {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "%02d:%02d", 5, 12),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.clickable { onTapBehaviour() }
                )
                Switch(
                    checked = false,
                    onCheckedChange = {},
                )
            }
        }
    }

    @Composable
    private fun editAlarmScreen() {
        Scaffold {
            Text(
                text = "hola"
            )
        }
    }
}