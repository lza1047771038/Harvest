package com.open.core_network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.harvest.core_base.interfaces.IContext
import com.harvest.core_base.service.ServiceFacade

class NetworkStatusUtils {
    companion object {

        private var instance: NetworkStatusUtils? = null

        @JvmStatic
        fun getInstance(): NetworkStatusUtils {
            if (instance == null) {
                synchronized(NetworkStatusUtils::class.java) {
                    if (instance == null) {
                        instance = NetworkStatusUtils()
                    }
                }
            }
            return instance!!
        }
    }

    fun registerNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        getConnectivityManager().registerNetworkCallback(request, callback)
    }

    fun unregisterNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        getConnectivityManager().unregisterNetworkCallback(callback)
    }

    private fun getConnectivityManager(): ConnectivityManager {
        val context = ServiceFacade.getInstance().get(IContext::class.java).context
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager = getConnectivityManager()
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    fun isNetworkEnable():Boolean{
        val connectivityManager = getConnectivityManager()
        return connectivityManager.activeNetworkInfo?.isAvailable ?: false
    }
}