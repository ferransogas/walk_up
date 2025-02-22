package com.github.ferransogas.walk_up.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedGradientBackground(colorScheme)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            circularProgress(progress, maxProgress, colorScheme)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "walk up",
                style = MaterialTheme.typography.displaySmall,
                fontStyle = FontStyle.Italic,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun animatedGradientBackground(colorScheme: ColorScheme): Brush {
    val transition = rememberInfiniteTransition(label = "Gradient Animation")

    val color1 by transition.animateColor(
        initialValue = colorScheme.primary,
        targetValue = colorScheme.secondary,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Color1"
    )

    val color2 by transition.animateColor(
        initialValue = colorScheme.tertiary,
        targetValue = colorScheme.primaryContainer,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Color2"
    )

    return Brush.verticalGradient(listOf(color1, color2))
}

@Composable
private fun circularProgress(progress: Float, maxProgress: Float, colorScheme: ColorScheme) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress / maxProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = "Progress Animation"
    )

    CircularProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.size(160.dp),
        strokeWidth = 6.dp,
        strokeCap = StrokeCap.Round,
        // color = colorScheme.onBackground.copy(alpha = 0.8f),
        // trackColor = colorScheme.surfaceVariant.copy(alpha = 0.4f)
    )
}