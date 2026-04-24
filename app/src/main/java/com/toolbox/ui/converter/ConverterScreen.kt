package com.toolbox.ui.converter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.toolbox.R
import com.toolbox.ui.theme.BrandPrimary
import kotlinx.coroutines.launch

// 6大分类
enum class UnitCategory(val stringRes: Int) {
    LENGTH(R.string.converter_length),
    WEIGHT(R.string.converter_weight),
    AREA(R.string.converter_area),
    VOLUME(R.string.converter_volume),
    TEMPERATURE(R.string.converter_temperature),
    SPEED(R.string.converter_speed),
}

// 单位定义
data class UnitDef(
    val name: String,        // 显示名称（越南语）
    val symbol: String,      // 符号
    val toBaseFactor: Double // 转为基础单位的系数
)

// 各分类单位定义（含越南本地单位）
val unitData = mapOf(
    UnitCategory.LENGTH to listOf(
        UnitDef("mm", "mm", 0.001),
        UnitDef("cm", "cm", 0.01),
        UnitDef("m", "m", 1.0),
        UnitDef("km", "km", 1000.0),
        UnitDef("thước", "", 1.0),     // 越南尺 ≈ 1m
        UnitDef("tấc", "", 0.1),       // 越南寸 ≈ 10cm
        UnitDef("inch", "in", 0.0254),
        UnitDef("foot", "ft", 0.3048),
        UnitDef("yard", "yd", 0.9144),
        UnitDef("mile", "mi", 1609.34),
    ),
    UnitCategory.WEIGHT to listOf(
        UnitDef("g", "g", 0.001),
        UnitDef("kg", "kg", 1.0),
        UnitDef("tấn", "t", 1000.0),
        UnitDef("yến", "", 10.0),       // 1 yến = 10kg
        UnitDef("tạ", "", 100.0),       // 1 tạ = 100kg
        UnitDef("lạng", "", 0.1),       // 1 lạng = 100g
        UnitDef("pound", "lb", 0.453592),
        UnitDef("ounce", "oz", 0.0283495),
    ),
    UnitCategory.AREA to listOf(
        UnitDef("m²", "m²", 1.0),
        UnitDef("km²", "km²", 1_000_000.0),
        UnitDef("mẫu", "", 3600.0),     // 1 mẫu ≈ 3600 m²
        UnitDef("sào", "", 360.0),       // 1 sào ≈ 360 m²
        UnitDef("công", "", 1000.0),     // 1 công ≈ 1000 m²
        UnitDef("hectare", "ha", 10_000.0),
        UnitDef("acre", "ac", 4046.86),
    ),
    UnitCategory.VOLUME to listOf(
        UnitDef("ml", "ml", 0.001),
        UnitDef("L", "L", 1.0),
        UnitDef("m³", "m³", 1000.0),
        UnitDef("gallon", "gal", 3.78541),
    ),
    UnitCategory.TEMPERATURE to listOf(
        UnitDef("°C", "°C", 0.0),
        UnitDef("°F", "°F", 0.0),
        UnitDef("K", "K", 0.0),
    ),
    UnitCategory.SPEED to listOf(
        UnitDef("km/h", "km/h", 1.0),
        UnitDef("m/s", "m/s", 3.6),
        UnitDef("mph", "mph", 1.60934),
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen() {
    val categories = UnitCategory.values()
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部：分类横向标签
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 16.dp,
            containerColor = BrandPrimary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(stringResource(category.stringRes), color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)) }
                )
            }
        }

        // 页面内容
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ConverterPageContent(category = categories[page])
        }
    }
}

@Composable
fun ConverterPageContent(category: UnitCategory) {
    var inputValue by remember { mutableStateOf("1") }
    val units = unitData[category] ?: emptyList()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 输入区
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Nhập giá trị") },  // 输入值（越南语）
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 单位换算结果列表
        units.forEach { unit ->
            val result = convert(inputValue, unit, category)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(unit.name, style = MaterialTheme.typography.bodyLarge)
                        Text(unit.symbol, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = result,
                        style = MaterialTheme.typography.titleMedium,
                        color = BrandPrimary
                    )
                }
            }
        }
    }
}

// 换算逻辑（骨架，温度需特殊处理）
fun convert(input: String, targetUnit: UnitDef, category: UnitCategory): String {
    return try {
        val value = input.toDouble()
        if (category == UnitCategory.TEMPERATURE) {
            // 温度换算骨架（需特殊处理）
            value.toString()
        } else {
            val baseValue = value // 假设输入的是第一个单位的值
            val result = baseValue * targetUnit.toBaseFactor
            formatResult(result)
        }
    } catch (e: Exception) {
        "—"
    }
}

fun formatResult(value: Double): String {
    return "%.4f".format(value)
}
