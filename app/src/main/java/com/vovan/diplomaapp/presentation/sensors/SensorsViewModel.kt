package com.vovan.diplomaapp.presentation.sensors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vovan.diplomaapp.data.api.AwsConnectionState
import com.vovan.diplomaapp.data.api.MqttManager
import com.vovan.diplomaapp.di.Injector
import com.vovan.diplomaapp.entity.SensorsEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SensorsViewModel(application: Application) : AndroidViewModel(application) {

    private var mqttManager: MqttManager = Injector.getMqttManager(application.applicationContext)

    private val _state = MutableLiveData<SensorsViewState>()
    val state: LiveData<SensorsViewState>
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
    private fun connect(){
        val dispose = mqttManager.connect()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { next -> defineConnectionState(next) }
    }

    /*
        Function subscribes on data from IoT devices on topic
     */
    private fun subscribe(){
        val dispose = mqttManager.subscribe<SensorsEntity>("esp32/pub")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data -> showData(data) }
    }

    /*
        Function defines AWS Connection state
     */
    private fun defineConnectionState(connectionState: AwsConnectionState){
        when(connectionState){
            AwsConnectionState.Connecting -> _state.value = SensorsViewState.Connecting
            AwsConnectionState.Connected ->{
                _state.value = SensorsViewState.Connected
                subscribe()
            }
            AwsConnectionState.Disconnect -> _state.value = SensorsViewState.Error("Disconnect")
        }
    }

    private fun showData(data: SensorsEntity){
        _state.value = SensorsViewState.Data(data)
    }


}