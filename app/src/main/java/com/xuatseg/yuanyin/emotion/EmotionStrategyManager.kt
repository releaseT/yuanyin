package com.xuatseg.yuanyin.emotion

import androidx.compose.runtime.mutableStateOf
import com.xuatseg.yuanyin.EmotionType
import com.xuatseg.yuanyin.EnvironmentType
import com.xuatseg.yuanyin.RobotEmotions
import com.xuatseg.yuanyin.SystemStatusType
import com.xuatseg.yuanyin.TaskComplexity
import com.xuatseg.yuanyin.UserInteractionType

/**
 * 表情策略管理器 - 负责管理不同的表情策略并提供当前表情
 */
class EmotionStrategyManager {
    // 可用的策略列表
    private val availableStrategies = mapOf(
        "default" to DefaultEmotionStrategy(),
        "conservative" to ConservativeEmotionStrategy(),
        "expressive" to ExpressiveEmotionStrategy()
    )
    
    // 当前使用的策略
    private var currentStrategy = availableStrategies["default"]!!
    
    // 当前情绪状态
    private var currentState = EmotionState()
    
    // 当前表情，可被观察
    val currentEmotion = mutableStateOf(EmotionType.NEUTRAL)
    
    /**
     * 切换到指定的表情策略
     */
    fun switchStrategy(strategyKey: String): Boolean {
        val strategy = availableStrategies[strategyKey] ?: return false
        currentStrategy = strategy
        // 应用新策略到当前状态
        updateEmotion()
        return true
    }
    
    /**
     * 获取当前策略名称
     */
    fun getCurrentStrategyName(): String {
        return currentStrategy.strategyName
    }
    
    /**
     * 获取所有可用策略的名称和键
     */
    fun getAvailableStrategies(): List<Pair<String, String>> {
        return availableStrategies.map { (key, strategy) -> 
            Pair(key, strategy.strategyName)
        }
    }
    
    /**
     * 更新表情状态并应用当前策略
     */
    private fun updateEmotion() {
        // 更新当前表情
        val newEmotion = currentStrategy.getEmotionForState(currentState.copy(
            currentEmotion = currentEmotion.value
        ))
        
        if (newEmotion != currentEmotion.value) {
            currentEmotion.value = newEmotion
        }
    }
    
    /**
     * 获取当前表情的ImageVector
     */
    fun getCurrentEmotionVector() = RobotEmotions.getEmotion(currentEmotion.value)
    
    /**
     * 下面是各种更新状态的方法
     */
    
    // 更新用户交互状态
    fun updateUserInteraction(interaction: UserInteractionType?) {
        currentState = currentState.copy(userInteraction = interaction)
        updateEmotion()
    }
    
    // 更新系统状态
    fun updateSystemStatus(status: SystemStatusType?) {
        currentState = currentState.copy(systemStatus = status)
        updateEmotion()
    }
    
    // 更新任务完成状态
    fun updateTaskCompletion(success: Boolean?, complexity: TaskComplexity = TaskComplexity.MEDIUM) {
        currentState = currentState.copy(
            taskSuccess = success,
            taskComplexity = complexity
        )
        updateEmotion()
    }
    
    // 更新环境状态
    fun updateEnvironment(environment: EnvironmentType?) {
        currentState = currentState.copy(environmentType = environment)
        updateEmotion()
    }
    
    // 更新电池状态
    fun updateBatteryStatus(level: Int?, isCharging: Boolean?) {
        currentState = currentState.copy(
            batteryLevel = level,
            isCharging = isCharging
        )
        updateEmotion()
    }
    
    // 更新维护模式
    fun setMaintenanceMode(inMaintenance: Boolean) {
        currentState = currentState.copy(isInMaintenanceMode = inMaintenance)
        updateEmotion()
    }
    
    // 重置所有状态
    fun resetState() {
        currentState = EmotionState()
        updateEmotion()
    }
    
    // 直接设置表情（覆盖策略）
    fun setEmotion(emotionType: EmotionType) {
        currentEmotion.value = emotionType
    }
    
    companion object {
        // 单例模式
        private var instance: EmotionStrategyManager? = null
        
        fun getInstance(): EmotionStrategyManager {
            if (instance == null) {
                instance = EmotionStrategyManager()
            }
            return instance!!
        }
    }
} 