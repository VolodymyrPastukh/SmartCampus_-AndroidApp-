package com.vovan.diplomaapp.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import kotlinx.coroutines.flow.map

abstract class BaseViewModel(val repository: MqttRepository): ViewModel() {

    lateinit var connectionState: LiveData<SensorsConnectionState>

    init {
        initializing()
    }

    open fun initializing(){
        connectionState = repository.connection.map { it.toSensorConnectionState() }.asLiveData()
    }
}