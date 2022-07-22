package com.vovan.diplomaapp.presentation.model

import com.vovan.diplomaapp.domain.entity.ConnectionState

sealed class SensorsConnectionState {
    object Connecting: SensorsConnectionState()
    object Connected: SensorsConnectionState()
    data class Error(val message: String): SensorsConnectionState()
}

fun ConnectionState.toSensorConnectionState(): SensorsConnectionState = when (this) {
    ConnectionState.Connecting -> SensorsConnectionState.Connecting
    ConnectionState.Connected -> SensorsConnectionState.Connected
    ConnectionState.Disconnect -> SensorsConnectionState.Error("Disconnect")
}

data class SensorDataState<T>(val data: T)