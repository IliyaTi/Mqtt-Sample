package com.example.mqttsample.data.source.remote

import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class MQTTClient(
    context: Context,
    hostUrl: String,
    clientId: String = ""
) {

    companion object {
//        private var instance: MqttRemoteSource? = null
//
//        fun getInstance(
//            context: Context,
//            hostUrl: String,
//            clientId: String
//        ): MqttRemoteSource {
//            synchronized(this) {
//                if (instance == null) {
//                    instance = MqttRemoteSource(context, hostUrl, clientId)
//                }
//                return instance as MqttRemoteSource
//            }
//        }

    }

    private var mqttClient = MqttAndroidClient(context, hostUrl, clientId)


    fun connect(
        options: MqttConnectOptions = MqttConnectOptions(),
        callback: MqttCallback? = null,
        actionListener: IMqttActionListener
    ) {

        callback?.let { mqttClient.setCallback(it) }

        try {
            mqttClient.connect(options, null, actionListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun disconnect(actionListener: IMqttActionListener? = null) {
        try {
            mqttClient.disconnect(null, actionListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun subscribe(
        topic: String,
        qos: Int = 1,
        actionListener: IMqttActionListener
    ) {
        try {
            mqttClient.subscribe(topic, qos, null, actionListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun unSubscribe(
        topic: String,
        actionListener: IMqttActionListener
    ) {
        try {
            mqttClient.unsubscribe(topic, null, actionListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun publish(
        topic: String,
        message: String,
        qos: Int,
        retained: Boolean,
        actionListener: IMqttActionListener
    ) {
        try {
            val mqttMessage = MqttMessage().apply {
                payload = message.toByteArray()
                this.qos = qos
                isRetained = retained
            }
            mqttClient.publish(topic, mqttMessage, null, actionListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }




}