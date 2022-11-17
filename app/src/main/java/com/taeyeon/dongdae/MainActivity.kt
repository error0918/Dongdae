@file:OptIn(ExperimentalAnimationApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.dongdae

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.dongdae.ui.theme.Theme

var screen by mutableStateOf(Screen.Main)

class MainActivity : ComponentActivity() {
    private var isSplashed by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            var isSplashScreen = false

            splashScreen.setSplashScreenTheme(R.style.Theme_Dongdae_Splash)
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofPropertyValuesHolder(
                    splashScreenView.iconView,
                    PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2f, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2f, 1f)
                ).run {
                    isSplashScreen = true
                    interpolator = AnticipateInterpolator()
                    duration = 100L
                    doOnEnd {
                        Core.initialize(applicationContext)
                        splashScreenView.remove()
                        isSplashed = true
                    }
                    start()
                }
            }
            if (!isSplashScreen) {
                Core.initialize(applicationContext)
                isSplashed = true
            }
        } else {
            installSplashScreen()
            Core.initialize(applicationContext)
            isSplashed = true
        }

        super.onCreate(savedInstanceState)

        Core.activityCreated(this)

        setContent {
            loadSettings()

            Theme {
                Surface {
                    if (isSplashed) {
                        AnimatedContent(
                            targetState = screen
                        ) {
                            when (it) {
                                Screen.Main -> {
                                    load()
                                    Box(
                                        modifier = Modifier.animateEnterExit(
                                            enter = scaleIn(),
                                            exit = scaleOut()
                                        )
                                    ) {
                                        Main.Main()
                                    }
                                }
                                Screen.Welcome -> {
                                    load()
                                    Box(
                                        modifier = Modifier.animateEnterExit(
                                            enter = scaleIn(),
                                            exit = scaleOut()
                                        )
                                    ) {
                                        Welcome.Welcome()
                                    }
                                }
                                Screen.InternetDisconnected -> {
                                    Box(
                                        modifier = Modifier.animateEnterExit(
                                            enter = scaleIn(),
                                            exit = scaleOut()
                                        )
                                    ) {
                                        InternetDisconnected.InternetDisconnected()
                                    }
                                }
                            }
                        }
                    }

                    if (tester) Tester.Tester()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Settings.applyFullScreenMode()
        Settings.applyScreenAlwaysOn()
    }

    override fun onRestart() {
        super.onRestart()
        Core.activityCreated(this)
    }

}