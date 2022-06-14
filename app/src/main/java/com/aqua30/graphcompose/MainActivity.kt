package com.aqua30.graphcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aqua30.graphcompose.screen.Graph
import com.aqua30.graphcompose.ui.theme.GraphComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphComposeTheme {
                val yStep = 50
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.DarkGray)
                ) {
                    Graph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        xValues = (0..9).map { it + 1 },
                        yValues = (0..6).map { (it + 1) * yStep },
                        points = listOf(150f,100f,250f,200f,330f,300f,90f,120f,285f,199f),
                        paddingSpace = 16.dp,
                        verticalStep = yStep
                    )
                }
            }
        }
    }
}