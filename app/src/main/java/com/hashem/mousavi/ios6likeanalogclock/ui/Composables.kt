package com.hashem.mousavi.ios6likeanalogclock.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.E
import kotlin.math.pow

@Composable
fun Clock(
    modifier: Modifier
) {

    val infiniteTransition = rememberInfiniteTransition()
    val secondHandAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 60 * 1000,
                easing = LinearEasing
            ),
            initialStartOffset = StartOffset(
                offsetMillis = with(System.currentTimeMillis()) {
                    this.getSecond() * 1000 + this.getMilliSecond()
                },
                offsetType = StartOffsetType.FastForward
            )
        )
    )

    val hourHandAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 12 * 60 * 60 * 1000,
                easing = LinearEasing
            ),
            initialStartOffset = StartOffset(
                offsetMillis = with(System.currentTimeMillis()) {
                    this.getHour() * 60 * 60 * 1000 + this.getSecond() * 1000 + this.getMilliSecond()
                },
                offsetType = StartOffsetType.FastForward
            )
        )
    )

    val animatableMinuteHandleAngle = remember {
        Animatable(initialValue = System.currentTimeMillis().getMinute() * 6f)
    }


    val isSecondReachedToZero by remember {
        //For preventing from unnecessary recomposition
        derivedStateOf {
            secondHandAngle.toInt() % 360 == 0 && System.currentTimeMillis().getSecond() == 0
        }
    }

    val scope = rememberCoroutineScope()
    if (isSecondReachedToZero) {
        LaunchedEffect(Unit) {
            //We have to use scope, otherwise due to leaving the composition of LaunchedEffect, the animation will be cancelled
            scope.launch {
                animatableMinuteHandleAngle.animateTo(
                    targetValue = animatableMinuteHandleAngle.value + 6,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    Canvas(modifier = modifier) {
        val width = this.size.width
        val height = this.size.height
        val bigDegreeLength = 30.dp.toPx()
        val smallDegreeLength = 15.dp.toPx()
        val radius = minOf(width, height) / 2
        val secondHandCircleRadius = 12.dp.toPx()

        val path = Path().apply {
            this.addOval(Rect(topLeft = Offset.Zero, bottomRight = Offset(x = width, y = height)))
        }
        drawPath(
            path = path,
            color = backGroundColor()
        )

        clipPath(path = path) {
            translate(left = width / 2, top = -height) {
                drawCircle(
                    color = darkColor(),
                    center = Offset(x = 0f, y = 0f),
                    radius = radius + height
                )
            }
        }

        repeat(60) { index ->

            rotate(index * 6f) {
                drawLine(
                    color = lightColor(),
                    start = Offset(x = width / 2, y = 0f),
                    end = Offset(
                        x = width / 2,
                        y = if (index % 5 == 0) {
                            bigDegreeLength
                        } else {
                            var x = -((secondHandAngle - index * 6f) / 12f).pow(2)
                            if ((secondHandAngle in 336f..360f) && index >= 1 && index <= 4) {
                                x = -((360f - secondHandAngle + index * 6f) / 12f).pow(2)
                            }
                            smallDegreeLength * (1 + E.toFloat().pow(x))
                        }
                    ),
                    strokeWidth = if (index % 5 == 0) 30f else 10f
                )
            }
        }

        translate(left = width / 2, top = height / 2) {

            //HourHand
            rotate(hourHandAngle, pivot = Offset(x = 0f, y = 0f)) {
                drawLine(
                    color = lightColor(),
                    start = Offset(x = 0f, y = radius / 4),
                    end = Offset(x = 0f, y = -radius + bigDegreeLength + 100f),
                    strokeWidth = 30f
                )
            }

            //MinuteHand
            rotate(
                degrees = animatableMinuteHandleAngle.value,
                pivot = Offset(x = 0f, y = 0f)
            ) {
                drawLine(
                    color = lightColor(),
                    start = Offset(x = 0f, y = radius / 4),
                    end = Offset(x = 0f, y = -radius + bigDegreeLength),
                    strokeWidth = 30f
                )
            }

            //SecondHand
            rotate(secondHandAngle, pivot = Offset(x = 0f, y = 0f)) {

                drawLine(
                    color = redColor(),
                    start = Offset(x = 0f, y = radius / 4),
                    end = Offset(x = 0f, y = 0f),
                    strokeWidth = 8f
                )
                drawCircle(
                    color = redColor(),
                    center = Offset.Zero,
                    radius = 15f
                )

                drawLine(
                    color = redColor(),
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 0f, y = -radius + bigDegreeLength + 20f),
                    strokeWidth = 8f
                )

                drawCircle(
                    color = redColor(),
                    center = Offset(
                        x = 0f,
                        y = -radius + bigDegreeLength + secondHandCircleRadius / 2 + 32f
                    ),
                    radius = secondHandCircleRadius
                )
            }

            drawCircle(
                color = lightColor(),
                center = Offset.Zero,
                radius = 5f
            )

            drawCircle(
                color = backGroundColor(),
                center = Offset.Zero,
                radius = 2f
            )

        }

    }

}

fun backGroundColor() = Color(12, 12, 12, 255)

private fun redColor() = Color(194, 0, 15, 255)

private fun lightColor() = Color(220, 220, 220, 255)

fun darkColor() = Color(27, 27, 27, 255)

val calendar = Calendar.getInstance()
fun Long.getSecond(): Int {
    calendar.timeInMillis = this
    return calendar.get(Calendar.SECOND)
}

fun Long.getMilliSecond(): Int {
    calendar.timeInMillis = this
    return calendar.get(Calendar.MILLISECOND)
}

fun Long.getHour(): Int {
    calendar.timeInMillis = this
    return calendar.get(Calendar.HOUR)
}

fun Long.getMinute(): Int {
    calendar.timeInMillis = this
    return calendar.get(Calendar.MINUTE)
}

@Preview
@Composable
fun ClockPreview() {
    Clock(modifier = Modifier.size(300.dp))
}