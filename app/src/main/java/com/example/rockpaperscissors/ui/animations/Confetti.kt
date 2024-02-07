package com.example.rockpaperscissors.ui.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.rockpaperscissors.ui.theme.Indigo
import com.example.rockpaperscissors.ui.theme.Orange
import com.example.rockpaperscissors.ui.theme.Violet

@Composable
fun Confetti(color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        // No content
    }
}

@Composable
fun ConfettiRain(onAnimationFinished: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val yOffset by animateDpAsState(
        targetValue = screenHeight * progress,
        label = "Confetti Y Offset"
    )
    val xOffset by animateDpAsState(
        targetValue = if ((progress * 10).toInt() % 2 == 0) 4.dp else (-4).dp,
        label = "Confetti X Offset"
    )

    LaunchedEffect(key1 = true) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(5000, easing = LinearEasing)
        ) { value, _ ->
            progress = value
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = yOffset, x = xOffset),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val colors = listOf(Color.Red, Orange, Color.Yellow, Color.Green, Color.Blue, Indigo, Violet)
        repeat(10) {
            Confetti(color = colors[it % colors.size])
        }
    }

    LaunchedEffect(key1 = progress) {
        if (progress >= 1f) {
            onAnimationFinished()
        }
    }
}