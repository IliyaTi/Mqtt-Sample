package com.example.mqttsample.ui.viewmodel

import HiveMqClient
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.repository.MQTTRepository
import com.example.mqttsample.data.repository.MQTTRepositoryImpl
import com.example.mqttsample.data.source.local.entity.BrokerWithDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DeviceScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brokerRepository: BrokerRepository,
) : ViewModel() {

    private var hiveMqRepo: MQTTRepository? = null

    val connectionState = hiveMqRepo.connectionState
    val messages = hiveMqRepo.messages

    private val _uiState = MutableStateFlow<MQTTUIState>(MQTTUIState.Idle)
    val uiState: StateFlow<MQTTUIState> = _uiState

    val connection = mutableStateOf("Connecting...")
    val brightness = mutableStateOf("0")

    val powerState = mutableStateOf(false)

    val device = mutableStateOf<BrokerWithDevice?>(null)

    val error = mutableStateOf<String?>(null)


    init {
        viewModelScope.launch {
            snapshotFlow { device.value }.collect {
                it?.let {
                    hiveMqRepo = MQTTRepositoryImpl(
                        HiveMqClient(
                            context = context,
                            brokerUrl = it.broker.host,
                            port = it.broker.port.toInt()
                        )
                    )

                }
            }



        }

    }

    fun connect() {
        viewModelScope.launch {
            _uiState.value = MQTTUIState.Loading
            try {
                repository.connect()
                _uiState.value = MQTTUIState.Connected
            } catch (e: Exception) {
                _uiState.value = MQTTUIState.Error(e.message ?: "Connection failed")
            }
        }
    }

    fun subscribe(topic: String) {
        viewModelScope.launch {
            _uiState.value = MQTTUIState.Loading
            try {
                repository.subscribe(topic)
                _uiState.value = MQTTUIState.Subscribed(topic)
            } catch (e: Exception) {
                _uiState.value = MQTTUIState.Error(e.message ?: "Subscription failed")
            }
        }
    }

    fun publish(topic: String, message: String) {
        viewModelScope.launch {
            _uiState.value = MQTTUIState.Loading
            try {
                repository.publish(topic, message)
                _uiState.value = MQTTUIState.MessageSent
            } catch (e: Exception) {
                _uiState.value = MQTTUIState.Error(e.message ?: "Publish failed")
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            repository.disconnect()
            _uiState.value = MQTTUIState.Disconnected
        }
    }
}


    fun populateDevice(deviceId: Int) {
        viewModelScope.launch {
            val result = brokerRepository.getDeviceWithBroker(deviceId)
            device.value = result
        }
    }



}