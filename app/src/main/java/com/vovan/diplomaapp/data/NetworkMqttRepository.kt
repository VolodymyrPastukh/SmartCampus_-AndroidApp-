package com.vovan.diplomaapp.data

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttMessageDeliveryCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.google.gson.Gson
import com.vovan.diplomaapp.di.ApplicationScope
import com.vovan.diplomaapp.di.DefaultDispatcher
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.toAwsConnectionState
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.io.UnsupportedEncodingException

class NetworkMqttRepository(
    private val manager: AWSIotMqttManager,
    private val credentialsProvider: CognitoCachingCredentialsProvider,
    private val gson: Gson,
    @ApplicationScope private val externalScope: CoroutineScope,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : MqttRepository {

    override val connection = callbackFlow {
        manager.connect(
            credentialsProvider
        ) { status, throwable ->
            if (throwable != null) {
                Timber.e("Connection error ${throwable.message}")
//                close(throwable)
            }
            Timber.i("Connection status ${status}")
            trySend(status.toAwsConnectionState()).isSuccess
        }

        awaitClose {
            Timber.e("Connection FlowCallback closed")
            manager.disconnect()
        }
    }.shareIn(externalScope, SharingStarted.Eagerly, 1)

    override fun subscribe(topic: String): Flow<SensorsEntity> = callbackFlow<SensorsEntity> {
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
    }.flowOn(defaultDispatcher)

    override suspend fun publish(topic: String, data: LedControllerEntity): Boolean {
        val message = gson.toJson(data)
        return withContext(Dispatchers.IO) {
            try {
                manager.publishString(message, topic, AWSIotMqttQos.QOS0)
                true
            } catch (e: Throwable) {
                Timber.e(e)
                false
            }
        }
    }


}