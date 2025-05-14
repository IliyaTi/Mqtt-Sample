import android.content.Context
import android.util.Log
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class HiveMqClient(
    private val context: Context,
    private val brokerUrl: String,
    private val port: Int = 1883,
    private val useTls: Boolean = false,
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private var client: Mqtt5AsyncClient? = null

    private val _events = MutableSharedFlow<MQTTEvent>(extraBufferCapacity = 50)
    val events = _events.asSharedFlow()

    sealed class MQTTEvent {
        data class MessageReceived(val topic: String, val payload: String) : MQTTEvent()
        data class ConnectionStatus(val isConnected: Boolean) : MQTTEvent()
        data class Error(val message: String, val throwable: Throwable? = null) : MQTTEvent()
    }

    fun connect(
        cleanSession: Boolean = true,
        keepAlive: Int = 60,
        username: String? = null,
        password: String? = null
    ) = ioScope.launch {
        try {
            client = Mqtt5Client.builder()
                .identifier("android-${UUID.randomUUID()}")
                .serverHost(brokerUrl)
                .serverPort(port)
                .apply { if (useTls) sslWithDefaultConfig() }
                .addConnectedListener { _events.tryEmit(MQTTEvent.ConnectionStatus(true)) }
                .addDisconnectedListener { _events.tryEmit(MQTTEvent.ConnectionStatus(false)) }
                .buildAsync()

            client!!.connectWith()
                .cleanStart(cleanSession)
                .keepAlive(keepAlive)
                .apply {
                    if (username != null && password != null) {
                        simpleAuth()
                            .username(username)
                            .password(password.toByteArray())
                            .applySimpleAuth()
                    }
                }
                .send()
                .get()

            Log.d(TAG, "Connected to $brokerUrl")
        } catch (e: Exception) {
            _events.tryEmit(MQTTEvent.Error("Connection failed", e))
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String, qos: MqttQos = MqttQos.AT_LEAST_ONCE) = ioScope.launch {
        try {
            client!!.subscribeWith()
                .topicFilter(topic)
                .qos(qos)
                .send()
                .get()

            // Set callback after successful subscription
            client!!.publishes(MqttGlobalPublishFilter.SUBSCRIBED) { publish ->
                handleMessage(publish.topic.toString(), publish.payloadAsBytes)
            }

            Log.d(TAG, "Subscribed to $topic")
        } catch (e: Exception) {
            _events.tryEmit(MQTTEvent.Error("Subscribe failed", e))
            Log.e(TAG, "Subscribe error", e)
        }
    }

    fun publish(
        topic: String,
        message: String,
        qos: MqttQos = MqttQos.AT_LEAST_ONCE,
        retain: Boolean = false
    ) = ioScope.launch {
        try {
            client!!.publishWith()
                .topic(topic)
                .payload(message.toByteArray())
                .qos(qos)
                .retain(retain)
                .send()
                .get()

            Log.d(TAG, "Message published to $topic")
        } catch (e: Exception) {
            _events.tryEmit(MQTTEvent.Error("Publish failed", e))
            Log.e(TAG, "Publish error", e)
        }
    }

    fun disconnect() = ioScope.launch {
        try {
            client!!.disconnect()
            Log.d(TAG, "Disconnected from broker")
        } catch (e: Exception) {
            _events.tryEmit(MQTTEvent.Error("Disconnect failed", e))
            Log.e(TAG, "Disconnect error", e)
        }
    }

    private fun handleMessage(topic: String, payload: ByteArray?) {
        ioScope.launch {
            try {
                val message = payload?.toString(Charsets.UTF_8) ?: ""
                _events.emit(MQTTEvent.MessageReceived(topic, message))
                Log.d(TAG, "Received on $topic: $message")
            } catch (e: Exception) {
                _events.emit(MQTTEvent.Error("Message parse error", e))
                Log.e(TAG, "Message handling error", e)
            }
        }
    }

    fun isConnected() = client?.state?.isConnected ?: false

    companion object {
        private const val TAG = "CoroutineMQTTClient"
    }

}