package com.example.nothing.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map

class NetViewModel(netLiveData: NetworkLiveData) {

    val connetionState: LiveData<ConnectionState> = map(netLiveData) { capabilities ->
        NetworkCapabilitiesMapper.map(capabilities)
    }
}