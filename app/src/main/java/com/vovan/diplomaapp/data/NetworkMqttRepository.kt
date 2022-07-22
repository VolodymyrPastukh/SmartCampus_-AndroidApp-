package com.vovan.diplomaapp.data

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttMessageDeliveryCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.google.gson.Gson
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.toAwsConnectionState
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.UnsupportedEncodingException

class NetworkMqttRepository(
    private val manager: AWSIotMqttManager,
    private val credentialsProvider: CognitoCachingCredentialsProvider,
    private val gson: Gson
) : MqttRepository {

    override fun connect(): Flow<ConnectionState> = callbackFlow {
        manager.connect(
            credentialsProvider
        ) { status, throwable ->
            if (throwable != null) {
                Timber.e("Connection error ${throwable.message}")
                close(throwable)
            }
            Timber.i("Connection status ${status}")
            trySend(status.toAwsConnectionState()).isSuccess
        }

        awaitClose {
            Timber.e("Connection FlowCallback closed")
            manager.disconnect()
        }
    }

    override fun subscribe(topic: String): Flow<SensorsEntity> = callbackFlow {
        manager.subscribeToTopic(topic, AWSIotMqttQos.QOS0) { _, message ->
            try {
                val data = String(message, Charsets.UTF_8)
                val sensorsEntity = gson.fromJson(data, SensorsEntity::class.java)
                trySend(sensorsEntity).isSuccess

            } catch (e: UnsupportedEncodingException) {
                close(e)
            }
        }
        awaitClose {
            Timber.e("Subscribe FlowCallback closed")
            manager.unsubscribeTopic(topic)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun publish(topic: String, data: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                manager.publishString(data, topic, AWSIotMqttQos.QOS0)
                true
            } catch (e: Throwable) {
                Timber.e(e)
                false
            }
        }
    }
}