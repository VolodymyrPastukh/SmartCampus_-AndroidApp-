package com.vovan.diplomaapp.domain.entity

data class LedControllerEntity(
    val message: String = "Led",
    val rgb: Int,
    var buzzer: Int = 0
)