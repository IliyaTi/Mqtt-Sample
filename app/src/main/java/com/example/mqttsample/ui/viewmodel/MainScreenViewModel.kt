package com.example.mqttsample.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.source.local.entity.Broker
import com.example.mqttsample.data.source.local.entity.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repo: BrokerRepository
) : ViewModel() {

    val brokers = repo.getAllBrokersFlow()

    val selectedBroker = mutableStateOf<Broker?>(null)
    var devices = mutableStateOf<List<Device>>(emptyList())


    init {
//        fetchDevices()
    }


    fun fetchDevices() {
        viewModelScope.launch {
            snapshotFlow { selectedBroker.value }.collect { state ->
                state?.let {
                    devices.value = repo.getBrokerDevices(it.id)
                }
            }
        }
    }

}