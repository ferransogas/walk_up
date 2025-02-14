package com.github.ferransogas.walk_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.ferransogas.walk_up.model.WalkDetector
import com.github.ferransogas.walk_up.ui.screens.dismissScreen
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme

class DismissAlarmActivity : ComponentActivity() {
    private lateinit var walkDetector: WalkDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        walkDetector = WalkDetector(this)

        setContent {
            WalkUpTheme {
                dismissScreen(walkDetector = walkDetector)
            }
        }

        walkDetector.startListening()
    }

    override fun onResume() {
        super.onResume()
        walkDetector.startListening()
    }

    override fun onPause() {
        super.onPause()
        walkDetector.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        walkDetector.stopListening()
    }
}