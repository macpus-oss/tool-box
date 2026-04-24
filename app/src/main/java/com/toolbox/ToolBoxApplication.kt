package com.toolbox

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore

// DataStore 实例（全局单例）
val Application.dataStore by preferencesDataStore(name = "toolbox_prefs")

class ToolBoxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化工作：
        // 1. 汇率缓存预热
        // 2. 主题设置读取
        // 3. 订阅状态检查（Google Play Billing）
    }
}
