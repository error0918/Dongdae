package com.taeyeon.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.getSystemService
import com.taeyeon.dongdae.R
import kotlin.system.exitProcess

object Utils {
    private var defaultToastLength = Toast.LENGTH_SHORT
        set(value) { if (value == Toast.LENGTH_SHORT || value == Toast.LENGTH_LONG) field = value }
    private var defaultIsShowToast = true

    fun toast(text: String, length: Int = defaultToastLength) {
        Toast.makeText(Core.getContext(), text, if (length == Toast.LENGTH_SHORT || length == Toast.LENGTH_LONG) length else defaultToastLength).show()
    }

    fun copy(label: String = Info.getApplicationName(), text: String, isShowToast: Boolean = defaultIsShowToast, toastLength: Int = defaultToastLength) {
        val clipboardManager = Core.getContext().getSystemService<ClipboardManager>()!!
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
        if (isShowToast) {
            if (label == Info.getApplicationName()) {
                toast(Core.getContext().resources.getString(R.string.copy_toast_message, text), toastLength)
            } else {
                toast(Core.getContext().resources.getString(R.string.copy_toast_message_with_label, label, text), toastLength)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun vibrate(milliseconds: Long) {
        val vibrator = Core.getContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(milliseconds)
    }

    fun shutDownApp() {
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }

    fun restartApp() {
        val packageManager = Core.getContext().packageManager
        val intent = packageManager.getLaunchIntentForPackage(Info.getPackage())
        val componentName = intent?.component
        Core.getActivity().startActivity(Intent.makeRestartActivityTask(componentName))
        exitProcess(0)
    }

    fun initializeData() {
        SharedPreferencesManager.clearAllSharedPreferencesManagers()
    }

    @Suppress("DEPRECATION")
    fun checkInternet(): Boolean {
        return Core.getContext().getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }
}