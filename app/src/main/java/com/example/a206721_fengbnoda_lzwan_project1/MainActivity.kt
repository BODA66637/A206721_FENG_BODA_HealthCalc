package com.example.a206721_fengbnoda_lzwan_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a206721_fengbnoda_lzwan_project1.ui.theme.A206721_FENGBNODA_lzwan_Project1Theme

// 1. 路由常量：定义 App 的 5 个页面
object Screen {
    const val Home = "home"
    const val Input = "input"
    const val Result = "result"
    const val History = "history"
    const val SDGInfo = "sdg_info"
}

// 2. 数据模型：保存单次计算记录
data class HealthRecord(
    val weight: String,
    val height: String,
    val bmi: Double
)

// 3. ViewModel：管理全局状态，严禁使用 Room/Firebase
class HealthViewModel : ViewModel() {
    var currentWeight by mutableStateOf("")
    var currentHeight by mutableStateOf("")
    var bmiResult by mutableStateOf(0.0)

    // 历史记录列表：实现“添加并跨页面显示”的核心[cite: 1]
    val historyList = mutableStateListOf<HealthRecord>()

    fun calculateBMI() {
        val w = currentWeight.toDoubleOrNull() ?: 0.0
        val h = (currentHeight.toDoubleOrNull() ?: 0.0) / 100.0
        if (w > 0 && h > 0) {
            bmiResult = w / (h * h)
            // 添加到共享列表[cite: 1]
            historyList.add(HealthRecord(currentWeight, currentHeight, bmiResult))
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A206721_FENGBNODA_lzwan_Project1Theme {
                val navController = rememberNavController()
                val myViewModel: HealthViewModel = viewModel() // 共享 ViewModel[cite: 1]

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 页面 1：首页[cite: 1]
                        composable(Screen.Home) {
                            HomeScreen(
                                onStartClick = { navController.navigate(Screen.Input) },
                                onHistoryClick = { navController.navigate(Screen.History) },
                                onSDGClick = { navController.navigate(Screen.SDGInfo) }
                            )
                        }

                        // 页面 2：输入页[cite: 1]
                        composable(Screen.Input) {
                            InputScreen(
                                weight = myViewModel.currentWeight,
                                height = myViewModel.currentHeight,
                                onWeightChange = { myViewModel.currentWeight = it },
                                onHeightChange = { myViewModel.currentHeight = it },
                                onCalculateClick = {
                                    myViewModel.calculateBMI()
                                    navController.navigate(Screen.Result)
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // 页面 3：结果页[cite: 1]
                        composable(Screen.Result) {
                            ResultScreen(
                                bmi = myViewModel.bmiResult,
                                onBackClick = { navController.navigate(Screen.Home) }
                            )
                        }

                        // 页面 4：历史页[cite: 1]
                        composable(Screen.History) {
                            HistoryScreen(
                                records = myViewModel.historyList,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // 页面 5：SDG 说明页[cite: 1]
                        composable(Screen.SDGInfo) {
                            SDGInfoScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

// ==================== 5 个屏幕的 UI 实现 ====================

@Composable
fun HomeScreen(onStartClick: () -> Unit, onHistoryClick: () -> Unit, onSDGClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "HealthCalc", fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Text(text = "Track Your Health Easily", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(30.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(6.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "What is BMI?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "BMI helps you understand whether your weight is healthy based on your height.")
                TextButton(onClick = onSDGClick) { Text("Learn about SDG 3 Goals") }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth().height(55.dp)) { Text("Start Calculation", fontSize = 16.sp) }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth().height(55.dp)) { Text("View History", fontSize = 16.sp) }
    }
}

@Composable
fun InputScreen(weight: String, height: String, onWeightChange: (String) -> Unit, onHeightChange: (String) -> Unit, onCalculateClick: () -> Unit, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Enter Details", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = weight, onValueChange = onWeightChange, label = { Text("Weight (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = height, onValueChange = onHeightChange, label = { Text("Height (cm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onCalculateClick, modifier = Modifier.fillMaxWidth().height(55.dp)) { Text("Calculate BMI", fontSize = 16.sp) }
        TextButton(onClick = onBackClick) { Text("Back to Home") }
    }
}

@Composable
fun ResultScreen(bmi: Double, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Your BMI Result", fontSize = 24.sp)
        Text(text = String.format("%.2f", bmi), fontSize = 64.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onBackClick) { Text("Finish") }
    }
}

@Composable
fun HistoryScreen(records: List<HealthRecord>, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(text = "History", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(records) { record ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "W: ${record.weight}kg / H: ${record.height}cm", fontSize = 14.sp)
                        }
                        Text(text = "BMI: ${String.format("%.2f", record.bmi)}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Back to Home") }
    }
}

@Composable
fun SDGInfoScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "SDG 3 Goals", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(text = "This app supports SDG 3: Good Health and Well-being. By tracking BMI, we help Malaysians monitor their health.", modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) { Text("I Understand") }
    }
}