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


var defaultTab by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.defaultTab)
var defaultPassword by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.defaultPassword)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.darkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.dynamicColor)
var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.fullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.screenAlwaysOn)
var tester by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.tester)

var id by mutableStateOf("")
var name by mutableStateOf("")
var subName by mutableStateOf("")
var uniqueColor by mutableStateOf(Color.Transparent)


fun load() {
    loadSettings()

    id = getAndroidId()
    name = getName(id)
    subName = getSubName(id)
    uniqueColor = getUniqueColor(id)
}

fun save() {
    saveSettings()
}

fun loadSettings() {
    Settings.loadSettings()

    defaultTab = Settings.settingsData.defaultTab
    defaultPassword = Settings.settingsData.defaultPassword
    darkMode = Settings.settingsData.darkMode
    dynamicColor = Settings.settingsData.dynamicColor
    fullScreenMode = Settings.settingsData.fullScreenMode
    screenAlwaysOn = Settings.settingsData.screenAlwaysOn
    tester = Settings.settingsData.tester
}

fun saveSettings() {
    Settings.settingsData.defaultTab = defaultTab
    Settings.settingsData.defaultPassword = defaultPassword
    Settings.settingsData.darkMode = darkMode
    Settings.settingsData.dynamicColor = dynamicColor
    Settings.settingsData.fullScreenMode = fullScreenMode
    Settings.settingsData.screenAlwaysOn = screenAlwaysOn
    Settings.settingsData.tester = tester

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

fun getDigitNumber(number: Int, digits: Int): String {
    return if (digits > 0) {
        if (number.toString().length >= digits) {
            number.toString().substring(0, digits)
        } else {
            "0".repeat(digits - number.toString().length) + number.toString()
        }
    } else ""
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
    return Color(android.graphics.Color.parseColor("#${getSubName(androidId)}"))
}