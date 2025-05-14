package com.example.mqttsample.ui.viewmodel

import HiveMqClient
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.repository.ConnectionState
import com.example.mqttsample.data.repository.MQTTMessage
import com.example.mqttsample.data.repository.MQTTRepository
import com.example.mqttsample.data.repository.MQTTRepositoryImpl
import com.example.mqttsample.data.source.local.entity.BrokerWithDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DeviceScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brokerRepository: BrokerRepository,
) : ViewModel() {

    private lateinit var hiveMqRepo: MQTTRepository

    var connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Connecting)
    var messages = MutableSharedFlow<MQTTMessage>()

    var modifyField by mutableStateOf("")

    private val _uiState = MutableStateFlow<MQTTUIState>(MQTTUIState.Idle)
    val uiState: StateFlow<MQTTUIState> = _uiState


    val deviceWithBroker = mutableStateOf<BrokerWithDevice?>(null)



    fun populateDevice(deviceId: Int) {
        viewModelScope.launch {
            val result = brokerRepository.getDeviceWithBroker(deviceId)
            deviceWithBroker.value = result

            val port = result.broker.port

            hiveMqRepo = MQTTRepositoryImpl(
                HiveMqClient(
                    context = context,
                    brokerUrl = result.broker.host,
                    port = if (port.isBlank()) 1883 else port.toInt()
                )
            )

            launch {
                hiveMqRepo.connectionState.collect {
                    connectionState.emit(it)
                }
            }

            launch {
                hiveMqRepo.messages.collect {
                    messages.emit(it)
                }
            }


            connect()


            connectionState.collect { s ->
                if (s is ConnectionState.Connected) {
                    subscribe()
                }
            }


        }
    }

    fun connect() {
        viewModelScope.launch {
            _uiState.value = MQTTUIState.Loading
            try {
                hiveMqRepo.connect()
                _uiState.value = MQTTUIState.Connected
            } catch (e: Exception) {
                _uiState.value = MQTTUIState.Error(e.message ?: "Connection failed")
            }
        }
    }

    fun subscribe() {
        viewModelScope.launch {
            _uiState.value = MQTTUIState.Loading
            try {
                hiveMqRepo.subscribe(deviceWithBroker.value!!.device.topic)
                _uiState.value = MQTTUIState.Subscribed(deviceWithBroker.value!!.device.topic)
            } catch (e: Exception) {
                _uiState.value = MQTTUIState.Error(e.message ?: "Subscription failed")
            }
        }
    }

    fun publish() {
        viewModelScope.launch {
            _uiState.value = MQTTUIState.Loading
            try {
                hiveMqRepo.publish(deviceWithBroker.value!!.device.topic, modifyField)
                _uiState.value = MQTTUIState.MessageSent
            } catch (e: Exception) {
                _uiState.value = MQTTUIState.Error(e.message ?: "Publish failed")
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            hiveMqRepo.disconnect()
            _uiState.value = MQTTUIState.Disconnected
        }
    }


    override fun onCleared() {
        disconnect()
        super.onCleared()
    }


}


sealed class MQTTUIState {
    object Idle : MQTTUIState()
    object Loading : MQTTUIState()
    object Connected : MQTTUIState()
    data class Subscribed(val topic: String) : MQTTUIState()
    object MessageSent : MQTTUIState()
    object Disconnected : MQTTUIState()
    data class Error(val message: String) : MQTTUIState()
}