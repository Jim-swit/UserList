package org.project.userlist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData



sealed class NetworkResult {
    object MOBILE : NetworkResult()
    object WIFI : NetworkResult()
    object NOT_CONNECTED : NetworkResult()

}

class NetworkConnect(
    private val context: Context
):LiveData<NetworkResult>() {
    private var connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            }
            else -> {
                val request = NetworkRequest.Builder().build()
                connectivityManager.registerNetworkCallback(request, connectivityManagerCallback())
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                super.onUnavailable()
                postValue(NetworkResult.NOT_CONNECTED)
            }
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                updateConnection()
            }

        }
        return networkCallback
    }


    private fun updateConnection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                when {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        postValue(NetworkResult.MOBILE)
                    }
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        postValue(NetworkResult.WIFI)
                    }
                    else -> {
                        postValue(NetworkResult.NOT_CONNECTED)
                    }
                }
            }
        }else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            activeNetwork?.type?.let {
                when(it) {
                    ConnectivityManager.TYPE_MOBILE -> {
                        postValue(NetworkResult.MOBILE)
                    }
                    ConnectivityManager.TYPE_WIFI -> {
                        postValue(NetworkResult.WIFI)
                    }
                    else -> {
                        postValue(NetworkResult.NOT_CONNECTED)
                    }
                }
            }
        }
    }
}