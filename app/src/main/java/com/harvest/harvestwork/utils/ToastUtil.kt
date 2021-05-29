package com.harvest.harvestwork.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.harvest.core_base.interfaces.IContext
import com.harvest.core_base.service.ServiceFacade

object ToastUtil {

    private val mainHandler = Handler(Looper.getMainLooper())

    @JvmStatic
    fun showToast(message: String?, toastLong: Boolean = false) {
        val iContext = ServiceFacade.getInstance().get(IContext::class.java) ?: return
        mainHandler.post {
            Toast.makeText(
                iContext.context.applicationContext, message,
                if (toastLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            ).show()
        }
    }
}