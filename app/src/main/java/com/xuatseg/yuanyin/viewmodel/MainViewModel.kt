package com.xuatseg.yuanyin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuatseg.yuanyin.model.BotState
import com.xuatseg.yuanyin.model.WorkMode
import com.xuatseg.yuanyin.mode.*
import com.xuatseg.yuanyin.ui.mode.IModeSwitchViewModel
import com.xuatseg.yuanyin.ui.mode.ModeSwitchEvent
import com.xuatseg.yuanyin.ui.mode.ModeSwitchUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.xuatseg.yuanyin.ui.emotion.RobotEmotionManager
import com.xuatseg.yuanyin.ui.emotion.SystemStatus
import com.xuatseg.yuanyin.ui.emotion.UserInteraction
import com.airastack.emotionkit.EmotionType
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay

/**
 * 主视图模型
 */
class MainViewModel(
    private val modeManager: IModeManager,
    private val modePersistence: IModePersistence,
    private val modeMonitor: IModeMonitor
) : ViewModel(), IModeSwitchViewModel {

    // 机器人状态数据流
    private val _botState = MutableStateFlow(BotState())
    val botState: StateFlow<BotState> = _botState.asStateFlow()

    // 模式切换UI状态
    private val _modeSwitchUiState = MutableStateFlow(
        ModeSwitchUiState(
            currentMode = ProcessingMode.LOCAL,
            availableModes = listOf()
        )
    )

    // 模式状态
    private val _modeState = MutableStateFlow<ModeState?>(null)
    
    // 机器人表情管理器
    private val emotionManager = RobotEmotionManager()
    
    // 机器人表情状态流
    private val _robotExpression = MutableStateFlow<ImageVector?>(null)
    val robotExpression: StateFlow<ImageVector?> = _robotExpression.asStateFlow()

    init {
        initializeMode()
        observeModeChanges()
        updateRobotExpression()
    }

    /**
     * 初始化模式
     */
    private fun initializeMode() {
        viewModelScope.launch {
            // 加载保存的模式设置
            modePersistence.loadMode()?.let { (mode, config) ->
                if (modeManager.isModeAvailable(mode)) {
                    modeManager.switchMode(mode)
                }
            }
            updateAvailableModes()
        }
    }

    /**
     * 观察模式变化
     */
    private fun observeModeChanges() {
        viewModelScope.launch {
            modeManager.observeMode().collect { mode ->
                updateModeSwitchUiState(mode)
            }
        }
    }

    /**
     * 更新机器人表情
     */
    private fun updateRobotExpression() {
        viewModelScope.launch {
            // 初始表情设置为中性
            emotionManager.setEmotion(EmotionType.NEUTRAL)
            // 获取当前表情并更新状态
            _robotExpression.value = emotionManager.getCurrentEmotionVector()
            
            // 创建一个协程来监视表情变化
            launch {
                while(true) {
                    // 定期检查表情是否变化并更新
                    _robotExpression.value = emotionManager.getCurrentEmotionVector()
                    delay(100) // 每100毫秒检查一次
                }
            }
        }
    }

    /**
     * 更新可用模式列表
     */
    private suspend fun updateAvailableModes() {
        val availableModes = ProcessingMode.values().filter { mode ->
            modeManager.isModeAvailable(mode)
        }
        _modeSwitchUiState.update { currentState ->
            currentState.copy(availableModes = availableModes)
        }
    }

    /**
     * 更新模式切换UI状态
     */
    private fun updateModeSwitchUiState(mode: ProcessingMode) {
        _modeSwitchUiState.update { currentState ->
            currentState.copy(
                currentMode = mode,
                isLoading = false,
                error = null
            )
        }
    }

    // IModeSwitchViewModel 实现
    override fun getUiState(): Flow<ModeSwitchUiState> = _modeSwitchUiState.asStateFlow()

    override fun handleEvent(event: ModeSwitchEvent) {
        when (event) {
            is ModeSwitchEvent.SwitchMode -> switchMode(event.mode)
            is ModeSwitchEvent.DismissError -> dismissError()
            is ModeSwitchEvent.UpdateConfig -> updateModeConfig(event.config)
        }
    }

    override fun getCurrentModeState(): ModeState {
        return _modeState.value ?: ModeState(
            currentMode = ProcessingMode.LOCAL,
            isAvailable = true,
            status = ModeStatus.INACTIVE
        )
    }

    override fun getAvailableModes(): List<ProcessingMode> {
        return _modeSwitchUiState.value.availableModes
    }

    /**
     * 切换模式
     */
    private fun switchMode(mode: ProcessingMode) {
        viewModelScope.launch {
            try {
                _modeSwitchUiState.update { it.copy(isLoading = true) }

                if (modeManager.isModeAvailable(mode)) {
                    modeManager.switchMode(mode)
                    val config = modeManager.getModeConfig(mode)
                    modePersistence.saveMode(mode, config)

                    // 记录模式切换
                    modeMonitor.recordModeSwitch(
                        fromMode = _modeSwitchUiState.value.currentMode,
                        toMode = mode,
                        duration = 0 // 实际实现中需要计算真实的切换时间
                    )
                    
                    // 更新表情为开心状态
                    emotionManager.setEmotion(EmotionType.HAPPY)
                    delay(2000) // 2秒后恢复中性表情
                    emotionManager.setEmotion(EmotionType.NEUTRAL)
                } else {
                    _modeSwitchUiState.update {
                        it.copy(error = "模式不可用")
                    }
                    // 更新表情为不满状态
                    emotionManager.setEmotion(EmotionType.SUSPICIOUS)
                }
            } catch (e: Exception) {
                _modeSwitchUiState.update {
                    it.copy(error = e.message ?: "切换模式失败")
                }
                // 更新表情为紧急状态
                emotionManager.setEmotion(EmotionType.PANIC)
            } finally {
                _modeSwitchUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * 更新模式配置
     */
    private fun updateModeConfig(config: ModeConfig) {
        viewModelScope.launch {
            try {
                modePersistence.saveMode(config.mode, config)
                if (_modeSwitchUiState.value.currentMode == config.mode) {
                    modeManager.switchMode(config.mode)
                }
            } catch (e: Exception) {
                _modeSwitchUiState.update {
                    it.copy(error = e.message ?: "更新配置失败")
                }
            }
        }
    }

    /**
     * 关闭错误提示
     */
    private fun dismissError() {
        _modeSwitchUiState.update { it.copy(error = null) }
    }

    // 原有的BotState相关方法
    fun updateConnectionStatus(isConnected: Boolean) {
        _botState.value = _botState.value.copy(
            isConnected = isConnected,
            error = if (!isConnected) "连接断开" else null
        )
        
        // 更新表情状态
        if (isConnected) {
            emotionManager.setEmotion(EmotionType.SATISFIED)
        } else {
            emotionManager.setEmotion(EmotionType.SUSPICIOUS)
        }
    }

    fun updateBatteryLevel(level: Int) {
        _botState.value = _botState.value.copy(batteryLevel = level)
        
        // 电量低于20%时更新表情状态
        if (level < 20) {
            emotionManager.updateSystemStatus(SystemStatus.LOW_BATTERY)
        }
    }

    fun updateWorkMode(mode: WorkMode) {
        _botState.value = _botState.value.copy(workMode = mode)
    }

    fun setError(error: String?) {
        _botState.value = _botState.value.copy(error = error)
        
        // 有错误时更新表情状态
        if (error != null) {
            emotionManager.updateSystemStatus(SystemStatus.ERROR)
        } else {
            emotionManager.updateSystemStatus(SystemStatus.NORMAL)
        }
    }
    
    /**
     * 语音唤醒时更新表情
     */
    fun updateVoiceWakeExpression() {
        viewModelScope.launch {
            emotionManager.updateUserInteraction(UserInteraction.GREETING)
            delay(2000) // 2秒后恢复中性表情
            emotionManager.setEmotion(EmotionType.NEUTRAL)
        }
    }
    
    /**
     * 获取当前表情矢量图
     */
    fun getCurrentEmotionVector(): ImageVector {
        return emotionManager.getCurrentEmotionVector()
    }
    
    /**
     * 切换表情策略
     */
    fun switchEmotionStrategy(strategyName: String) {
        emotionManager.switchStrategy(strategyName)
    }

    /**
     * 直接设置表情（用于UI调用）
     */
    fun setEmotion(emotionType: EmotionType) {
        emotionManager.setEmotion(emotionType)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            // 保存当前模式设置
            _modeSwitchUiState.value.currentMode.let { mode ->
                val config = modeManager.getModeConfig(mode)
                modePersistence.saveMode(mode, config)
            }
        }
    }
}
