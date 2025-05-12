package com.xuatseg.yuanyin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YuanYinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmotionsDemoApp()
                }
            }
        }
    }
}

@Composable
fun YuanYinTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

@Composable
fun EmotionsDemoApp() {
    // 创建标签页状态
    var currentTab by remember { mutableStateOf(DemoTab.SHOWCASE) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部标签切换
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(100.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton(
                title = "表情展示",
                isSelected = currentTab == DemoTab.SHOWCASE,
                onClick = { currentTab = DemoTab.SHOWCASE }
            )
            
            TabButton(
                title = "业务场景",
                isSelected = currentTab == DemoTab.SCENARIOS,
                onClick = { currentTab = DemoTab.SCENARIOS }
            )
        }
        
        // 标签页内容
        when (currentTab) {
            DemoTab.SHOWCASE -> RobotEmotionsScreen()
            DemoTab.SCENARIOS -> ScenariosDemoScreen()
        }
    }
}

@Composable
fun TabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.LightGray
        )
    }
}

@Composable
fun ScenariosDemoScreen() {
    // 使用单例获取情感管理器
    val emotionManager = remember { RobotEmotionManager.getInstance() }
    // 观察当前表情状态
    val currentEmotion = emotionManager.currentEmotion.value
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 显示当前机器人表情
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
                .background(Color(0x22AAAAAA))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = emotionManager.getCurrentEmotionVector(),
                    contentDescription = "Current robot emotion",
                    modifier = Modifier.size(80.dp)
                )
                
                Text(
                    text = getEmotionDisplayName(currentEmotion),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        
        // 业务场景类别
        Text(
            text = "业务场景演示",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 场景卡片区域
        Column(
            modifier = Modifier.weight(0.6f)
        ) {
            // 用户交互场景
            ScenarioCard(
                title = "用户交互",
                options = listOf(
                    "打招呼" to { emotionManager.handleUserInteraction(UserInteractionType.GREETING) },
                    "提问" to { emotionManager.handleUserInteraction(UserInteractionType.QUESTION) },
                    "复杂查询" to { emotionManager.handleUserInteraction(UserInteractionType.COMPLEX_QUERY) },
                    "表达感谢" to { emotionManager.handleUserInteraction(UserInteractionType.APPRECIATION) }
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 系统状态场景
            ScenarioCard(
                title = "系统状态",
                options = listOf(
                    "正常" to { emotionManager.handleSystemStatus(SystemStatusType.NORMAL) },
                    "错误" to { emotionManager.handleSystemStatus(SystemStatusType.ERROR) },
                    "维护中" to { emotionManager.handleSystemStatus(SystemStatusType.MAINTENANCE) },
                    "低电量" to { emotionManager.handleSystemStatus(SystemStatusType.LOW_BATTERY) },
                    "充电中" to { emotionManager.handleSystemStatus(SystemStatusType.CHARGING) }
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 任务完成场景
            ScenarioCard(
                title = "任务完成",
                options = listOf(
                    "简单任务成功" to { emotionManager.handleTaskCompletion(true, TaskComplexity.LOW) },
                    "复杂任务成功" to { emotionManager.handleTaskCompletion(true, TaskComplexity.HIGH) },
                    "简单任务失败" to { emotionManager.handleTaskCompletion(false, TaskComplexity.LOW) },
                    "复杂任务失败" to { emotionManager.handleTaskCompletion(false, TaskComplexity.HIGH) }
                )
            )
        }
    }
}

@Composable
fun ScenarioCard(
    title: String,
    options: List<Pair<String, () -> Unit>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { (label, action) ->
                    Button(
                        onClick = action,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RobotEmotionsScreen() {
    var selectedEmotion by remember { mutableStateOf(EmotionType.NEUTRAL) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the currently selected emotion
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0x22AAAAAA))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = RobotEmotions.getEmotion(selectedEmotion),
                    contentDescription = "Robot ${selectedEmotion.name.lowercase()} face",
                    modifier = Modifier.size(80.dp)
                )
                
                Text(
                    text = getEmotionDisplayName(selectedEmotion),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        
        // Grid of all available emotions
        Text(
            text = "选择表情",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
        )
        
        // First row of emotions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmotionCard(
                emotionType = EmotionType.SUSPICIOUS,
                isSelected = selectedEmotion == EmotionType.SUSPICIOUS,
                onEmotionSelected = { selectedEmotion = it }
            )
            
            EmotionCard(
                emotionType = EmotionType.HAPPY,
                isSelected = selectedEmotion == EmotionType.HAPPY,
                onEmotionSelected = { selectedEmotion = it }
            )
            
            EmotionCard(
                emotionType = EmotionType.SATISFIED,
                isSelected = selectedEmotion == EmotionType.SATISFIED,
                onEmotionSelected = { selectedEmotion = it }
            )
        }
        
        // Second row of emotions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmotionCard(
                emotionType = EmotionType.NEUTRAL,
                isSelected = selectedEmotion == EmotionType.NEUTRAL,
                onEmotionSelected = { selectedEmotion = it }
            )
            
            EmotionCard(
                emotionType = EmotionType.PANIC,
                isSelected = selectedEmotion == EmotionType.PANIC,
                onEmotionSelected = { selectedEmotion = it }
            )
            
            EmotionCard(
                emotionType = EmotionType.REPAIR,
                isSelected = selectedEmotion == EmotionType.REPAIR,
                onEmotionSelected = { selectedEmotion = it }
            )
        }
    }
}

@Composable
fun EmotionCard(
    emotionType: EmotionType,
    isSelected: Boolean,
    onEmotionSelected: (EmotionType) -> Unit
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { onEmotionSelected(emotionType) }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = RobotEmotions.getEmotion(emotionType),
                contentDescription = "Robot ${emotionType.name.lowercase()} face",
                modifier = Modifier.size(60.dp)
            )
            
            Text(
                text = getEmotionShortName(emotionType),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun getEmotionDisplayName(emotionType: EmotionType): String {
    return when (emotionType) {
        EmotionType.SUSPICIOUS -> "怀疑 / 不满 / 生气"
        EmotionType.HAPPY -> "开心 / 友善"
        EmotionType.SATISFIED -> "满意 / 平和"
        EmotionType.NEUTRAL -> "中性 / 待机"
        EmotionType.PANIC -> "跌落 / 紧急 / 惊慌"
        EmotionType.REPAIR -> "清洁 / 维修状态"
    }
}

fun getEmotionShortName(emotionType: EmotionType): String {
    return when (emotionType) {
        EmotionType.SUSPICIOUS -> "怀疑"
        EmotionType.HAPPY -> "开心"
        EmotionType.SATISFIED -> "满意"
        EmotionType.NEUTRAL -> "中性"
        EmotionType.PANIC -> "紧急"
        EmotionType.REPAIR -> "维修"
    }
}

// 标签页枚举
enum class DemoTab {
    SHOWCASE,  // 表情展示
    SCENARIOS  // 业务场景
}

@Preview(showBackground = true)
@Composable
fun RobotEmotionsScreenPreview() {
    YuanYinTheme {
        RobotEmotionsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ScenariosDemoScreenPreview() {
    YuanYinTheme {
        ScenariosDemoScreen()
    }
}
