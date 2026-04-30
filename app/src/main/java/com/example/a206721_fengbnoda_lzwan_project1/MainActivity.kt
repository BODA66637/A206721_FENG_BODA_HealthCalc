package com.example.a206721_fengbnoda_lzwan_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a206721_fengbnoda_lzwan_project1.ui.theme.A206721_FENGBNODA_lzwan_Project1Theme

// 1. 定义路由常量
object Screen {
    const val Home = "home"
    const val Input = "input"
    const val Result = "result"
    const val History = "history"
    const val SDGInfo = "sdg_info"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A206721_FENGBNODA_lzwan_Project1Theme {
                // 2. 初始化导航控制器
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // 3. 设置 NavHost 导航枢纽
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 页面 1：主页
                        composable(Screen.Home) {
                            HomeScreen(
                                onStartClick = { navController.navigate(Screen.Input) },
                                onHistoryClick = { navController.navigate(Screen.History) },
                                onSDGClick = { navController.navigate(Screen.SDGInfo) }
                            )
                        }

                        // 页面 2：输入页 (真实页面)
                        composable(Screen.Input) {
                            InputScreen(
                                onCalculateClick = { navController.navigate(Screen.Result) },
                                onBackClick = { navController.popBackStack() } // 返回上一页
                            )
                        }

                        // 页面 3：结果页 (待开发)
                        composable(Screen.Result) {
                            PlaceholderScreen("Result Screen")
                        }

                        // 页面 4：历史记录页 (待开发)
                        composable(Screen.History) {
                            PlaceholderScreen("History Screen")
                        }

                        // 页面 5：SDG 说明页 (待开发)
                        composable(Screen.SDGInfo) {
                            PlaceholderScreen("SDG 3 Info Screen")
                        }
                    }
                }
            }
        }
    }
}

// ==================== 下面是各个屏幕的 UI 代码 ====================

@Composable
fun HomeScreen(
    onStartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSDGClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "HealthCalc", fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Text(text = "Track Your Health Easily", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "What is BMI?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "BMI helps you understand whether your weight is healthy based on your height.")
                TextButton(onClick = onSDGClick) {
                    Text("Learn about SDG 3 Goals")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth().height(55.dp)) {
            Text("Start Calculation", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth().height(55.dp)) {
            Text("View History", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Stay healthy with simple tracking 💚", fontSize = 14.sp)
    }
}

@Composable
fun InputScreen(
    onCalculateClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 暂时用 remember 保存输入数据
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Enter Your Details", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(30.dp))

        // 体重输入框
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 身高输入框
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 计算按钮
        Button(
            onClick = onCalculateClick,
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text("Calculate BMI", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 返回按钮
        TextButton(onClick = onBackClick) {
            Text("Back to Home")
        }
    }
}

// 占位页面组件
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Light)
    }
}