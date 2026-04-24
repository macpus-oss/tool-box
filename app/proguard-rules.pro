# ToolBox ProGuard 规则

# 保留 Application 类
-keep class com.toolbox.ToolBoxApplication { *; }

# 保留 Compose 相关
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }

# 保留 Navigation 路由
-keep class com.toolbox.ui.navigation.Screen { *; }

# 保留 Serializable/Parcelable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private <fields>;
    !private <methods>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep class com.toolbox.data.** { *; }

# Kotlinx Serialization
-keep class kotlinx.serialization.json.** { *; }
-keep class com.toolbox.data.model.** { *; }

# Google Play Billing
-keep class com.android.billingclient.** { *; }

# 保留 Manifest 中注册的组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# 移除日志（Release 构建）
-assumenosideffects class * { *; }
-assertions class * { *; }
