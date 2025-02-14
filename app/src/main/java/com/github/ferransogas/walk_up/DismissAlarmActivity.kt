package com.github.ferransogas.walk_up

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.ferransogas.walk_up.model.AlarmForegroundService
import com.github.ferransogas.walk_up.model.WalkDetector
import com.github.ferransogas.walk_up.ui.screens.dismissScreen
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme

class DismissAlarmActivity : ComponentActivity() {
    private lateinit var walkDetector: WalkDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)

        walkDetector = WalkDetector(this)

        setContent {
            WalkUpTheme {
                dismissScreen(
                    walkDetector = walkDetector,
                    maxProgress = 25f,
                    onProgressUpdate = { progress ->
                        if (progress >= 25f) {
                            this.stopService(
                                Intent(this, AlarmForegroundService::class.java)
                            )
                            this.startActivity(
                                Intent(this, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                            )
                            finish()
                        }
                    }
                )
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