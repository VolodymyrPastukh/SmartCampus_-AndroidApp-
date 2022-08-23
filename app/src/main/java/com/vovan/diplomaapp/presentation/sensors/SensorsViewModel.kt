package com.vovan.diplomaapp.presentation.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.vovan.diplomaapp.TOPIC_SUB
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SensorsViewModel @Inject constructor(repository: MqttRepository) : ViewModel() {
    val connectionState = repository.connection.map { it.toSensorConnectionState() }.asLiveData()
    var dataState = repository.subscribe(TOPIC_SUB).map { SensorDataState(it) }.asLiveData()
}
