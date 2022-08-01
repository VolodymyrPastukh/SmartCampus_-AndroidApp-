package com.vovan.diplomaapp.presentation.ledController

import androidx.lifecycle.*
import androidx.work.*
import com.vovan.diplomaapp.OFFLINE_LED_PUBLISH
import com.vovan.diplomaapp.TOPIC_PUB
import com.vovan.diplomaapp.data.sharedPreference.SharedPreferenceDataSource
import com.vovan.diplomaapp.defineSharedState
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import com.vovan.diplomaapp.presentation.model.BackgroundInfoEventState
import com.vovan.diplomaapp.presentation.model.SensorDataState
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState
import com.vovan.diplomaapp.presentation.model.toSensorConnectionState
import com.vovan.diplomaapp.presentation.workers.PublishWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
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

    private val _eventState = MutableSharedFlow<BackgroundInfoEventState>(replay = 1)
    val eventState: LiveData<BackgroundInfoEventState>
        get() = _eventState.asLiveData()

    init {
        processWorkerOutput()
    }

    override fun onCleared() {
        prepareWork()
        super.onCleared()
    }

    private fun processWorkerOutput() {
        viewModelScope.launch {
            val uuid = sharedPreferences.getValue<String>(OFFLINE_LED_PUBLISH)
                .also { if (it.isEmpty()) return@launch }
            val workInfo = workManager.getWorkInfoById(UUID.fromString(uuid)).get()
            val isSuccess = workInfo.outputData.getBoolean("isSuccess", false)
            val publishData = workInfo.outputData.getInt("publishData", 0)
            if (isSuccess)
                _eventState.emit(BackgroundInfoEventState.Success(publishData))
            else
                _eventState.emit(BackgroundInfoEventState.Error)
            sharedPreferences.putValueTo(OFFLINE_LED_PUBLISH, "")
        }
    }

    private fun publish(ledEntity: LedControllerEntity) {
        viewModelScope.launch {
            val completedSuccess = repository.publish(TOPIC_PUB, ledEntity)
            if (completedSuccess) _dataState.value = SensorDataState(Rgb.getState())
        }
    }

    fun clickOnLed(color: String) {
        val led = LedControllerEntity("Online publish request", Rgb.setColor(color))
            .also { it.buzzer = if (it.rgb == 7) 1 else 0 }
        when (connectionState.value) {
            SensorsConnectionState.Connected -> { publish(led) }
            else ->  _dataState.value = SensorDataState(Rgb.getState())
        }
    }

    private fun prepareWork() {
        if (connectionState.value !is SensorsConnectionState.Disconnected) return
        val inputWorkData = workDataOf("led_data" to Rgb.getState())

        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<PublishWorker>()
            .setConstraints(constraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(inputWorkData)
            .build()

        sharedPreferences.putValueTo(OFFLINE_LED_PUBLISH, request.id.toString())
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
