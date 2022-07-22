package com.vovan.diplomaapp.presentation.sensors

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.vovan.diplomaapp.TOPIC_SUB
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SensorsViewModel @Inject constructor(
    private val repository: MqttRepository,
) : ViewModel() {

    private val _connectionState =
        repository.connect().map { it.toSensorConnectionState() }.asLiveData()
    val connectionState: LiveData<SensorsConnectionState>
        get() = _connectionState

    private var _dataState =
        repository.subscribe(TOPIC_SUB).map { SensorDataState(it) }.asLiveData()
    val dataState: LiveData<SensorDataState<SensorsEntity>>
        get() = _dataState
}
