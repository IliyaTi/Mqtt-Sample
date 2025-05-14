package com.example.mqttsample.data.repository

import HiveMqClient
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


interface MQTTRepository {
    val connectionState: StateFlow<ConnectionState>
    val messages: SharedFlow<MQTTMessage>

    suspend fun connect()
    suspend fun subscribe(topic: String)
    suspend fun publish(topic: String, message: String)
    suspend fun disconnect()
}


class MQTTRepositoryImpl (
    private val mqttClient: HiveMqClient
) : MQTTRepository, DefaultLifecycleObserver {
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Connecting)
    override val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _messages = MutableSharedFlow<MQTTMessage>()
    override val messages: SharedFlow<MQTTMessage> = _messages

    init {
        CoroutineScope(Dispatchers.IO).launch {
            mqttClient.events.collect { event ->
                when (event) {
                    is HiveMqClient.MQTTEvent.MessageReceived -> {
                        _messages.emit(MQTTMessage(event.topic, event.payload))
                    }
                    is HiveMqClient.MQTTEvent.ConnectionStatus -> {
                        _connectionState.value = if (event.isConnected) {
                            ConnectionState.Connected
                        } else {
                            ConnectionState.Disconnected
                        }
                    }
                    is HiveMqClient.MQTTEvent.Error -> {
                        _connectionState.value = ConnectionState.Error(event.message)
                    }

                    else -> {}
                }
            }
        }
    }

    override suspend fun connect() {
        mqttClient.connect(
            username = "user",
            password = "pass"
        )
    }

    override suspend fun subscribe(topic: String) {
        mqttClient.subscribe(topic)
    }

    override suspend fun publish(topic: String, message: String) {
        mqttClient.publish(topic, message)
    }

    override suspend fun disconnect() {
        mqttClient.disconnect()
    }

    // Handle lifecycle events
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        CoroutineScope(Dispatchers.IO).launch {
            disconnect()
        }
    }
}




sealed class ConnectionState {
    object Connecting: ConnectionState()
    object Connected : ConnectionState()
    object Disconnected : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

data class MQTTMessage(val topic: String, val payload: String)
