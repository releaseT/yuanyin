package com.xuatseg.yuanyin.emotion

import com.xuatseg.yuanyin.EmotionType
import com.xuatseg.yuanyin.SystemStatusType
import com.xuatseg.yuanyin.TaskComplexity
import com.xuatseg.yuanyin.UserInteractionType
import com.xuatseg.yuanyin.EnvironmentType

/**
 * 表情策略接口 - 定义所有表情策略的基本行为
 */
interface EmotionStrategy {
    fun getEmotionForState(state: EmotionState): EmotionType
    val strategyName: String
}

/**
 * 表情状态 - 包含可能影响表情选择的所有因素
 */
data class EmotionState(
    val userInteraction: UserInteractionType? = null,
    val systemStatus: SystemStatusType? = null,
    val taskSuccess: Boolean? = null,
    val taskComplexity: TaskComplexity? = null,
    val environmentType: EnvironmentType? = null,
    val batteryLevel: Int? = null,
    val isCharging: Boolean? = null,
    val isInMaintenanceMode: Boolean = false,
    val currentEmotion: EmotionType = EmotionType.NEUTRAL,
    // 可以根据需要扩展其他状态因素
)

/**
 * 默认表情策略 - 基于当前状态的直接映射
 */
class DefaultEmotionStrategy : EmotionStrategy {
    override val strategyName: String = "默认表情策略"
    
    override fun getEmotionForState(state: EmotionState): EmotionType {
        // 优先级处理
        
        // 1. 如果在维护模式
        if (state.isInMaintenanceMode) {
            return EmotionType.REPAIR
        }
        
        // 2. 如果系统状态是错误状态
        if (state.systemStatus == SystemStatusType.ERROR) {
            return EmotionType.PANIC
        }
        
        // 3. 如果电池电量低且不在充电
        if (state.batteryLevel != null && state.batteryLevel < 15 && state.isCharging == false) {
            return EmotionType.SUSPICIOUS
        }
        
        // 4. 处理其他普通状态
        return when {
            // 用户交互优先
            state.userInteraction != null -> handleUserInteraction(state.userInteraction)
            
            // 系统状态次之
            state.systemStatus != null -> handleSystemStatus(state.systemStatus)
            
            // 任务完成状态
            state.taskSuccess != null -> handleTaskCompletion(state.taskSuccess, state.taskComplexity ?: TaskComplexity.MEDIUM)
            
            // 环境状态
            state.environmentType != null -> handleEnvironment(state.environmentType)
            
            // 默认情况保持当前表情
            else -> state.currentEmotion
        }
    }
    
    private fun handleUserInteraction(interactionType: UserInteractionType): EmotionType {
        return when (interactionType) {
            UserInteractionType.GREETING -> EmotionType.HAPPY
            UserInteractionType.QUESTION -> EmotionType.NEUTRAL
            UserInteractionType.COMPLEX_QUERY -> EmotionType.SUSPICIOUS
            UserInteractionType.APPRECIATION -> EmotionType.SATISFIED
        }
    }
    
    private fun handleSystemStatus(systemStatus: SystemStatusType): EmotionType {
        return when (systemStatus) {
            SystemStatusType.NORMAL -> EmotionType.NEUTRAL
            SystemStatusType.ERROR -> EmotionType.PANIC
            SystemStatusType.MAINTENANCE -> EmotionType.REPAIR
            SystemStatusType.LOW_BATTERY -> EmotionType.SUSPICIOUS
            SystemStatusType.CHARGING -> EmotionType.SATISFIED
        }
    }
    
    private fun handleTaskCompletion(success: Boolean, complexity: TaskComplexity): EmotionType {
        return when {
            success && complexity == TaskComplexity.HIGH -> EmotionType.HAPPY
            success -> EmotionType.SATISFIED
            !success && complexity == TaskComplexity.HIGH -> EmotionType.PANIC
            else -> EmotionType.SUSPICIOUS
        }
    }
    
    private fun handleEnvironment(environmentType: EnvironmentType): EmotionType {
        return when (environmentType) {
            EnvironmentType.NORMAL -> EmotionType.NEUTRAL
            EnvironmentType.HAZARDOUS -> EmotionType.PANIC
            EnvironmentType.UNFAMILIAR -> EmotionType.SUSPICIOUS
            EnvironmentType.OPTIMAL -> EmotionType.HAPPY
        }
    }
}

/**
 * 保守表情策略 - 偏向使用中性表情，不轻易显示极端情绪
 */
class ConservativeEmotionStrategy : EmotionStrategy {
    override val strategyName: String = "保守表情策略"
    
    override fun getEmotionForState(state: EmotionState): EmotionType {
        // 即使在特殊情况下，也保持更加保守的表情
        
        // 维护模式是必须的
        if (state.isInMaintenanceMode) {
            return EmotionType.REPAIR
        }
        
        // 其他情况更倾向于使用中性或满意表情
        return when {
            state.systemStatus == SystemStatusType.ERROR -> EmotionType.SUSPICIOUS  // 不使用PANIC
            state.userInteraction == UserInteractionType.GREETING -> EmotionType.SATISFIED // 不使用HAPPY
            state.userInteraction == UserInteractionType.APPRECIATION -> EmotionType.SATISFIED
            state.taskSuccess == true -> EmotionType.SATISFIED
            state.taskSuccess == false -> EmotionType.NEUTRAL // 失败也不表现得太明显
            state.environmentType == EnvironmentType.HAZARDOUS -> EmotionType.SUSPICIOUS // 不使用PANIC
            else -> EmotionType.NEUTRAL
        }
    }
}

/**
 * 情绪化表情策略 - 表情变化更丰富，反应更强烈
 */
class ExpressiveEmotionStrategy : EmotionStrategy {
    override val strategyName: String = "情绪化表情策略"
    
    override fun getEmotionForState(state: EmotionState): EmotionType {
        // 情绪表现更加丰富和强烈
        
        // 优先级处理特定情况
        if (state.isInMaintenanceMode) {
            return EmotionType.REPAIR
        }
        
        if (state.systemStatus == SystemStatusType.ERROR) {
            return EmotionType.PANIC
        }
        
        if (state.batteryLevel != null && state.batteryLevel < 20) {
            return if (state.isCharging == true) EmotionType.SATISFIED else EmotionType.PANIC
        }
        
        // 一般情况处理，表情更加情绪化
        return when {
            state.userInteraction == UserInteractionType.GREETING -> EmotionType.HAPPY
            state.userInteraction == UserInteractionType.QUESTION -> EmotionType.SUSPICIOUS // 更好奇
            state.userInteraction == UserInteractionType.COMPLEX_QUERY -> EmotionType.PANIC // 更紧张
            state.userInteraction == UserInteractionType.APPRECIATION -> EmotionType.HAPPY // 更高兴
            
            state.taskSuccess == true -> EmotionType.HAPPY // 总是非常高兴
            state.taskSuccess == false -> EmotionType.SUSPICIOUS // 总是明显不满
            
            state.environmentType == EnvironmentType.UNFAMILIAR -> EmotionType.PANIC // 更害怕陌生环境
            state.environmentType == EnvironmentType.OPTIMAL -> EmotionType.HAPPY
            
            else -> state.currentEmotion
        }
    }
} 