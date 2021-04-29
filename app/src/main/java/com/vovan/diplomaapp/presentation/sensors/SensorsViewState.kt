package com.vovan.diplomaapp.presentation.sensors

import com.vovan.diplomaapp.entity.SensorsEntity

sealed class SensorsViewState {
    object Connecting: SensorsViewState()
    object Connected: SensorsViewState()

    data class Data(val data: SensorsEntity): SensorsViewState()

    data class Error(val message: String): SensorsViewState()
}