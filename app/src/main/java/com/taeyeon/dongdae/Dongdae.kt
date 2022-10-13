package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.taeyeon.core.Core
import com.taeyeon.core.Settings

var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.FullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ScreenAlwaysOn)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DarkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DynamicColor)

var id by mutableStateOf(getAndroidId())
var name by mutableStateOf(getName(id))
var subName by mutableStateOf(getSubName(id))

fun load() {
    Settings.loadSettings()

    fullScreenMode = Settings.settingsData.FullScreenMode
    screenAlwaysOn = Settings.settingsData.ScreenAlwaysOn
    darkMode = Settings.settingsData.DarkMode
    dynamicColor = Settings.settingsData.DynamicColor
}

fun save() {
    Settings.settingsData.FullScreenMode = fullScreenMode
    Settings.settingsData.ScreenAlwaysOn = screenAlwaysOn
    Settings.settingsData.DarkMode = darkMode
    Settings.settingsData.DynamicColor = dynamicColor

    Settings.saveSettings()
}

data class Partition(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val lazyListState: LazyListState? = null,
    val fab: (@Composable () -> Unit)? = null,
    val composable: @Composable () -> Unit
)

@SuppressLint("HardwareIds")
fun getAndroidId(): String = android.provider.Settings.Secure.getString(Core.getContext().contentResolver, android.provider.Settings.Secure.ANDROID_ID)

fun getName(androidId: String = getAndroidId()): String {
    val endangeredSpecies = Core.getContext().resources.getStringArray(R.array.endangered_species)
    return endangeredSpecies[Integer.parseInt(androidId.substring(1..5), 16) % endangeredSpecies.size]
}

fun getSubName(androidId: String = getAndroidId()): String {
    return androidId.substring(androidId.length - 6, androidId.length - 1)
}
