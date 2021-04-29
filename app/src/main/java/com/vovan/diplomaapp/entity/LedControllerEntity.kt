package com.vovan.diplomaapp.entity

data class LedControllerEntity(
    val message: String = "Led",
    val rgb: Int,
    var buzzer: Int = 0
)