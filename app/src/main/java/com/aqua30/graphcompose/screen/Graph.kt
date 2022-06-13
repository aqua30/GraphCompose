package com.aqua30.graphcompose.screen

import android.graphics.Paint
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
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
    val random = Random()
    val points = (0..9).map {
        var num = random.nextInt(350)
        if (num <= 50)
            num += 100
        num.toFloat()
    }
    Log.e("points","$points")
    val paddingSpace = 16.dp
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    Box(
        modifier = modifier
            .background(Color.White)
            .padding(start = 8.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
            onDraw = {
                val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
                val yAxisSpace = size.height / yValues.size
                for (i in xValues.indices) {
                    drawContext.canvas.nativeCanvas.drawText(
                        "${xValues[i]}",
                        xAxisSpace * (i + 1),
                        size.height - 50,
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

                Log.e("yAxisSpace","$yAxisSpace")
                Log.e("xAxisSpace","$xAxisSpace")
                val stroke = Path().apply {
                    for (i in points.indices) {
                        val x1 = xAxisSpace * xValues[i]
                        val y1 = size.height - (yAxisSpace * (points[i]/yStep))
                        val x2 = xAxisSpace * (xValues.getOrNull(i + 1) ?: xValues.last())
                        val y2 = size.height - (yAxisSpace * ((points.getOrNull(i+1)?: points.last())/yStep))
                        Log.e("coordinates","x1 = $x1, y1 = $y1,x2 = $x2, y2 = $y2")
                        if (i == 0)
                            moveTo(x1, y1)
                        quadraticBezierTo(
                            x1, y1,
                            (x1 + x2)/2f, (y1 + y2)/2f
                        )
                        quadraticBezierTo(
                            (x1 + x2)/2f, (y1 + y2)/2f,
                            (x1 + (3 * x2))/(4f),(y1 + (3 * y2))/(4f)
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
                            Color.Transparent
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

                for (i in points.indices) {
                    val x1 = xAxisSpace * xValues[i]
                    val y1 = size.height - (yAxisSpace * (points[i]/yStep))
                    val x2 = xAxisSpace * (xValues.getOrNull(i + 1) ?: xValues.last())
                    val y2 = size.height - (yAxisSpace * ((points.getOrNull(i+1)?: points.last())/yStep))
                    Log.e("coordinates","x1 = $x1, y1 = $y1,x2 = $x2, y2 = $y2")
                    drawCircle(
                        color = Color.Red,
                        radius = 10f,
                        center = Offset(x1,y1)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun previewGraph() {
    GraphComposeTheme {
        Graph(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}