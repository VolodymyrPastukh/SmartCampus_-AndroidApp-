package com.vovan.diplomaapp.data.dto

data class ItemsSensorsDTO(
    val Items: List<SensorsDTO>,
    val Count: Int,
    val ScannedCount: Int
)

data class SensorsDTO(
    val time: Long,
    val device: String,
    val temperature: Float,
    val light: Float,
    val pressure: Int
)