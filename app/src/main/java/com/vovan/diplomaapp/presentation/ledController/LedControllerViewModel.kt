package com.vovan.diplomaapp.presentation.ledController

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.vovan.diplomaapp.data.api.AwsConnectionState
import com.vovan.diplomaapp.data.api.MqttManager
import com.vovan.diplomaapp.di.Injector
import com.vovan.diplomaapp.entity.LedControllerEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LedControllerViewModel(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()
    private var mqttManager: MqttManager = Injector.getMqttManager(application.applicationContext)

    private val _state = MutableLiveData<LedControllerViewState>()
    val state: LiveData<LedControllerViewState>
        get() = _state

    init {
        connect()
    }

    override fun onCleared() {
        super.onCleared()
        mqttManager.disconnect()
    }

    /*
        Function makes connection to AWS IoT Core Broker
     */
    private fun connect() {
        val dispose = mqttManager.connect()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { next -> defineConnectionState(next) }
    }

    /*
        Function publishes data (RGB state) to remote devices
     */
    private fun publish(topic: String, message: String) {
        val dispose = mqttManager.publish(topic, message)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _state.value = LedControllerViewState.Data(Rgb.getState()) },
                { error -> _state.value = LedControllerViewState.Error(error.message ?: "Some Unknown Error")}
            )
    }


    /*
        Function prepares data to publishing
     */
    fun turnOnLed(color: String) {
        val led = LedControllerEntity(
            "Vova",
            Rgb.setColor(color)
        )

        if (led.rgb == 7) led.buzzer = 1
        else led.buzzer = 0

        val message = gson.toJson(led)
        publish("esp32/sub", message)
    }


    /*
        Function defines and processes AWS Connection state
     */
    private fun defineConnectionState(connectionState: AwsConnectionState) {
        when (connectionState) {
            AwsConnectionState.Connecting -> _state.value = LedControllerViewState.Connecting
            AwsConnectionState.Connected -> {
                _state.value = LedControllerViewState.Connected
            }
            AwsConnectionState.Disconnect -> _state.value =
                LedControllerViewState.Error("Disconnect")
        }
    }


    /*
        Object which stores state of RGB led
            and includes functions to managing the state
        I know it isn't the best implementation, but it works, and it is enough at the moment)
     */
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
            if (red && green && blue) return 7 //All
            if (!(red || green || blue)) return 0 //None
            if (red && green) return 4 //RG
            if (red && blue) return 5 //RB
            if (blue && green) return 6 //GB
            if (red) return 1 //R
            if (green) return 2 //G
            if (blue) return 3 //B
            else return 0
        }

        fun getState(): List<Boolean> = listOf(red, green, blue)

    }

    companion object {
        const val RED_LED = "red"
        const val GREEN_LED = "green"
        const val BLUE_LED = "blue"
    }
}