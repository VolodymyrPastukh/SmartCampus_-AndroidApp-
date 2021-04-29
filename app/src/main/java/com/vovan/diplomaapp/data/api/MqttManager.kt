package com.vovan.diplomaapp.data.api

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.amazonaws.regions.Regions
import timber.log.Timber
import java.util.*

private const val CUSTOMER_SPECIFIC_ENDPOINT = "<Secret data>"
private const val COGNITO_POOL_ID = "<Secret data>"

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
        @param callback AwsIotMqtt callback
            which defines in viewModel and gets respond about connection
     */
    fun connect(callback: AWSIotMqttClientStatusCallback) {
        Timber.d("Connection to AWS")
        manager.connect(
            credentialsProvider,
            callback
        )

    }

    /*
        Subscribe to AWS IoT Core topic
        @param topic
            name of topic
        @param callback AwsIotMqtt callback
            which gets data from other devices
     */
    fun subscribe(topic: String, callback: AWSIotMqttNewMessageCallback) {
        try {
            manager.subscribeToTopic(
                topic,
                AWSIotMqttQos.QOS0,
                callback
            )
        } catch (e: Throwable) {
            Timber.e("SUBSCRIBE ERROR")
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
        @param topic
            name of topic
        @param callback AwsIotMqtt callback
            which defines in viewModel and gets respond about connection
     */
    fun disconnect() {
        manager.disconnect()
    }

}


