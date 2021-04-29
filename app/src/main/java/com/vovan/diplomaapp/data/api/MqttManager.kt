package com.vovan.diplomaapp.data.api

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.amazonaws.regions.Regions
import com.google.gson.Gson
import io.reactivex.Observable
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.util.*

private const val CUSTOMER_SPECIFIC_ENDPOINT = "<Secret Data>"
private const val COGNITO_POOL_ID = "<Secret Data>"

class MqttManager(private val context: Context) {
    val gson = Gson()
    private val region = Regions.US_EAST_2
    private val clientId = UUID.randomUUID().toString()
    lateinit var credentialsProvider: CognitoCachingCredentialsProvider

    private lateinit var _manager: AWSIotMqttManager
    val manager: AWSIotMqttManager by lazy {
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
        return Observable<AwsConnectionState>
    */
    fun connect(): Observable<AwsConnectionState> {
        Timber.d("Connection to AWS")
        return Observable.create{ subscriber ->
            manager.connect(
                credentialsProvider
            ) { status, throwable ->
                Timber.d("Status = $status")
                when (status) {
                    AWSIotMqttClientStatus.Connecting -> subscriber.onNext(AwsConnectionState.Connecting)
                    AWSIotMqttClientStatus.Connected -> subscriber.onNext(AwsConnectionState.Connected)
                    AWSIotMqttClientStatus.ConnectionLost -> subscriber.onNext(AwsConnectionState.Disconnect)
                    AWSIotMqttClientStatus.Reconnecting -> subscriber.onNext(AwsConnectionState.Connecting)
                    else -> Timber.e("ELSE ERROR $throwable")
                }
            }
        }

    }


    /*
        Subscribe to AWS IoT Core topic
        @param topic
            name of topic
        return Observable<Any>
    */
    inline fun <reified T: Any> subscribe(topic: String): Observable<T> {
        return Observable.create{ subscriber ->
            manager.subscribeToTopic(
                topic,
                AWSIotMqttQos.QOS0
            ) { _, data ->
                try {
                    val message = String(data, Charsets.UTF_8)
                    val sensors = gson.fromJson(message, T::class.java)
                    subscriber.onNext(sensors)

                } catch (e: UnsupportedEncodingException) {
                    Timber.e("Message encoding error. $e")
                    subscriber.onError(e)
                }
            }
        }

    }


    /*
        Publishing to AWS IoT Core Broker
        @param topic
            name of topic
        @param callback AwsIotMqtt callback
            which defines in viewModel and gets respond about connection

     */
    fun publish(topic: String, message: String) {
        try {
            manager.publishString(message, topic, AWSIotMqttQos.QOS0)
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }


    /*
        Disconnect from AWS IoT Core Broker

    */
    fun disconnect() {
        manager.disconnect()
    }

}

sealed class AwsConnectionState {
    object Connecting : AwsConnectionState()
    object Connected : AwsConnectionState()
    object Disconnect : AwsConnectionState()
}
