package com.taeyeon.dongdae

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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


object FDManager {
    private const val ChatId = "chat"
    private const val PostId = "post"

    val chatDatabase = Firebase.database.getReference(ChatId)
    val postDatabase = Firebase.database.getReference(PostId)

    fun initializeChat(
        onInitialized: () -> Unit,
        onChildAdded: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        onChildChanged: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        onChildRemoved: (snapshot: DataSnapshot) -> Unit
    ) {
        onInitialized()
        chatDatabase.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                onChildAdded(snapshot, previousChildName)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                onChildChanged(snapshot, previousChildName)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                onChildRemoved(snapshot)
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun initializePost(
        onInitialized: () -> Unit,
        onChildAdded: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        onChildChanged: (snapshot: DataSnapshot, previousChildName: String?) -> Unit,
        onChildRemoved: (snapshot: DataSnapshot) -> Unit
    ) {
        onInitialized()
        postDatabase.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                onChildAdded(snapshot, previousChildName)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                onChildChanged(snapshot, previousChildName)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                onChildRemoved(snapshot)
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
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
fun getCornerSize(
    shape: CornerBasedShape,
    size: Size = Size.Unspecified
): Dp {
    var cornerRadius: Dp = 0.dp
    shape.let {
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
    color: Color,
    darkIcons: Boolean = !when (darkMode) {
        Settings.DarkMode.SYSTEM_MODE -> isSystemInDarkTheme()
        Settings.DarkMode.LIGHT_MODE -> false
        Settings.DarkMode.DARK_MODE -> true
    }
) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(color, darkIcons) {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = darkIcons
        )
    }
}

@Composable
fun SetNavigationBarColor(
    color: Color,
    darkIcons: Boolean = !when (darkMode) {
        Settings.DarkMode.SYSTEM_MODE -> isSystemInDarkTheme()
        Settings.DarkMode.LIGHT_MODE -> false
        Settings.DarkMode.DARK_MODE -> true
    }
) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(color, darkIcons) {
        systemUiController.setNavigationBarColor(
            color = color,
            darkIcons = darkIcons
        )
    }
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
    if (androidId == "0".repeat(17)) return "관리자" // TODO
    val endangeredSpecies = Core.getContext().resources.getStringArray(R.array.endangered_species)
    return endangeredSpecies[Integer.parseInt(androidId.substring(0..5), 16) % endangeredSpecies.size - 1]
}

fun getSubName(androidId: String = id): String {
    return androidId.substring(androidId.length - 7, androidId.length - 1)
}

fun getUniqueColor(androidId: String = id): Color {
    return Color(android.graphics.Color.parseColor("#${getSubName(androidId)}"))
}