package com.toolbox.ui.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toolbox.ui.theme.AccentOrange
import com.toolbox.ui.theme.BrandPrimary
import com.toolbox.ui.theme.DangerRed
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var history by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 显示区域（35%）
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            // 表达式
            Text(
                text = expression.ifEmpty { " " },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 结果（≥32sp）
            Text(
                text = formatVietnameseNumber(result),
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1
            )
        }

        // 按键区域（65%）
        Column(
            modifier = Modifier.fillMaxWidth().weight(1.3f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 第一行：C  CE  %  ÷
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("C", DangerRed, Modifier.weight(1f)) { expression = ""; result = "0" }
                CalculatorButton("CE", DangerRed, Modifier.weight(1f)) { expression = expression.dropLast(1) }
                CalculatorButton("%", BrandPrimary, Modifier.weight(1f)) { expression += "%" }
                CalculatorButton("÷", BrandPrimary, Modifier.weight(1f)) { expression += "÷" }
            }
            // 第二行：7 8 9 ×
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("7", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "7" }
                CalculatorButton("8", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "8" }
                CalculatorButton("9", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "9" }
                CalculatorButton("×", BrandPrimary, Modifier.weight(1f)) { expression += "×" }
            }
            // 第三行：4 5 6 -
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("4", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "4" }
                CalculatorButton("5", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "5" }
                CalculatorButton("6", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "6" }
                CalculatorButton("-", BrandPrimary, Modifier.weight(1f)) { expression += "-" }
            }
            // 第四行：1 2 3 +
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("1", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "1" }
                CalculatorButton("2", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "2" }
                CalculatorButton("3", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "3" }
                CalculatorButton("+", BrandPrimary, Modifier.weight(1f)) { expression += "+" }
            }
            // 第五行：0 . =（= 占两行）
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton("0", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "0" }
                CalculatorButton(".", MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f)) { expression += "." }
                // = 按钮
                Button(
                    onClick = { result = calculate(expression) },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                ) {
                    Text("=", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun RowScope.CalculatorButton(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.White)
    }
}

// 越南数字格式：千分位用句点，小数位用逗号
fun formatVietnameseNumber(number: String): String {
    return try {
        val num = number.replace(",", ".").toDouble()
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val format = DecimalFormat("#,##0.#####", symbols)
        format.format(num)
    } catch (e: Exception) {
        number
    }
}

// 简易计算（支持 + - × ÷）
fun calculate(expr: String): String {
    return try {
        val normalized = expr.replace("÷", "/").replace("×", "*")
        val result = evaluateSimple(normalized)
        result.toString()
    } catch (e: Exception) {
        "Lỗi"  // 错误
    }
}

// 极简表达式求值（仅支持 四则运算）
fun evaluateSimple(expr: String): Double {
    // 实际项目中应引入更完整的表达式解析库
    // 此处为骨架代码，仅作演示
    return 0.0
}
