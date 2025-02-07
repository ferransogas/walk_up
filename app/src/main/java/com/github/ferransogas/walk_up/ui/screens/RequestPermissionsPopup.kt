package com.github.ferransogas.walk_up.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun requestPermissionsPopup(
    onAccept: () -> Unit,
    onDeny: () -> Unit,
) {
    Dialog(onDismissRequest = onDeny) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Please grant alarm permission to set the alarm.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDeny() }) {
                        Text(text = "Deny")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onAccept() }) {
                        Text(text = "Accept")
                    }

                }
            }
        }
    }
}