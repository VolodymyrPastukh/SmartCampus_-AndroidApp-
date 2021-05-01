package com.vovan.diplomaapp.data

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.google.gson.Gson
import com.vovan.diplomaapp.data.api.MqttManager
import com.vovan.diplomaapp.domain.CampusRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import io.reactivex.Completable
import io.reactivex.Observable

class NetworkCampusRepository(private val api: MqttManager) : CampusRepository {
    private val gson = Gson()

    override fun connect(): Observable<ConnectionState> {
        return api.connect().map { status ->
            status.toAwsConnectionState()
        }
    }

    override fun subscribe(topic: String): Observable<SensorsEntity> {
        return api.subscribe(topic).map { message ->
            gson.fromJson(message, SensorsEntity::class.java)
        }
    }

    override fun publish(topic: String, data: String): Completable = api.publish(topic, data)


    override fun disconnect() {
        api.disconnect()
    }


    private fun AWSIotMqttClientStatus.toAwsConnectionState(): ConnectionState =
        when (this) {
            AWSIotMqttClientStatus.Connecting -> ConnectionState.Connecting
            AWSIotMqttClientStatus.Connected -> ConnectionState.Connected
            AWSIotMqttClientStatus.ConnectionLost -> ConnectionState.Disconnect
            AWSIotMqttClientStatus.Reconnecting -> ConnectionState.Connecting
        }
}