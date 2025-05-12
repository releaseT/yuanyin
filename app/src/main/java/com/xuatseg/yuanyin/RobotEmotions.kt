package com.xuatseg.yuanyin

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Enum defining different emotion types for the robot
 */
enum class EmotionType {
    SUSPICIOUS, // 怀疑/不满/生气
    HAPPY,      // 开心/友善
    SATISFIED,  // 满意/平和
    NEUTRAL,    // 中性/待机
    PANIC,      // 跌落/紧急/惊慌
    REPAIR      // 清洁/维修状态
}

/**
 * A utility class that provides access to robot emotion vectors
 */
object RobotEmotions {
    // 定义机器人的统一青蓝色
    private val robotCyanBlue = Color(0xFF00CCFF)
    
    /**
     * Gets the appropriate emotion vector based on the emotion type
     */
    fun getEmotion(emotionType: EmotionType): ImageVector {
        return when (emotionType) {
            EmotionType.SUSPICIOUS -> suspiciousFace
            EmotionType.HAPPY -> happyFace
            EmotionType.SATISFIED -> satisfiedFace
            EmotionType.NEUTRAL -> neutralFace
            EmotionType.PANIC -> panicFace
            EmotionType.REPAIR -> repairFace
        }
    }

    // 怀疑/不满/生气
    val suspiciousFace = ImageVector.Builder(
        name = "RobotSuspiciousFace",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        // 面罩（黑色）
        path(
            fill = SolidColor(Color.Black)
        ) {
            RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
        }

        // 左眼（小方块）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(44f, 48f, 52f, 56f)
        }

        // 右眼（小方块）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(72f, 48f, 80f, 56f)
        }

        // 眉毛（斜线）- 生气的表情
        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 3f
        ) {
            moveTo(42f, 40f)
            lineTo(52f, 44f)
        }

        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 3f
        ) {
            moveTo(76f, 40f)
            lineTo(86f, 44f)
        }

        // 嘴巴（不高兴的线）
        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 3f
        ) {
            moveTo(50f, 76f)
            lineTo(78f, 76f)
        }
    }.build()

    // 开心/友善
    val happyFace = ImageVector.Builder(
        name = "RobotHappyFace",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        // 面罩（黑色）
        path(
            fill = SolidColor(Color.Black)
        ) {
            RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
        }

        // 左眼（圆形）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            moveTo(48f, 52f)
            lineTo(48f, 48f)
            lineTo(52f, 48f)
            lineTo(52f, 52f)
            close()
        }

        // 右眼（圆形）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            moveTo(76f, 52f)
            lineTo(76f, 48f)
            lineTo(80f, 48f)
            lineTo(80f, 52f)
            close()
        }

        // 嘴巴（微笑 - 使用多个线段模拟曲线）
        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 3f
        ) {
            moveTo(50f, 76f)
            lineTo(57f, 80f)
            lineTo(64f, 82f)
            lineTo(71f, 80f)
            lineTo(78f, 76f)
        }
    }.build()

    // 满意/平和
    val satisfiedFace = ImageVector.Builder(
        name = "RobotSatisfiedFace",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        // 面罩（黑色）
        path(
            fill = SolidColor(Color.Black)
        ) {
            RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
        }

        // 左眼（圆形）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            moveTo(44f, 52f)
            lineTo(44f, 48f)
            lineTo(52f, 48f)
            lineTo(52f, 52f)
            close()
        }

        // 右眼（圆形）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            moveTo(72f, 52f)
            lineTo(72f, 48f)
            lineTo(80f, 48f)
            lineTo(80f, 52f)
            close()
        }

        // 嘴巴（平和微笑 - 使用多个线段模拟轻微曲线）
        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 3f
        ) {
            moveTo(50f, 76f)
            lineTo(57f, 77f)
            lineTo(64f, 78f)
            lineTo(71f, 77f)
            lineTo(78f, 76f)
        }
    }.build()

    // 中性/待机
    val neutralFace = ImageVector.Builder(
        name = "RobotNeutralFace",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        // 面罩（黑色）
        path(
            fill = SolidColor(Color.Black)
        ) {
            RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
        }

        // 左眼（方形，白色表示待机状态）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(44f, 48f, 52f, 56f)
        }

        // 右眼（方形，白色表示待机状态）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(72f, 48f, 80f, 56f)
        }

        // 嘴巴（中性线）
        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 3f
        ) {
            moveTo(50f, 76f)
            lineTo(78f, 76f)
        }
    }.build()

    // 跌落/紧急/惊慌
    val panicFace = ImageVector.Builder(
        name = "RobotPanicFace",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        // 面罩（黑色）
        path(
            fill = SolidColor(Color.Black)
        ) {
            RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
        }

        // 左眼（感叹号形状）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(44f, 40f, 52f, 52f)
            moveTo(44f, 56f)
            lineTo(52f, 56f)
            lineTo(52f, 58f)
            lineTo(44f, 58f)
            close()
        }

        // 右眼（感叹号形状）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(72f, 40f, 80f, 52f)
            moveTo(72f, 56f)
            lineTo(80f, 56f)
            lineTo(80f, 58f)
            lineTo(72f, 58f)
            close()
        }

        // 嘴巴（惊讶的O形）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            moveTo(59f, 76f)
            lineTo(59f, 70f)
            lineTo(69f, 70f)
            lineTo(69f, 76f)
            close()
        }
    }.build()

    // 清洁/维修状态
    val repairFace = ImageVector.Builder(
        name = "RobotRepairFace",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        // 面罩（黑色）
        path(
            fill = SolidColor(Color.Black)
        ) {
            RoundRect(Rect(16f, 16f, 112f, 96f), 16f, 16f)
        }

        // 左眼（扳手形状指示维修）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(40f, 44f, 56f, 48f)
            Rect(44f, 48f, 48f, 60f)
        }

        // 右眼（螺丝刀形状指示维修）
        path(
            fill = SolidColor(robotCyanBlue)
        ) {
            Rect(72f, 44f, 88f, 48f)
            Rect(78f, 48f, 82f, 60f)
        }

        // 嘴巴（水平条纹表示维修状态）
        path(
            stroke = SolidColor(robotCyanBlue),
            strokeLineWidth = 2f
        ) {
            moveTo(50f, 72f)
            lineTo(78f, 72f)
            moveTo(50f, 76f)
            lineTo(78f, 76f)
            moveTo(50f, 80f)
            lineTo(78f, 80f)
        }
    }.build()
} 