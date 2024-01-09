package com.mioshek.chartplanner.views.graphs

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aqua30.graphcompose.models.GraphAppearance

/**
 * Created by Saurabh url: https://github.com/aqua30/GraphCompose forked by Mioshek https://github.com/Mioshek
 */

@Composable
fun Graph(
    modifier : Modifier,
    xValues: List<Int>,
    yValues: List<Int>,
    points: List<Float>,
    paddingSpace: Dp,
    verticalStep: Int,
    graphAppearance: GraphAppearance
) {
    val controlPoints1 = mutableListOf<PointF>()
    val controlPoints2 = mutableListOf<PointF>()
    val coordinates = mutableListOf<PointF>()
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = graphAppearance.graphAxisColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(graphAppearance.backgroundColor)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
            val yAxisSpace = size.height / yValues.size
            /** placing x axis points */
            for (i in xValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${xValues[i]}",
                    xAxisSpace * (i + 1),
                    size.height - 30,
                    textPaint
                )
            }
            /** placing y axis points */
            for (i in yValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${yValues[i]}",
                    paddingSpace.toPx() / 2f,
                    size.height - yAxisSpace * (i + 1),
                    textPaint
                )
            }
            /** placing our x axis points */
            for (i in points.indices) {
                val x1 = xAxisSpace * xValues[i]
                val y1 = size.height - (yAxisSpace * (points[i]/verticalStep.toFloat()))
                coordinates.add(PointF(x1,y1))
                /** drawing circles to indicate all the points */
                if (graphAppearance.isCircleVisible){
                    drawCircle(
                        color = graphAppearance.circleColor,
                        radius = 10f,
                        center = Offset(x1,y1)
                    )
                }
            }
            /** calculating the connection points */
            for (i in 1 until coordinates.size) {
                controlPoints1.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i - 1].y))
                controlPoints2.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y))
            }
            /** drawing the path */
            val stroke = Path().apply {
                reset()
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        controlPoints1[i].x,controlPoints1[i].y,
                        controlPoints2[i].x,controlPoints2[i].y,
                        coordinates[i + 1].x,coordinates[i + 1].y
                    )
                }
            }
            /** filling the area under the path */
            val fillPath = android.graphics.Path(stroke.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(xAxisSpace * xValues.last(), size.height - yAxisSpace)
                    lineTo(xAxisSpace, size.height - yAxisSpace)
                    close()
                }
            if (graphAppearance.iscolorAreaUnderChart){
                drawPath(
                    fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            graphAppearance.colorAreaUnderChart,
                            Color.Transparent,
                        ),
                        endY = size.height - yAxisSpace
                    ),
                )
            }
            drawPath(
                stroke,
                color = graphAppearance.graphColor,
                style = Stroke(
                    width = graphAppearance.graphThickness,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}