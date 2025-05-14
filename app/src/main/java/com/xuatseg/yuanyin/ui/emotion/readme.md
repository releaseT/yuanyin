# 机器人表情系统

本模块集成了 emotion-kit 外部库，提供了丰富的机器人表情展示功能。

## 特性

- 六种基本表情：怀疑/不满/生气、开心/友善、满意/平和、中性/待机、跌落/紧急/惊慌、清洁/维修状态
- 基于策略模式的情绪管理系统
- 三种内置表情策略：默认策略、保守策略和情绪化策略
- 支持多种业务场景：用户交互、系统状态、任务完成、环境检测等
- 统一的青蓝色表情设计 (0xFF00CCFF)
- Jetpack Compose 矢量图形实现，高效渲染

## 使用方法

### 在 ViewModel 中使用

机器人表情功能已集成到 `MainViewModel` 中，可以通过以下方式使用：

```kotlin
// 获取当前表情
val robotExpression = viewModel.robotExpression

// 更新系统状态
viewModel.updateConnectionStatus(true) // 自动更新为满意表情
viewModel.updateBatteryLevel(10) // 电量低时自动更新为怀疑表情
viewModel.setError("系统错误") // 自动更新为惊慌表情

// 语音唤醒时更新表情
viewModel.updateVoiceWakeExpression() // 更新为问候表情

// 直接设置表情
viewModel.setEmotion(EmotionType.HAPPY) // 设置为开心表情

// 切换表情策略
viewModel.switchEmotionStrategy("conservative") // 切换到保守策略
```

### 在 UI 中使用

在 `MainScreen` 中已集成表情显示组件，点击中央表情区域可以打开表情选择弹窗：

1. 可以直接选择表情
2. 可以切换不同的表情策略

### 自定义表情逻辑

如需自定义更多表情行为，可以修改 `RobotEmotionManager` 类，或者在 `MainViewModel` 中添加新的表情相关方法。

## 表情类型

- `EmotionType.HAPPY` - 开心/友善
- `EmotionType.NEUTRAL` - 中性/待机
- `EmotionType.SATISFIED` - 满意/平和 
- `EmotionType.SUSPICIOUS` - 怀疑/不满/生气
- `EmotionType.PANIC` - 跌落/紧急/惊慌
- `EmotionType.REPAIR` - 清洁/维修状态

## 表情策略

- `default` - 默认策略：根据状态直接映射表情
- `conservative` - 保守策略：偏向使用中性表情，不易显示极端情绪
- `expressive` - 表现策略：表情变化更丰富，反应更强烈

## 拓展功能

### 添加新表情

目前使用的是 emotion-kit 内置的六种基本表情。如需添加新表情，需要修改 emotion-kit 库。

### 自定义表情策略

通过实现 `EmotionStrategy` 接口可以创建自定义表情策略：

```kotlin
class MyCustomStrategy : EmotionStrategy {
    override val strategyName: String = "自定义策略"
    
    override fun getEmotionForState(state: EmotionState): EmotionType {
        // 实现自定义逻辑
        return EmotionType.NEUTRAL
    }
}
```

然后在 `RobotEmotionManager` 中注册该策略。 