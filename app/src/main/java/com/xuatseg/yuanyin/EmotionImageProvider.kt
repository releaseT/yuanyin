package com.xuatseg.yuanyin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.caverock.androidsvg.SVG
import java.io.InputStream

/**
 * 表情图片提供者，负责加载自定义表情图片
 */
object EmotionImageProvider {
    
    // 表情类型与实际文件名的映射
    private val emotionAssets = mapOf(
        EmotionType.NEUTRAL to "neutral.svg",
        EmotionType.HAPPY to "happy.svg",
        EmotionType.SATISFIED to "satisfied.svg",
        EmotionType.SUSPICIOUS to "suspicious.svg",
        EmotionType.PANIC to "panic.svg",
        EmotionType.REPAIR to "repair.svg"
    )
    
    /**
     * 从assets目录加载SVG表情图片
     */
    fun loadEmotionBitmap(context: Context, emotionType: EmotionType): ImageBitmap? {
        val assetFileName = emotionAssets[emotionType] ?: return null
        
        return try {
            val inputStream: InputStream = context.assets.open("emotion/$assetFileName")
            // 解析SVG
            val svg = SVG.getFromInputStream(inputStream)
            inputStream.close()
            
            // 将SVG渲染为Bitmap
            val width = 128
            val height = 128
            val picture = svg.renderToPicture(width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawPicture(picture)
            
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
} 