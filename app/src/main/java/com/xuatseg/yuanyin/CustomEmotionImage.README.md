# CustomEmotionImage组件

`CustomEmotionImage.kt`文件定义了一个自定义的Composable组件，用于显示SVG格式的表情图片。

## 功能描述

`EmotionImage`组件会优先尝试加载和显示自定义的SVG表情图片，如果加载失败则会回退到使用默认的矢量图表情。

## 组件属性

- `emotionType: EmotionType` - 要显示的表情类型
- `modifier: Modifier` - 组件修饰符，默认为`Modifier`
- `size: Int` - 表情图片的大小，默认为80dp

## 使用示例

```kotlin
// 显示标准大小的中性表情
EmotionImage(
    emotionType = EmotionType.NEUTRAL
)

// 显示自定义大小的开心表情
EmotionImage(
    emotionType = EmotionType.HAPPY,
    modifier = Modifier.padding(8.dp),
    size = 60
)
```

## 工作原理

1. 组件会使用`produceState`异步加载SVG表情图片
2. 如果成功加载自定义SVG表情，则显示该表情
3. 如果加载失败，则回退显示默认矢量表情
4. 所有的表情图片都居中显示在一个Box容器中

## 依赖关系

- 依赖`EmotionImageProvider`加载SVG表情图片
- 依赖`RobotEmotions`提供默认矢量表情 