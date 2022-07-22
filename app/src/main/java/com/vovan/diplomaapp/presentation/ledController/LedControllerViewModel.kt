package com.vovan.diplomaapp.presentation.ledController

import androidx.lifecycle.*
import com.google.gson.Gson
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedControllerViewModel @Inject constructor(
    private val repository: MqttRepository,
    private val gson: Gson
) : ViewModel() {

    private val _connectionState = repository.connect().map { it.toSensorConnectionState() }.asLiveData()
    val connectionState: LiveData<SensorsConnectionState>
        get() = _connectionState

    private val _dataState = MutableLiveData<SensorDataState<List<Boolean>>>()
    val dataState: LiveData<SensorDataState<List<Boolean>>>
        get() = _dataState

    private fun publish(message: String) {
        viewModelScope.launch {
            val completedSuccess = repository.publish(TOPIC_PUB, message)
            if(completedSuccess) _dataState.postValue(SensorDataState(Rgb.getState()))
        }
    }


    fun turnOnLed(color: String) {
        val led = LedControllerEntity(
            "Vova",
            Rgb.setColor(color)
        )

        //Turn on buzzer if each color is active
        if (led.rgb == 7) led.buzzer = 1
        else led.buzzer = 0

        val message = gson.toJson(led)
        publish(message)
    }

    private object Rgb {
        private var red = false
        private var green = false
        private var blue = false

        fun setColor(color: String): Int {
            when (color) {
                RED_LED -> red = !red
                GREEN_LED -> green = !green
                BLUE_LED -> blue = !blue
            }
            return getRgbNumber()
        }

        private fun getRgbNumber(): Int {
            var result = 0
            if(red) result = result or 1
            if(green) result = result or 2
            if(blue) result = result or 4
            return result
        }

        fun getState(): List<Boolean> = listOf(red, green, blue)
    }

    companion object {
        const val RED_LED = "red"
        const val GREEN_LED = "green"
        const val BLUE_LED = "blue"
    }
}