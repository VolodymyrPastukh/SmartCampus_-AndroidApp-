package com.vovan.diplomaapp

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.vovan.diplomaapp.domain.entity.ConnectionState

fun AWSIotMqttClientStatus.toAwsConnectionState(): ConnectionState =
    when (this) {
        AWSIotMqttClientStatus.Connecting -> ConnectionState.Connecting
        AWSIotMqttClientStatus.Connected -> ConnectionState.Connected
        AWSIotMqttClientStatus.ConnectionLost -> ConnectionState.Disconnect
        AWSIotMqttClientStatus.Reconnecting -> ConnectionState.Connecting
    }