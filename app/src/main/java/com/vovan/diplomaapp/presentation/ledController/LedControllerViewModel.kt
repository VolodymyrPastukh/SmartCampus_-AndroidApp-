package com.vovan.diplomaapp.presentation.ledController

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.vovan.diplomaapp.data.NetworkCampusRepository
import com.vovan.diplomaapp.di.Injector
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.LedControllerEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LedControllerViewModel(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()
    private val repository: NetworkCampusRepository =
        Injector.provideRepository(application.applicationContext)

    private var disposable: Disposable? = null

    private val _state = MutableLiveData<LedControllerViewState>()
    val state: LiveData<LedControllerViewState>
        get() = _state

    init {
        connect()
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
        disposable?.dispose()
    }

    /*
        Function makes connection to AWS IoT Core Broker
     */
    private fun connect() {
        disposable = repository.connect()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { next -> defineConnectionState(next) },
                { throwable -> Timber.e(throwable) }
            )
    }

    /*
        Function publishes data (RGB state) to remote devices
     */
    private fun publish(message: String) {
        disposable = repository.publish(TOPIC_PUB, message)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _state.value = LedControllerViewState.Data(Rgb.getState()) },
                { throwable -> Timber.e(throwable) }
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

        //Turn on buzzer if each color is active
        if (led.rgb == 7) led.buzzer = 1
        else led.buzzer = 0

        val message = gson.toJson(led)
        publish(message)
    }


    /*
        Function defines and processes AWS Connection state
     */
    private fun defineConnectionState(connectionState: ConnectionState) {
        when (connectionState) {
            ConnectionState.Connecting -> _state.value = LedControllerViewState.Connecting
            ConnectionState.Connected -> _state.value = LedControllerViewState.Connected
            ConnectionState.Disconnect -> _state.value = LedControllerViewState.Error("Disconnect")
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
        const val TOPIC_PUB = "esp32/sub"

        const val RED_LED = "red"
        const val GREEN_LED = "green"
        const val BLUE_LED = "blue"
    }
}