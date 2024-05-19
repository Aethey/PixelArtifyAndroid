package com.example.image_to_pixel.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewLoadingAnimation() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color =  MaterialTheme.colorScheme.primary
    ) {
        LoadingAnimation()
    }
}

@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val twinCircleAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "loading"
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .size(120.dp)
                .padding(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(15.dp)
                    .scale(twinCircleAnimation)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(15.dp)
                    .scale(twinCircleAnimation)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}