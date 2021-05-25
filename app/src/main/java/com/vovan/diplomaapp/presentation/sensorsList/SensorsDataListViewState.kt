package com.vovan.diplomaapp.presentation.sensorsList

import com.vovan.diplomaapp.domain.entity.SensorsEntity

sealed class SensorsDataListViewState{
    object Loading: SensorsDataListViewState()
    data class Data(val data: List<SensorsEntity>): SensorsDataListViewState()
    data class Error(val message: String): SensorsDataListViewState()
}
