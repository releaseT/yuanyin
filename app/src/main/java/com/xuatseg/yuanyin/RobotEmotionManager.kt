package com.xuatseg.yuanyin

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Manager class for controlling robot emotions based on different business scenarios
 */
class RobotEmotionManager {
    
    // Current emotion state that can be observed
    val currentEmotion = mutableStateOf(EmotionType.NEUTRAL)
    
    /**
     * Change the robot's emotion
     */
    fun setEmotion(emotionType: EmotionType) {
        currentEmotion.value = emotionType
    }
    
    /**
     * Get the current emotion's ImageVector
     */
    fun getCurrentEmotionVector(): ImageVector {
        return RobotEmotions.getEmotion(currentEmotion.value)
    }
    
    /**
     * Business scenario: User interaction with robot
     */
    fun handleUserInteraction(interactionType: UserInteractionType) {
        when (interactionType) {
            UserInteractionType.GREETING -> setEmotion(EmotionType.HAPPY)
            UserInteractionType.QUESTION -> setEmotion(EmotionType.NEUTRAL)
            UserInteractionType.COMPLEX_QUERY -> setEmotion(EmotionType.SUSPICIOUS)
            UserInteractionType.APPRECIATION -> setEmotion(EmotionType.SATISFIED)
        }
    }
    
    /**
     * Business scenario: System status changes
     */
    fun handleSystemStatus(systemStatus: SystemStatusType) {
        when (systemStatus) {
            SystemStatusType.NORMAL -> setEmotion(EmotionType.NEUTRAL)
            SystemStatusType.ERROR -> setEmotion(EmotionType.PANIC)
            SystemStatusType.MAINTENANCE -> setEmotion(EmotionType.REPAIR)
            SystemStatusType.LOW_BATTERY -> setEmotion(EmotionType.SUSPICIOUS)
            SystemStatusType.CHARGING -> setEmotion(EmotionType.SATISFIED)
        }
    }
    
    /**
     * Business scenario: Task completion status
     */
    fun handleTaskCompletion(success: Boolean, complexity: TaskComplexity = TaskComplexity.MEDIUM) {
        when {
            success && complexity == TaskComplexity.HIGH -> setEmotion(EmotionType.HAPPY)
            success -> setEmotion(EmotionType.SATISFIED)
            !success && complexity == TaskComplexity.HIGH -> setEmotion(EmotionType.PANIC)
            !success -> setEmotion(EmotionType.SUSPICIOUS)
        }
    }
    
    /**
     * Business scenario: Environment detection
     */
    fun handleEnvironmentDetection(environmentType: EnvironmentType) {
        when (environmentType) {
            EnvironmentType.NORMAL -> setEmotion(EmotionType.NEUTRAL)
            EnvironmentType.HAZARDOUS -> setEmotion(EmotionType.PANIC)
            EnvironmentType.UNFAMILIAR -> setEmotion(EmotionType.SUSPICIOUS)
            EnvironmentType.OPTIMAL -> setEmotion(EmotionType.HAPPY)
        }
    }
    
    companion object {
        // Singleton instance
        private var instance: RobotEmotionManager? = null
        
        fun getInstance(): RobotEmotionManager {
            if (instance == null) {
                instance = RobotEmotionManager()
            }
            return instance!!
        }
    }
}

/**
 * Types of user interactions
 */
enum class UserInteractionType {
    GREETING,
    QUESTION,
    COMPLEX_QUERY,
    APPRECIATION
}

/**
 * Types of system statuses
 */
enum class SystemStatusType {
    NORMAL,
    ERROR,
    MAINTENANCE,
    LOW_BATTERY,
    CHARGING
}

/**
 * Task complexity levels
 */
enum class TaskComplexity {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Environment types
 */
enum class EnvironmentType {
    NORMAL,
    HAZARDOUS,
    UNFAMILIAR,
    OPTIMAL
} 