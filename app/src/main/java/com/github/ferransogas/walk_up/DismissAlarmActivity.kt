package com.github.ferransogas.walk_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.ferransogas.walk_up.ui.screens.dismissScreen
import com.github.ferransogas.walk_up.ui.theme.WalkUpTheme

class DismissAlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*
        //TODO: test if real devices can also wake up without the commented code
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)
         */

        setContent {
            WalkUpTheme {
                dismissScreen()
            }
        }
    }
}