package com.example.mqttsample.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.source.local.entity.BrokerWithDevice
import com.example.mqttsample.data.source.remote.MQTTClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DeviceScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brokerRepository: BrokerRepository,
) : ViewModel() {

    var client: MQTTClient? = null

    val connection = mutableStateOf("Connecting...")
    val brightness = mutableStateOf("")

    val device = mutableStateOf<BrokerWithDevice?>(null)

    val error = mutableStateOf<String?>(null)


    init {
        viewModelScope.launch {
            snapshotFlow { device.value }.collect {
                it?.let {
                    client = MQTTClient(context, hostUrl = it.broker.host, UUID.randomUUID().toString())
                    connect()
                }
            }



        }

    }


    fun connect() {
        client?.connect(
            callback = object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    connection.value = "Disconnected: ${cause?.message}"
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    brightness.value = message?.payload?.decodeToString() ?: ""
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }

            },
            actionListener = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    connection.value = "Connected"
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    connection.value = "Failed: ${exception?.message}"
                }

            }
        )
    }


    fun populateDevice(deviceId: Int) {
        viewModelScope.launch {
            val result = brokerRepository.getDeviceWithBroker(deviceId)
            device.value = result
        }
    }



}