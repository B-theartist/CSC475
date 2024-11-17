package com.example.helloandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloAndroidApp()
        }
    }
}

@Composable
fun HelloAndroidApp() {
    // Add animation for text movement
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Center the content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Display animated text
        AnimatedHelloText(animatedOffset)
    }
}

@Composable
fun AnimatedHelloText(offset: Float) {
    Text(
        text = "Hello, Android!",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Magenta,
        modifier = Modifier
            .offset(y = offset.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHelloAndroidApp() {
    HelloAndroidApp()
}