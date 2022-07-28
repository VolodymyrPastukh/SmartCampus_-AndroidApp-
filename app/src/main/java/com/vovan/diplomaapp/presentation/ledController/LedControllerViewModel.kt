package com.vovan.diplomaapp.presentation.ledController

import androidx.lifecycle.*
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.defineSharedState
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedControllerViewModel @Inject constructor(
    private val repository: MqttRepository,
) : ViewModel() {

    val connectionState = repository.connection.map { it.toSensorConnectionState() }.asLiveData()
    private val _dataState = MutableLiveData(SensorDataState(Rgb.getState()))
    val dataState: LiveData<SensorDataState<List<Boolean>>>
        get() = _dataState

    private fun publish(ledEntity: LedControllerEntity) {
        viewModelScope.launch {
            val completedSuccess = repository.publish(TOPIC_PUB, ledEntity)
            if (completedSuccess) _dataState.value = SensorDataState(Rgb.getState())
        }
    }


    fun clickOnLed(color: String) {
        val led = LedControllerEntity("Feel the pain!!!", Rgb.setColor(color))
            .also { it.buzzer = if (it.rgb == 7) 1 else 0 }

        publish(led)
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

        private fun getRgbNumber(): Int = defineSharedState(red, green, blue)

        fun getState(): List<Boolean> = listOf(red, green, blue)
    }

    companion object {
        const val RED_LED = "red"
        const val GREEN_LED = "green"
        const val BLUE_LED = "blue"
    }
}