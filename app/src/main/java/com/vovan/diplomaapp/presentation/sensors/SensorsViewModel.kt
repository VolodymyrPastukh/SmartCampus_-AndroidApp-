package com.vovan.diplomaapp.presentation.sensors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vovan.diplomaapp.domain.MqttRepository
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SensorsViewModel @Inject constructor(
    private val repository: MqttRepository,
) : ViewModel() {

    private var disposable: Disposable? = null
    private val _state = MutableLiveData<SensorsViewState>()
    val state: LiveData<SensorsViewState>
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
            .subscribe(
                { status -> defineConnectionState(status) },
                { throwable -> Timber.e(throwable) }
            )
    }

    /*
        Function subscribes on data from IoT devices on topic
     */
    private fun subscribe() {
        disposable = repository.subscribe(TOPIC_SUB)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> showData(data) },
                { throwable -> Timber.e(throwable) }
            )
    }

    /*
        Function defines AWS Connection state
     */
    private fun defineConnectionState(connectionState: ConnectionState) {
        when (connectionState) {
            ConnectionState.Connecting -> _state.value = SensorsViewState.Connecting
            ConnectionState.Connected -> {
                _state.value = SensorsViewState.Connected
                subscribe()
            }
            ConnectionState.Disconnect -> _state.value = SensorsViewState.Error("Disconnect")
        }
    }

    private fun showData(data: SensorsEntity) {
        _state.value = SensorsViewState.Data(data)
    }

    companion object {
        const val TOPIC_SUB = "esp32/pub"
    }

}