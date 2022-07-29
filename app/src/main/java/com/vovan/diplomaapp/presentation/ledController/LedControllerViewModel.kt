package com.vovan.diplomaapp.presentation.ledController

import androidx.lifecycle.*
import androidx.work.*
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.data.sharedPreference.SharedPreferenceDataSource
import com.vovan.diplomaapp.defineSharedState
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import com.vovan.diplomaapp.presentation.workers.PublishWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LedControllerViewModel @Inject constructor(
    private val repository: MqttRepository,
    private val sharedPreferences: SharedPreferenceDataSource,
    private val workManager: WorkManager
) : ViewModel() {

    val connectionState = repository.connection.map { it.toSensorConnectionState() }.asLiveData()
    private val _dataState = MutableLiveData(SensorDataState(Rgb.getState()))
    val dataState: LiveData<SensorDataState<List<Boolean>>>
        get() = _dataState

    override fun onCleared() {
        prepareWork()
        super.onCleared()
    }

    private fun publish(ledEntity: LedControllerEntity) {
        viewModelScope.launch {
            val completedSuccess = repository.publish(TOPIC_PUB, ledEntity)
            if (completedSuccess) _dataState.value = SensorDataState(Rgb.getState())
        }
    }


    fun clickOnLed(color: String) {
        when (connectionState.value) {
            SensorsConnectionState.Connected -> {
                val led = LedControllerEntity("Feel the pain!!!", Rgb.setColor(color))
                    .also { it.buzzer = if (it.rgb == 7) 1 else 0 }

                publish(led)
            }
            else -> saveState(color)
        }
    }

    private fun saveState(color: String) {
        if (connectionState.value !is SensorsConnectionState.Disconnected) return

        Timber.i("${connectionState.value} -> save to sp")
        viewModelScope.launch {
            sharedPreferences.putValueTo("led", Rgb.setColor(color))
            _dataState.value = SensorDataState(Rgb.getState())
        }
    }

    fun prepareWork() {
        if (connectionState.value !is SensorsConnectionState.Disconnected) return

        Timber.i("${connectionState.value} -> start work")

        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<PublishWorker>()
            .setConstraints(constraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()


        workManager.enqueue(request)
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