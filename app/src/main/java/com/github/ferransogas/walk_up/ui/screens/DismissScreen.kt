package com.github.ferransogas.walk_up.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.ferransogas.walk_up.model.WalkDetector

@Composable
fun dismissScreen(
    walkDetector: WalkDetector,
    maxProgress: Float,
    onProgressUpdate: (progress: Float) -> Unit
) {
    val progress by walkDetector.walkProgress.collectAsState()

    LaunchedEffect(progress) {
        onProgressUpdate(progress)
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                circularProgress(progress, maxProgress)
                Text(
                    text = "walk up",
                    style = MaterialTheme.typography.displaySmall,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun circularProgress(progress: Float, maxProgress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress / maxProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    CircularProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.size(220.dp),
        strokeWidth = 4.dp,
        strokeCap = StrokeCap.Round,
    )
}