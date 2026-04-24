package com.toolbox.ui.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.toolbox.R
import com.toolbox.ui.theme.BrandPrimary
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

// 支持的币种
data class CurrencyInfo(
    val code: String,        // USD, VND, CNY...
    val nameVi: String,     // 越南语名称
    val flagEmoji: String,   // 国旗emoji
    val rateToVnd: Double    // 对VND汇率（基准）
)

// 预设币种列表
val defaultCurrencies = listOf(
    CurrencyInfo("VND", "Việt Nam Đồng", "🇻🇳", 1.0),
    CurrencyInfo("USD", "Đô la Mỹ", "🇺🇸", 25300.0),
    CurrencyInfo("CNY", "Nhân dân tệ", "🇨🇳", 3500.0),
    CurrencyInfo("EUR", "Euro", "🇪🇺", 27500.0),
    CurrencyInfo("THB", "Baht Thái", "🇹🇭", 700.0),
    CurrencyInfo("KRW", "Won Hàn Quốc", "🇰🇷", 19.0),
    CurrencyInfo("SGD", "Đô la Singapore", "🇸🇬", 18800.0),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen() {
    var baseAmount by remember { mutableStateOf("1") }
    val currencies = remember { mutableStateListOf(*defaultCurrencies.toTypedArray()) }
    var lastUpdated by remember { mutableStateOf("—") }
    var isOffline by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部：标题 + 刷新按钮 + 最后更新时间
        TopAppBar(
            title = { Text(stringResource(R.string.currency_title)) },
            actions = {
                IconButton(onClick = { /* TODO: 刷新汇率 */ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = "Làm mới"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandPrimary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
        )

        // 基准货币输入区（VND固定置顶）
        Card(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🇻🇳", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("VND", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = baseAmount,
                    onValueChange = { baseAmount = it },
                    label = { Text("Nhập số tiền VND") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                if (isOffline) {
                    Text(
                        text = stringResource(R.string.offline_mode),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    text = "${stringResource(R.string.last_updated)}: $lastUpdated",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 币种列表（同步换算）
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(currencies.filter { it.code != "VND" }) { currency ->
                CurrencyRow(currency = currency, baseAmount = baseAmount)
            }
        }
    }
}

@Composable
fun CurrencyRow(currency: CurrencyInfo, baseAmount: String) {
    val vndAmount = baseAmount.toDoubleOrNull() ?: 0.0
    val converted = vndAmount / currency.rateToVnd
    val formatted = formatVietnameseLargeNumber(converted)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currency.flagEmoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(currency.code, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Text(currency.nameVi, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatted,
                    style = MaterialTheme.typography.titleMedium,
                    color = BrandPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "1 ${currency.code} = ${formatRate(currency.rateToVnd)} ₫",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// 越南大额数字简化显示：ngàn（千）/ triệu（百万）/ tỷ（十亿）
fun formatVietnameseLargeNumber(value: Double): String {
    return when {
        value >= 1_000_000_000 -> "%.2f tỷ".format(value / 1_000_000_000)
        value >= 1_000_000 -> "%.2f triệu".format(value / 1_000_000)
        value >= 1_000 -> "%.2f ngàn".format(value / 1_000)
        else -> "%.2f".format(value)
    }
}

fun formatRate(rate: Double): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val format = DecimalFormat("#,##0.#####", symbols)
    return format.format(rate)
}
