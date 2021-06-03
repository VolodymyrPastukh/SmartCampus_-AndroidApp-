package com.vovan.diplomaapp

import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.vovan.diplomaapp.data.dto.SensorsDTO
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import java.text.SimpleDateFormat
import java.util.*

const val TODAY = "SC_DataToday"
const val DAILY = "SC_DailyData"

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
    var formatedTime = ""
    when (tableName) {
        TODAY -> formatedTime = convertLongToTime(time, "HH:mm:ss")
        DAILY -> formatedTime = convertLongToTime(time, "yyyy.MM.dd")
    }
    return SensorsEntity(
        formatedTime,
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
