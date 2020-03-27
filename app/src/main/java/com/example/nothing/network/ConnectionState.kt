package com.example.nothing.network

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build


sealed class ConnectionState {

    object NotConnected : ConnectionState()

    data class Connected(
        val linkDownstreamBandwidthKbps: Int,
        val linkUpstreamBandwidthKbps: Int,
        val transports: List<String>,
        val hasInternet: Boolean
    ) : ConnectionState()
}

object NetworkCapabilitiesMapper {

    private val transports: Map<Int, String> = mutableMapOf(
        TRANSPORT_BLUETOOTH to "Bluetooth",
        TRANSPORT_CELLULAR to "Cellular",
        TRANSPORT_ETHERNET to "Ethernet",
        TRANSPORT_VPN to "VPN",
        TRANSPORT_WIFI to "Wi-Fi",
        TRANSPORT_LOWPAN to "LoWPAN",
        TRANSPORT_WIFI_AWARE to "Wi-Fi Aware"
    )

    fun map(capabilities: NetworkCapabilities?): ConnectionState =
        capabilities?.let {
            ConnectionState.Connected(
                linkDownstreamBandwidthKbps = capabilities.linkDownstreamBandwidthKbps,
                linkUpstreamBandwidthKbps = capabilities.linkUpstreamBandwidthKbps,
                transports = transportNames(capabilities),
                hasInternet = capabilities.hasCapability(NET_CAPABILITY_INTERNET)
            )
        } ?: ConnectionState.NotConnected

    private fun transportNames(capabilities: NetworkCapabilities): List<String> = transports
        .filter { entry -> capabilities.hasTransport(entry.key) }
        .map { it.value }
}