package com.example.mqttsample.data.repository

import android.content.Context
import com.example.mqttsample.data.source.remote.MqttRemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

class MQTTRemoteRepository(
    context: Context,
    hostUrl: String,
    clientId: String
) {

    val source = MqttRemoteSource(context, hostUrl, clientId)

    suspend fun connect(
        options: MqttConnectOptions,
        callback: MqttCallback,
        actionListener: IMqttActionListener
    ) {
        withContext(Dispatchers.IO) {
            source.connect(options, callback, actionListener)
        }
    }

    suspend fun disconnect(
        actionListener: IMqttActionListener
    ) {
        withContext(Dispatchers.IO) {
            source.disconnect(actionListener)
        }
    }

    suspend fun subscribe(
        topic: String,
        qos: Int,
        actionListener: IMqttActionListener
    ) {
        withContext(Dispatchers.IO) {
            source.subscribe(topic, qos, actionListener)
        }
    }

    suspend fun unSubscribe(
        topic: String,
        actionListener: IMqttActionListener
    ) {
        withContext(Dispatchers.IO) {
            source.unSubscribe(topic, actionListener)
        }
    }

    suspend fun publish(
        topic: String,
        message: String,
        qos: Int,
        retained: Boolean,
        actionListener: IMqttActionListener
    ) {
        withContext(Dispatchers.IO) {
            source.publish(topic, message, qos, retained, actionListener)
        }
    }



}