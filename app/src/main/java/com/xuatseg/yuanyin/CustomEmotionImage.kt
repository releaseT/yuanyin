package com.xuatseg.yuanyin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * 表情图片显示组件，优先使用自定义SVG表情，如果没有则使用默认表情
 */
@Composable
fun EmotionImage(
    emotionType: EmotionType,
    modifier: Modifier = Modifier,
    size: Int = 80
) {
    val context = LocalContext.current
    
    // 异步加载SVG表情图片
    val customEmotionBitmap = produceState<ImageBitmap?>(initialValue = null, emotionType) {
        value = EmotionImageProvider.loadEmotionBitmap(context, emotionType)
    }.value
    
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        if (customEmotionBitmap != null) {
            // 显示自定义SVG表情图片
            Image(
                bitmap = customEmotionBitmap,
                contentDescription = "Custom ${emotionType.name.lowercase()} emotion",
                modifier = Modifier.size(size.dp)
            )
        } else {
            // 显示默认的矢量图表情
            Image(
                imageVector = RobotEmotions.getEmotion(emotionType),
                contentDescription = "Robot ${emotionType.name.lowercase()} face",
                modifier = Modifier.size(size.dp)
            )
        }
    }
} 