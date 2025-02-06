package com.github.ferransogas.walk_up.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import com.github.ferransogas.walk_up.data.AlarmDataStore
import java.util.*
import com.github.ferransogas.walk_up.data.AlarmData
import kotlinx.coroutines.launch

@Composable
fun alarmScreen(
    alarmData: AlarmData?,
    onTapBehaviour: () -> Unit,
    onToggleAlarm: (enabled: Boolean) -> Unit,
) {
    alarmData?.let {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        alarmData.hour,
                        alarmData.minute
                    ),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.clickable { onTapBehaviour() }
                )
                Switch(
                    checked = alarmData.enabled,
                    onCheckedChange = { isEnabled ->
                        onToggleAlarm(isEnabled)
                    },
                )
            }
        }
    }

}
