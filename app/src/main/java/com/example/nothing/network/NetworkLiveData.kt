package com.example.nothing.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData


class NetworkLiveData(
    private val connectivityManager: ConnectivityManager
) : LiveData<NetworkCapabilities>() {

    private var activeNetwork: Network? = null
    private val allNetworks = ArrayMap<Network, NetworkCapabilities>()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            allNetworks.remove(network)
        }

        override fun onCapabilitiesChanged(net: Network, netCap: NetworkCapabilities) {
            allNetworks[net] = netCap
            updateActiveNetwork()
        }
    }

    override fun onActive() {
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private fun updateActiveNetwork() {
        activeNetwork = connectivityManager.activeNetwork
        val capabilities = allNetworks[activeNetwork]
        if (capabilities != value) {
            postValue(capabilities)
        }
    }
}