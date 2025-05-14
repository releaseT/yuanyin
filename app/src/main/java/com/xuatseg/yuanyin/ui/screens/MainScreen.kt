package com.xuatseg.yuanyin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import com.xuatseg.yuanyin.mode.ProcessingMode
import com.xuatseg.yuanyin.ui.control.IRobotControlViewModel
import com.xuatseg.yuanyin.ui.mode.IModeSwitchViewModel
import com.xuatseg.yuanyin.ui.mode.ModeSwitchButton
import com.xuatseg.yuanyin.ui.mode.ModeSwitchEvent
import com.xuatseg.yuanyin.ui.mode.ModeSwitchUiState
import com.xuatseg.yuanyin.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.xuatseg.yuanyin.ui.VideoPlayer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import com.airastack.emotionkit.EmotionType
import com.airastack.emotionkit.strategy.EmotionStrategyManager
import com.airastack.emotionkit.RobotEmotions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modeSwitchViewModel: IModeSwitchViewModel,
    robotControlViewModel: IRobotControlViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val modeSwitchUiState by modeSwitchViewModel.getUiState().collectAsState(initial = ModeSwitchUiState(
        currentMode = ProcessingMode.LOCAL,
        availableModes = listOf()
    )
    )

    // 新增遮罩弹窗状态
    var showSwitchMask by remember { mutableStateOf(false) }
    var showSettingsMask by remember { mutableStateOf(false) }
    var showEmotionMask by remember { mutableStateOf(false) }
    
    // 获取机器人表情
    val robotExpression by viewModel.robotExpression.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027), // 深蓝
                        Color(0xFF2C5364), // 蓝紫
                        Color(0xFF1A2980)  // 科幻蓝
                    )
                )
            )
    ) {
        // 视频背景部分已被注释掉
        
        // 2. 半透明遮罩增强科幻感
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA0F2027))
        )
        // 2.5 机器人表情显示区域 (新增)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(240.dp)
                .background(Color(0x33000000), shape = MaterialTheme.shapes.extraLarge)
                .border(
                    width = 2.dp,
                    color = Color(0xFF00CCFF),  // 青蓝色边框，与emotion-kit的颜色保持一致
                    shape = MaterialTheme.shapes.extraLarge
                )
                .clickable { showEmotionMask = true },  // 点击显示表情选择弹窗
            contentAlignment = Alignment.Center
        ) {
            // 显示机器人表情
            robotExpression?.let { 
                Image(
                    imageVector = it,
                    contentDescription = "Robot emotion",
                    modifier = Modifier.size(180.dp)
                )
            } ?: run {
                // 如果表情为空，显示默认文本表情
                Text(
                    text = "^_^",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00CCFF)
                )
            }
        }
        // 3. 右侧竖向排列按钮
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 24.dp, top = 48.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            // 切换按钮（开关图标）
            IconButton(onClick = { showSwitchMask = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "切换模式",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // 设置按钮
            IconButton(onClick = { showSettingsMask = true }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "设置",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        // 4. 切换遮罩弹窗
        if (showSwitchMask) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000)) // 遮罩外部区域
                    .clickable { showSwitchMask = false }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 0.dp)
                        .width(220.dp)
                        .fillMaxHeight()
                        .background(Color(0xEE222B3A), shape = MaterialTheme.shapes.medium)
                        .clickable(enabled = false) { }, // 禁止遮罩内容冒泡
                ) {
                    // 竖向排列所有模式
                    val modes = ProcessingMode.values()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text("模式切换", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        modes.forEach { mode ->
                            val isCurrent = mode == modeSwitchUiState.currentMode
                            Button(
                                onClick = {
                                    modeSwitchViewModel.handleEvent(ModeSwitchEvent.SwitchMode(mode))
                                    showSwitchMask = false
                                },
                                enabled = !isCurrent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(text = mode.name, color = if (isCurrent) Color.Gray else Color.White)
                            }
                        }
                    }
                }
            }
        }
        // 5. 设置遮罩弹窗
        if (showSettingsMask) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000)) // 遮罩外部区域
                    .clickable { showSettingsMask = false }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 0.dp)
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(Color(0xEE222B3A), shape = MaterialTheme.shapes.medium)
                        .clickable(enabled = false) { }, // 禁止遮罩内容冒泡
                ) {
                    // 左右布局：左avatar，右个性信息
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 随机avatar（可用默认头像）
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color.LightGray, shape = MaterialTheme.shapes.large),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", fontSize = 36.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        // 个性信息
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("昵称：未来机器人", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("签名：探索未来，连接智能世界。", color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
        
        // 6. 表情选择弹窗 (新增)
        if (showEmotionMask) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000)) // 遮罩外部区域
                    .clickable { showEmotionMask = false }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(320.dp)
                        .background(Color(0xEE222B3A), shape = MaterialTheme.shapes.medium)
                        .clickable(enabled = false) { }, // 禁止遮罩内容冒泡
                        
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("表情选择", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // 表情选择选项
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0x33000000), shape = MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0xFF00CCFF), shape = MaterialTheme.shapes.medium)
                                    .clickable { 
                                        viewModel.setEmotion(EmotionType.HAPPY)
                                        showEmotionMask = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = RobotEmotions.getEmotion(EmotionType.HAPPY),
                                    contentDescription = "Happy",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0x33000000), shape = MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0xFF00CCFF), shape = MaterialTheme.shapes.medium)
                                    .clickable { 
                                        viewModel.setEmotion(EmotionType.NEUTRAL)
                                        showEmotionMask = false 
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = RobotEmotions.getEmotion(EmotionType.NEUTRAL),
                                    contentDescription = "Neutral",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0x33000000), shape = MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0xFF00CCFF), shape = MaterialTheme.shapes.medium)
                                    .clickable { 
                                        viewModel.setEmotion(EmotionType.SATISFIED)
                                        showEmotionMask = false 
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = RobotEmotions.getEmotion(EmotionType.SATISFIED),
                                    contentDescription = "Satisfied",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0x33000000), shape = MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0xFF00CCFF), shape = MaterialTheme.shapes.medium)
                                    .clickable { 
                                        viewModel.setEmotion(EmotionType.SUSPICIOUS)
                                        showEmotionMask = false 
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = RobotEmotions.getEmotion(EmotionType.SUSPICIOUS),
                                    contentDescription = "Suspicious",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0x33000000), shape = MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0xFF00CCFF), shape = MaterialTheme.shapes.medium)
                                    .clickable { 
                                        viewModel.setEmotion(EmotionType.PANIC)
                                        showEmotionMask = false 
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = RobotEmotions.getEmotion(EmotionType.PANIC),
                                    contentDescription = "Panic",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0x33000000), shape = MaterialTheme.shapes.medium)
                                    .border(1.dp, Color(0xFF00CCFF), shape = MaterialTheme.shapes.medium)
                                    .clickable { 
                                        viewModel.setEmotion(EmotionType.REPAIR)
                                        showEmotionMask = false 
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = RobotEmotions.getEmotion(EmotionType.REPAIR),
                                    contentDescription = "Repair",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // 表情策略选择
                        Text("表情策略", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { 
                                    viewModel.switchEmotionStrategy("default")
                                    showEmotionMask = false
                                }
                            ) {
                                Text("默认策略")
                            }
                            
                            Button(
                                onClick = { 
                                    viewModel.switchEmotionStrategy("conservative")
                                    showEmotionMask = false
                                }
                            ) {
                                Text("保守策略")
                            }
                            
                            Button(
                                onClick = { 
                                    viewModel.switchEmotionStrategy("expressive")
                                    showEmotionMask = false
                                }
                            ) {
                                Text("表现策略")
                            }
                        }
                    }
                }
            }
        }
    }
}
