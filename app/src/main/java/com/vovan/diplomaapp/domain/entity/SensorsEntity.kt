package com.vovan.diplomaapp.domain.entity

data class SensorsEntity(
    val time: String,
    val device: String,
    val temperature: Float,
    val light: Int,
    val pressure: Int
)