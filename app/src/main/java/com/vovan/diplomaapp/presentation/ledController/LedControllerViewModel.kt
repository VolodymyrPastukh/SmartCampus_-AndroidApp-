package com.vovan.diplomaapp.presentation.ledController

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.defineSharedState
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.presentation.base.BaseViewModel
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LedControllerViewModel @Inject constructor(
    repository: MqttRepository,
) : BaseViewModel(repository) {

    private val _dataState = MutableLiveData(SensorDataState(Rgb.getState()))
    val dataState: LiveData<SensorDataState<List<Boolean>>>
        get() = _dataState

    private fun publish(ledEntity: LedControllerEntity) {
        viewModelScope.launch {
            val completedSuccess = repository.publish(TOPIC_PUB, ledEntity)
            if (completedSuccess) _dataState.value = SensorDataState(Rgb.getState())
        }
    }

    fun clickOnLed(color: Color) {
        val led = LedControllerEntity("Online publish request", Rgb.setColor(color))
            .also { it.buzzer = if (it.rgb == 7) 1 else 0 }
        when (connectionState.value) {
            SensorsConnectionState.Connected -> {
                publish(led)
            }
            else -> _dataState.value = SensorDataState(Rgb.getState())
        }
    }

    object Rgb {
        private var red = false
        private var green = false
        private var blue = false

        fun setColor(color: Color): Int {
            when (color) {
                Color.RED -> red = !red
                Color.GREEN -> green = !green
                Color.BLUE -> blue = !blue
                else -> {}
            }
            return getRgbNumber()
        }

        fun getRgbNumber(): Int = defineSharedState(red, green, blue)

        fun getState(): List<Boolean> = listOf(red, green, blue)
    }
}

enum class Color(val index: Int) {
    RED(0),
    GREEN(1),
    BLUE(2),
    UNKNOWN(99);

    companion object {
        fun getByIndex(index: Int) = values().find { it.index == index } ?: UNKNOWN
    }
}
