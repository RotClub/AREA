package org.rotclub.area.composes

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun skeletonLoading(display: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (display) {
        val shimmerColors = listOf(
            FrispyTheme.Surface800,
            FrispyTheme.Surface600,
            FrispyTheme.Surface800
        )

        val transition = rememberInfiniteTransition()
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000), repeatMode = RepeatMode.Reverse
            )
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@Composable
fun Modifier.skeletonLoading(display: Boolean = true, targetValue: Float = 1000f) =
    background(org.rotclub.area.composes.skeletonLoading(display, targetValue))
