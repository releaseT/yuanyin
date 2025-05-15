# 原音表情组件

该目录包含了原音表情应用的主要组件和类。

## 主要文件

- `MainActivity.kt` - 应用的主活动，包含表情展示和场景演示两个主要界面
- `EmotionImageProvider.kt` - SVG表情图片加载和处理的工具类
- `CustomEmotionImage.kt` - 自定义表情图片显示的Composable组件
- `RobotEmotions.kt` - 默认矢量表情的定义和生成
- `RobotEmotionManager.kt` - 情感管理器，处理各种业务场景下的表情变化

## 表情类型

应用支持六种基本表情类型：

- `NEUTRAL` - 中性/待机
- `HAPPY` - 开心/友善
- `SATISFIED` - 满意/平和
- `SUSPICIOUS` - 怀疑/不满/生气
- `PANIC` - 跌落/紧急/惊慌
- `REPAIR` - 清洁/维修状态

## 代码结构

应用使用Jetpack Compose构建UI，采用单例模式管理表情状态，并使用SVG渲染库展示高质量的表情图片。 