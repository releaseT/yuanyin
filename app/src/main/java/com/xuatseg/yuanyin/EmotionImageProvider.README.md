# EmotionImageProvider工具类

`EmotionImageProvider.kt`文件定义了一个工具类，负责加载和处理SVG格式的表情图片。

## 功能描述

`EmotionImageProvider`对象提供了加载SVG表情图片并将其转换为ImageBitmap的功能，用于在应用中显示自定义表情。

## 主要API

- `loadEmotionBitmap(context: Context, emotionType: EmotionType): ImageBitmap?` - 加载指定类型的SVG表情图片

## 表情映射

该类维护了一个表情类型到SVG文件名的映射：

```kotlin
private val emotionAssets = mapOf(
    EmotionType.NEUTRAL to "neutral.svg",
    EmotionType.HAPPY to "happy.svg",
    EmotionType.SATISFIED to "satisfied.svg",
    EmotionType.SUSPICIOUS to "suspicious.svg",
    EmotionType.PANIC to "panic.svg",
    EmotionType.REPAIR to "repair.svg"
)
```

## 工作原理

1. 根据提供的表情类型，查找对应的SVG文件名
2. 从assets/emotion/目录加载SVG文件
3. 使用AndroidSVG库解析SVG内容
4. 将SVG渲染为Bitmap
5. 将Bitmap转换为ImageBitmap并返回

## 使用示例

```kotlin
val context = LocalContext.current
val emotionType = EmotionType.HAPPY
val imageBitmap = EmotionImageProvider.loadEmotionBitmap(context, emotionType)

if (imageBitmap != null) {
    // 使用加载的ImageBitmap
    Image(
        bitmap = imageBitmap,
        contentDescription = "Happy emotion"
    )
}
```

## 依赖

- 依赖AndroidSVG库解析和渲染SVG
- 需要在assets/emotion/目录下存放对应的SVG文件 