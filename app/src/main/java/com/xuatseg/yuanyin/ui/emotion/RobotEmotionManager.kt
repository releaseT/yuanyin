package com.xuatseg.yuanyin.ui.emotion

import com.airastack.emotionkit.EmotionType
import com.airastack.emotionkit.strategy.EmotionStrategyManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.airastack.emotionkit.RobotEmotions
import androidx.compose.runtime.State
import com.airastack.emotionkit.SystemStatusType
import com.airastack.emotionkit.UserInteractionType
import com.airastack.emotionkit.TaskComplexity

/**
 * Robot emotion manager that adapts the emotion-kit functionality
 * for use in the yuanyin application.
 */
class RobotEmotionManager {
    private val emotionManager = EmotionStrategyManager.getInstance()

    /**
     * Get current emotion type
     */
    fun getCurrentEmotionType() = emotionManager.currentEmotion

    /**
     * Get current emotion vector to display
     */
    fun getCurrentEmotionVector(): ImageVector = emotionManager.getCurrentEmotionVector()

    /**
     * Update emotion based on system status
     */
    fun updateSystemStatus(status: SystemStatus) {
        when (status) {
            SystemStatus.NORMAL -> emotionManager.setEmotion(EmotionType.NEUTRAL)
            SystemStatus.ERROR -> emotionManager.setEmotion(EmotionType.PANIC)
            SystemStatus.LOW_BATTERY -> emotionManager.updateSystemStatus(
                SystemStatusType.LOW_BATTERY
            )
            SystemStatus.MAINTENANCE -> emotionManager.setMaintenanceMode(true)
        }
    }

    /**
     * Update emotion based on user interaction
     */
    fun updateUserInteraction(interaction: UserInteraction) {
        when (interaction) {
            UserInteraction.GREETING -> emotionManager.updateUserInteraction(
                UserInteractionType.GREETING
            )
            UserInteraction.QUESTION -> emotionManager.updateUserInteraction(
                UserInteractionType.QUESTION
            )
            UserInteraction.COMPLEX_QUERY -> emotionManager.updateUserInteraction(
                UserInteractionType.COMPLEX_QUERY
            )
            UserInteraction.APPRECIATION -> emotionManager.updateUserInteraction(
                UserInteractionType.APPRECIATION
            )
        }
    }

    /**
     * Update emotion based on task completion
     */
    fun updateTaskCompletion(success: Boolean, complexity: TaskComplexity = TaskComplexity.MEDIUM) {
        emotionManager.updateTaskCompletion(success, complexity)
    }

    /**
     * Set emotion directly (override strategy)
     */
    fun setEmotion(emotionType: EmotionType) {
        emotionManager.setEmotion(emotionType)
    }

    /**
     * Switch emotion strategy
     */
    fun switchStrategy(strategyName: String) {
        emotionManager.switchStrategy(strategyName)
    }

    /**
     * Reset all states
     */
    fun resetState() {
        emotionManager.resetState()
    }
}

/**
 * System status types for robot
 */
enum class SystemStatus {
    NORMAL,
    ERROR,
    LOW_BATTERY,
    MAINTENANCE
}

/**
 * User interaction types
 */
enum class UserInteraction {
    GREETING,
    QUESTION,
    COMPLEX_QUERY,
    APPRECIATION
} 