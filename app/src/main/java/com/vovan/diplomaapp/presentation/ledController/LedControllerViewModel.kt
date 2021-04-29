package com.vovan.diplomaapp.presentation.ledController

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus
import com.google.gson.Gson
import com.vovan.diplomaapp.data.api.MqttManager
import com.vovan.diplomaapp.di.Injector
import com.vovan.diplomaapp.entity.LedControllerEntity
import timber.log.Timber

class LedControllerViewModel(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()
    private var mqttManager: MqttManager = Injector.getMqttManager(application.applicationContext)

    private val _state = MutableLiveData<LedControllerViewState>()
    val state: LiveData<LedControllerViewState>
        get() = _state

    private var callbackConnect = AWSIotMqttClientStatusCallback { status, _ ->
        Timber.d("Status = $status")
        when (status) {
            AWSIotMqttClientStatus.Connecting -> _state.postValue(LedControllerViewState.Connecting)
            AWSIotMqttClientStatus.Connected -> _state.postValue(LedControllerViewState.Connected)
            AWSIotMqttClientStatus.ConnectionLost -> _state.postValue(LedControllerViewState.Error("Lost Connection"))
            AWSIotMqttClientStatus.Reconnecting -> _state.postValue(LedControllerViewState.Connecting)
            else -> Timber.e("ELSE ERROR")
        }
    }

    init {
        mqttManager.connect(callbackConnect)
    }

    fun turnOnLed(color: String){
        val led = LedControllerEntity(
            "Vova",
            Rgb.setColor(color)
        )

        val message = gson.toJson(led)
        mqttManager.publish("esp32/sub", message)
        _state.postValue(LedControllerViewState.Data(Rgb.getState()))
    }


    /*
        Object which stores state of RGB led
            and includes functions to managing the state
     */
    private object Rgb{
        private var red = false
        private var green = false
        private var blue = false

        fun setColor(color: String): Int{
            when(color){
                RED_LED -> red = !red
                GREEN_LED -> green = !green
                BLUE_LED -> blue = !blue
            }
            return getRgbNumber()
        }

        private fun getRgbNumber(): Int{
            if(red && green && blue) return 7 //All
            if(!(red || green || blue)) return 0 //None
            if(red && green) return 4 //RG
            if(red && blue) return 5 //RB
            if(blue && green) return 6 //GB
            if(red) return 1 //R
            if(green) return 2 //G
            if(blue) return 3 //B
            else return 0
        }

        fun getState(): List<Boolean> = listOf(red, green, blue)

    }

    companion object{
        const val RED_LED = "red"
        const val GREEN_LED = "green"
        const val BLUE_LED = "blue"
    }
}