package com.xuatseg.yuanyin

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// 此文件已弃用，所有情感表达已移至RobotEmotions.kt
// 仅保留此类以保证兼容性

val RobotSuspiciousFace = ImageVector.Builder(
    name = "RobotSuspiciousFace",
    defaultWidth = 128.dp,
    defaultHeight = 128.dp,
    viewportWidth = 128f,
    viewportHeight = 128f
).apply {
    // 头部外壳（白色大轮廓）
//    path(
//        fill = SolidColor(Color.White),
//        stroke = SolidColor(Color.Black),
//        strokeLineWidth = 4f
//    ) {
//        arcTo(
//            left = 16f,
//            top = 16f,
//            right = 112f,
//            bottom = 96f,
//            startAngleDegrees = 0f,
//            sweepAngleDegrees = 360f,
//            forceMoveTo = false
//        )
//    }

    // 面罩（黑色）
    path(
        fill = SolidColor(Color.Black)
    ) {
        RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
    }

    // 左眼（小方块）
    path(
        fill = SolidColor(Color.White)
    ) {
        Rect(44f, 48f, 52f, 56f)
    }

    // 右眼（小方块）
    path(
        fill = SolidColor(Color.White)
    ) {
        Rect(72f, 48f, 80f, 56f)
    }

    // 眉毛（斜线）
    path(
        stroke = SolidColor(Color.White),
        strokeLineWidth = 3f
    ) {
        moveTo(42f, 40f)
        lineTo(52f, 44f)
    }

    // 脖子（小矩形）
    path(
        fill = SolidColor(Color.White),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 2f
    ) {
        Rect(56f, 96f, 72f, 116f)
    }

}.build()
