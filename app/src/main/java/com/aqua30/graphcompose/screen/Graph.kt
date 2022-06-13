package com.aqua30.graphcompose.screen

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aqua30.graphcompose.ui.theme.GraphComposeTheme
import java.util.*

/**
 * Created by Saurabh
 */
@Composable
fun Graph(
    modifier : Modifier
) {
    val xValues = (0..9).map { it + 1 }
    val yStep = 50
    val yValues = (0..6).map { (it + 1) * yStep }
    val points = listOf(150,100,250,200,330,300,90,120,285,199)
    val paddingSpace = 16.dp
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    val connectionPoints1 = remember {
        mutableListOf<PointF>()
    }
    val connectionPoints2 = remember {
        mutableListOf<PointF>()
    }
    val coordinates = remember {
        mutableListOf<PointF>()
    }

    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
            val yAxisSpace = size.height / yValues.size
            for (i in xValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${xValues[i]}",
                    xAxisSpace * (i + 1),
                    size.height - 30,
                    textPaint
                )
            }
            for (i in yValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${yValues[i]}",
                    paddingSpace.toPx() / 2f,
                    size.height - yAxisSpace * (i + 1),
                    textPaint
                )
            }
            for (i in points.indices) {
                val x1 = xAxisSpace * xValues[i]
                val y1 = size.height - (yAxisSpace * (points[i]/yStep.toFloat()))
                coordinates.add(PointF(x1,y1))
            }
            for (i in 1 until coordinates.size) {
                connectionPoints1.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i - 1].y))
                connectionPoints2.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y))
            }

            val stroke = Path().apply {
                reset()
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        connectionPoints1[i].x,connectionPoints1[i].y,
                        connectionPoints2[i].x,connectionPoints2[i].y,
                        coordinates[i + 1].x,coordinates[i + 1].y
                    )
                }
            }

            val fillPath = android.graphics.Path(stroke.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(xAxisSpace * xValues.last(), size.height - yAxisSpace)
                    lineTo(xAxisSpace, size.height - yAxisSpace)
                    close()
                }
            drawPath(
                fillPath,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Cyan,
                        Color.Transparent,
                    ),
                    endY = size.height - yAxisSpace
                ),
            )
            drawPath(
                stroke,
                color = Color.Black,
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round
                )
            )

            for (i in coordinates.indices) {
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = Offset(coordinates[i].x,coordinates[i].y)
                )
            }
        }
    }
}

//@Preview
@Composable
fun previewCurve() {
    GraphComposeTheme {
        Graph(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}