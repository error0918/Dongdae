package com.taeyeon.dongdae

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.core.SharedPreferencesManager
import com.taeyeon.core.Utils

var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.FullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ScreenAlwaysOn)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DarkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DynamicColor)

var id by mutableStateOf(getAndroidId())
var name by mutableStateOf(getName(id))
var subName by mutableStateOf(getSubName(id))
var uniqueColor by mutableStateOf(getUniqueColor(id))


fun load() {
    loadSettings()
    // TODO
}

fun save() {
    saveSettings()
    // TODO
}

fun loadSettings() {
    Settings.loadSettings()

    fullScreenMode = Settings.settingsData.FullScreenMode
    screenAlwaysOn = Settings.settingsData.ScreenAlwaysOn
    darkMode = Settings.settingsData.DarkMode
    dynamicColor = Settings.settingsData.DynamicColor
}

fun saveSettings() {
    Settings.settingsData.FullScreenMode = fullScreenMode
    Settings.settingsData.ScreenAlwaysOn = screenAlwaysOn
    Settings.settingsData.DarkMode = darkMode
    Settings.settingsData.DynamicColor = dynamicColor

    Settings.saveSettings()
}


enum class Screen {
    Main, Welcome, InternetDisconnected
}

fun checkScreen(): Screen {
    return if (checkFirstOpen()) Screen.Welcome
        else if (!Utils.checkInternet()) Screen.InternetDisconnected
        else Screen.Main
}


fun checkFirstOpen(): Boolean {
    val key = "OPENED"
    if (SharedPreferencesManager.Companion.Public.contains(key)) return false
    SharedPreferencesManager.Companion.Public.putBoolean(key, true)
    return true
}


data class Partition(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val lazyListState: LazyListState? = null,
    val fab: (@Composable () -> Unit)? = null,
    val composable: @Composable () -> Unit
)


@Composable
fun getCornerSize(shape: CornerBasedShape): Dp {
    var cornerRadius: Dp = 0.dp
    shape.let {
        val size = Size.Unspecified
        with(LocalDensity.current) {
            val corners = listOf(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
            corners.forEach { corner ->
                cornerRadius += corner.toPx(size, this).toDp() / corners.size
            }
        }
    }
    return cornerRadius
}

@Suppress("DEPRECATION")
@Composable
fun SetStatusBarColor(
    color: Color = MaterialTheme.colorScheme.surface,
    isAppearanceLightStatusBars: Boolean = !when (darkMode) {
        Settings.DarkMode.SYSTEM_MODE -> isSystemInDarkTheme()
        Settings.DarkMode.LIGHT_MODE -> false
        Settings.DarkMode.DARK_MODE -> true
        else -> isSystemInDarkTheme()
    }
) {
    val view = LocalView.current
    (view.context as Activity).window.statusBarColor = color.toArgb()
    ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = isAppearanceLightStatusBars
}

@Composable
fun SetNavigationBarColor(
    color: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) // BottomBar Color
) {
    val view = LocalView.current
    (view.context as Activity).window.navigationBarColor = color.toArgb()
}

@SuppressLint("HardwareIds")
fun getAndroidId(): String = android.provider.Settings.Secure.getString(Core.getContext().contentResolver, android.provider.Settings.Secure.ANDROID_ID)

fun getName(androidId: String = id): String {
    val endangeredSpecies = Core.getContext().resources.getStringArray(R.array.endangered_species)
    return endangeredSpecies[Integer.parseInt(androidId.substring(0..5), 16) % endangeredSpecies.size]
}

fun getSubName(androidId: String = id): String {
    return androidId.substring(androidId.length - 7, androidId.length - 1)
}

fun getUniqueColor(androidId: String = id): Color {
    return Color(android.graphics.Color.parseColor("#${getSubName(id)}"))
}