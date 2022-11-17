package com.taeyeon.core

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

/*
 * NOTICE
 *
 * Add "implementation "com.google.code.gson:gson:2.9.1"" at build.gradle (app)
 * Require "com.taeyeon.core.SharedPreferencesManager"
 *
 */

object Settings {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private const val SETTINGS_KEY = "SETTINGS"

    enum class DarkMode {
        SYSTEM_MODE, LIGHT_MODE, DARK_MODE
    }

    val INITIAL_SETTINGS_DATA =
        SettingsData(
            defaultTab = 2,
            defaultPassword = "0000",
            darkMode = DarkMode.SYSTEM_MODE,
            dynamicColor = false,
            fullScreenMode = false,
            screenAlwaysOn = false,
            tester = false
        )
        get() {
            return field.clone()
        }

    lateinit var settingsData: SettingsData

    class SettingsData(
        defaultTab: Int = INITIAL_SETTINGS_DATA.defaultTab,
        defaultPassword: String = INITIAL_SETTINGS_DATA.defaultPassword,
        fullScreenMode: Boolean = INITIAL_SETTINGS_DATA.fullScreenMode,
        screenAlwaysOn: Boolean = INITIAL_SETTINGS_DATA.screenAlwaysOn,
        darkMode: DarkMode = INITIAL_SETTINGS_DATA.darkMode,
        dynamicColor: Boolean = INITIAL_SETTINGS_DATA.dynamicColor,
        tester: Boolean = INITIAL_SETTINGS_DATA.tester
    ) : Cloneable {
        var defaultTab: Int = 2
            set(value) {
                field = value
                saveSettings()
            }
        var defaultPassword: String = "0000"
            set(value) {
                field = value
                saveSettings()
            }
        var fullScreenMode: Boolean = false
            set(value) {
                field = value
                saveSettings()
            }
        var screenAlwaysOn: Boolean = false
            set(value) {
                field = value
                saveSettings()
            }
        var darkMode: DarkMode = DarkMode.SYSTEM_MODE
            set(value) {
                field = value
                saveSettings()
            }
        var dynamicColor: Boolean = true
            set(value) {
                field = value
                saveSettings()
            }
        var tester: Boolean = true
            set(value) {
                field = value
                saveSettings()
            }

        init {
            this.defaultTab = defaultTab
            this.defaultPassword = defaultPassword
            this.fullScreenMode = fullScreenMode
            this.screenAlwaysOn = screenAlwaysOn
            this.darkMode = darkMode
            this.dynamicColor = dynamicColor
            this.tester = tester
        }

        public override fun clone(): SettingsData {
            return super.clone() as SettingsData
        }

    }

    fun loadSettings() {
        settingsData = sharedPreferencesManager.getAny(SETTINGS_KEY, SettingsData::class.java, INITIAL_SETTINGS_DATA.clone()).clone()
    }

    fun saveSettings() {
        if (Core.isSetUp()) sharedPreferencesManager.putAny(SETTINGS_KEY, settingsData)
    }

    @Suppress("DEPRECATION")
    fun applyFullScreenMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val window = Core.getActivity().window
            if (settingsData.fullScreenMode) {
                window.setDecorFitsSystemWindows(false)

                val controller = window.insetsController
                controller?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                window.setDecorFitsSystemWindows(true)
            }
        } else {
            if (settingsData.fullScreenMode) {
                val flag: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                Core.getActivity().window.decorView.systemUiVisibility = flag
            } else {
                val flag: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                Core.getActivity().window.decorView.systemUiVisibility = flag
            }
            Core.getActivity().window.decorView.requestLayout()
        }
    }

    fun applyScreenAlwaysOn() {
        if (settingsData.screenAlwaysOn) Core.getActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else Core.getActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun initializeSettings() {
        settingsData = INITIAL_SETTINGS_DATA.clone()
        saveSettings()
    }

    fun initialize() {
        sharedPreferencesManager = SharedPreferencesManager(SETTINGS_KEY)
        loadSettings()
    }

}