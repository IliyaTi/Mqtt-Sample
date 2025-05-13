package com.example.mqttsample.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

class RemoteRepository constructor (
    context: Context,
    val hostUrl: String,
    val clientId: String
) {

//    companion object {
//
//        @Volatile
//        private var instance: RemoteRepository? = null
//
//
//        fun getInstance(context: Context, hostUrl: String, clientId: String): RemoteRepository {
//
//            if (instance != null && hostUrl == instance!!.hostUrl && clientId == instance!!.clientId) {
//                return instance!!
//            }
//
//            return RemoteRepository(context, hostUrl, clientId)
//
//        }
//
//    }
//
////    val source = MqttRemoteSource(context, hostUrl, clientId)
//
//    suspend fun connect(
//        options: MqttConnectOptions,
//        callback: MqttCallback,
//        actionListener: IMqttActionListener
//    ) {
//        withContext(Dispatchers.IO) {
//            source.connect(options, callback, actionListener)
//        }
//    }
//
//    suspend fun disconnect(
//        actionListener: IMqttActionListener
//    ) {
//        withContext(Dispatchers.IO) {
//            source.disconnect(actionListener)
//        }
//    }
//
//    suspend fun subscribe(
//        topic: String,
//        qos: Int,
//        actionListener: IMqttActionListener
//    ) {
//        withContext(Dispatchers.IO) {
//            source.subscribe(topic, qos, actionListener)
//        }
//    }
//
//    suspend fun unSubscribe(
//        topic: String,
//        actionListener: IMqttActionListener
//    ) {
//        withContext(Dispatchers.IO) {
//            source.unSubscribe(topic, actionListener)
//        }
//    }
//
//    suspend fun publish(
//        topic: String,
//        message: String,
//        qos: Int,
//        retained: Boolean,
//        actionListener: IMqttActionListener
//    ) {
//        withContext(Dispatchers.IO) {
//            source.publish(topic, message, qos, retained, actionListener)
//        }
//    }



}





class RemoteRepositoryFactory() {

    fun create(
        context: Context,
        hostUrl: String,
        clientId: String
    ) : RemoteRepository {
        return RemoteRepository(context, hostUrl, clientId)
    }

}
