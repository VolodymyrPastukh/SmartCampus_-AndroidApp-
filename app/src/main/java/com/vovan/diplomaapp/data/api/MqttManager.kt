package com.vovan.diplomaapp.data.api

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.amazonaws.regions.Regions
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.util.*

private const val CUSTOMER_SPECIFIC_ENDPOINT = "a240uztzb3wu4b-ats.iot.us-east-2.amazonaws.com"
private const val COGNITO_POOL_ID = "us-east-2:af05d7dd-66aa-433c-95b8-15fc636f663d"

class MqttManager(private val context: Context) {

    private val region = Regions.US_EAST_2
    private val clientId = UUID.randomUUID().toString()
    lateinit var credentialsProvider: CognitoCachingCredentialsProvider

    private lateinit var _manager: AWSIotMqttManager
    private val manager: AWSIotMqttManager by lazy {
        createManager()
        _manager
    }

    private fun createManager() {
        credentialsProvider = CognitoCachingCredentialsProvider(
            context,
            COGNITO_POOL_ID,
            region
        )
        val temp = AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT)
        _manager = temp
    }


    /*
        Connection to AWS IoT Core Broker
        @return Observable<AwsConnectionState>
    */
    fun connect(): Observable<AWSIotMqttClientStatus> {
        Timber.d("Connection to AWS")
        return Observable.create{ subscriber ->
            manager.connect(
                credentialsProvider
            ) { status, throwable ->
                Timber.d("Status = $status")
                subscriber.onNext(status)
                if(throwable != null) subscriber.onError(throwable)
            }
        }

    }


    /*
        Subscribe to AWS IoT Core topic
        @param topic
            name of topic
        @return Observable<Any>
    */
    fun subscribe(topic: String): Observable<String> {
        return Observable.create{ subscriber ->
            manager.subscribeToTopic(
                topic,
                AWSIotMqttQos.QOS0
            ) { _, message ->
                try {
                    val data = String(message, Charsets.UTF_8)
                    subscriber.onNext(data)

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
    fun publish(topic: String, message: String): Completable {
        return Completable.create { subscriber ->
            try {
                manager.publishString(message, topic, AWSIotMqttQos.QOS0)
                subscriber.onComplete()
            } catch (e: Throwable) {
                subscriber.onError(e)
            }
        }
    }


    /*
        Disconnect from AWS IoT Core Broker
    */
    fun disconnect() {
        manager.disconnect()
    }

}

