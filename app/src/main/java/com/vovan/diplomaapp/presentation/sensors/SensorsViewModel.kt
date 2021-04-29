package com.vovan.diplomaapp.presentation.sensors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.google.gson.Gson
import com.vovan.diplomaapp.data.api.MqttManager
import com.vovan.diplomaapp.di.Injector
import com.vovan.diplomaapp.entity.SensorsEntity
import timber.log.Timber
import java.io.UnsupportedEncodingException

class SensorsViewModel(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()
    private var mqttManager: MqttManager = Injector.getMqttManager(application.applicationContext)

    private val _state = MutableLiveData<SensorsViewState>()
    val state: LiveData<SensorsViewState>
        get() = _state


    private var callbackConnect = AWSIotMqttClientStatusCallback { status, throwable ->
        Timber.d("Status = $status")
        when (status) {
            AWSIotMqttClientStatus.Connecting -> _state.postValue(SensorsViewState.Connecting)
            AWSIotMqttClientStatus.Connected -> {
                mqttManager.subscribe("esp32/pub", callbackSubscribe)
                _state.postValue(SensorsViewState.Connected)
            }
            AWSIotMqttClientStatus.ConnectionLost -> _state.postValue(SensorsViewState.Error("Lost Connection"))
            AWSIotMqttClientStatus.Reconnecting -> _state.postValue(SensorsViewState.Connecting)
            else -> Timber.e("ELSE ERROR")
        }

    }

    private val callbackSubscribe = AWSIotMqttNewMessageCallback { _, data ->
        try {
            val message = String(data, Charsets.UTF_8)
            val sensors = gson.fromJson(message, SensorsEntity::class.java)
            _state.postValue(SensorsViewState.Data(sensors))

        } catch (e: UnsupportedEncodingException) {
            Timber.e("Message encoding error. $e")
        }
    }

    init {
        mqttManager.connect(callbackConnect)
    }


    override fun onCleared() {
        super.onCleared()
        mqttManager.disconnect()
    }

}