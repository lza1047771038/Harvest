package com.open.core_base.utils.system

import android.app.ActivityManager
import android.content.Context
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object OSUtils {
    private const val ROM_MIUI = "MIUI"
    private const val ROM_EMUI = "EMUI"
    private const val ROM_FLYME = "FLYME"
    private const val ROM_OPPO = "OPPO"
    private const val ROM_SMARTISAN = "SMARTISAN"
    private const val ROM_VIVO = "VIVO"
    private const val ROM_QIKU = "QIKU"
    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"
    private var sName: String? = null
    private lateinit var sVersion: String
    val isEmui: Boolean
        get() = check(ROM_EMUI)

    val isMiui: Boolean
        get() = check(ROM_MIUI)

    val isVivo: Boolean
        get() = check(ROM_VIVO)

    val isOppo: Boolean
        get() = check(ROM_OPPO)

    val isFlyme: Boolean
        get() = check(ROM_FLYME)

    fun is360(): Boolean {
        return check(ROM_QIKU) || check("360")
    }

    val isSmartisan: Boolean
        get() = check(ROM_SMARTISAN)

    val name: String?
        get() {
            if (sName == null) {
                check("")
            }
            return sName
        }

    val version: String?
        get() {
            if (sVersion == null) {
                check("")
            }
            return sVersion
        }

    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }
        if (!TextUtils.isEmpty(getProp(KEY_VERSION_MIUI).also { sVersion = it!! })) {
            sName = ROM_MIUI
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_EMUI).also { sVersion = it!! })) {
            sName = ROM_EMUI
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_OPPO).also { sVersion = it!! })) {
            sName = ROM_OPPO
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_VIVO).also { sVersion = it!! })) {
            sName = ROM_VIVO
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_SMARTISAN).also { sVersion = it!! })) {
            sName = ROM_SMARTISAN
        } else {
            sVersion = Build.DISPLAY
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.toUpperCase()
            }
        }
        return sName == rom
    }

    fun getProp(name: String): String? {
        val line: String?
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

    fun audioSpeakerSettings(context: Context, speaker: Boolean) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (speaker) {
            audioManager.isSpeakerphoneOn = true
            audioManager.mode = AudioManager.MODE_NORMAL
            audioManager.setStreamVolume(
                AudioManager.MODE_NORMAL,
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FX_KEY_CLICK
            )
        } else {
            audioManager.isSpeakerphoneOn = false
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            audioManager.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                AudioManager.FX_KEY_CLICK
            )
        }
    }

    fun getProcessName(context: Context, pid: Int): String {
        val mActivityManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = mActivityManager.runningAppProcesses
        if (runningApps != null && runningApps.isNotEmpty()) {
            for (app in runningApps) {
                if (app.pid == pid) {
                    return app.processName
                }
            }
        }
        return ""
    }
}