package com.vovan.diplomaapp.data

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.google.gson.Gson
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.toAwsConnectionState
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber
import java.io.UnsupportedEncodingException

class NetworkMqttRepository(
    private val manager: AWSIotMqttManager,
    private val credentialsProvider: CognitoCachingCredentialsProvider,
    private val gson: Gson
) : MqttRepository {

    /*
        Connection to AWS IoT Core Broker
        @return Observable<ConnectionState>
    */
    override fun connect(): Observable<ConnectionState> {
        return Observable.create { subscriber ->
            manager.connect(
                credentialsProvider
            ) { status, throwable ->
                Timber.d("Status = $status")
                subscriber.onNext(status.toAwsConnectionState())
                if (throwable != null) subscriber.onError(throwable)
            }
        }
    }

    /*
        Subscribe to AWS IoT Core topic
        @param topic
            name of topic
        @return Observable<SensorsEntity>
    */
    override fun subscribe(topic: String): Observable<SensorsEntity> {
        return Observable.create { subscriber ->
            manager.subscribeToTopic(
                topic,
                AWSIotMqttQos.QOS0
            ) { _, message ->
                try {
                    val data = String(message, Charsets.UTF_8)
                    val sensorsEntity = gson.fromJson(data, SensorsEntity::class.java)
                    subscriber.onNext(sensorsEntity)

                } catch (e: UnsupportedEncodingException) {
                    subscriber.onError(e)
                }
            }
        }
    }

    /*
        Publishing to AWS IoT Core Broker
        @param topic
            name of topic
        @param message
            object in json
        @return Completable (RxJava object)
     */
    override fun publish(topic: String, data: String): Completable {
        return Completable.create { subscriber ->
            try {
                manager.publishString(data, topic, AWSIotMqttQos.QOS0)
                subscriber.onComplete()
            } catch (e: Throwable) {
                subscriber.onError(e)
            }
        }
    }

    /*
        Disconnect from AWS IoT Core Broker
    */
    override fun disconnect() {
        manager.disconnect()
    }

}