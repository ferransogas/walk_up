package com.github.ferransogas.walk_up.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editAlarmScreen(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {

    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0
    )

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Alarm",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TimePicker(
                    state = timePickerState
                )
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onCancel() }) {
                        Text(text = "Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onSave() }) {
                        Text(text = "Save")
                    }

                }
            }
        }
    }
}