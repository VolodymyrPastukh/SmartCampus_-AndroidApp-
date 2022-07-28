package com.vovan.diplomaapp

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.vovan.diplomaapp.data.dto.SensorsDTO
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import java.text.SimpleDateFormat
import java.util.*

fun AWSIotMqttClientStatus.toAwsConnectionState(): ConnectionState =
    when (this) {
        AWSIotMqttClientStatus.Connecting -> ConnectionState.Connecting
        AWSIotMqttClientStatus.Connected -> ConnectionState.Connected
        AWSIotMqttClientStatus.ConnectionLost -> ConnectionState.Disconnect
        AWSIotMqttClientStatus.Reconnecting -> ConnectionState.Connecting
    }

fun SensorsDTO.toEntity(): SensorsEntity = SensorsEntity(
    convertLongToTime(time, "HH:mm:ss"),
    device,
    temperature,
    light,
    pressure
)

fun SensorsDTO.toEntity(tableName: String): SensorsEntity {
    var formattedTime = ""
    when (tableName) {
        TODAY_L -> formattedTime = convertLongToTime(time, "HH:mm:ss")
        DAILY_L -> formattedTime = convertLongToTime(time, "yyyy.MM.dd")
    }
    return SensorsEntity(
        formattedTime,
        device,
        temperature,
        light,
        pressure
    )
}

fun convertLongToTime(time: Long, pattern: String): String {
    val date = Date(time)
    val format = SimpleDateFormat(pattern, Locale.GERMANY)
    return format.format(date)
}

fun defineSharedState(vararg states: Boolean): Int {
    var result = 0
    states.forEachIndexed { position, state -> if (state) result = result or (1 shl position) }
    return result
}

fun defineSharedStateReversed(vararg states: Boolean) = defineSharedState(*states.reversed().toBooleanArray())