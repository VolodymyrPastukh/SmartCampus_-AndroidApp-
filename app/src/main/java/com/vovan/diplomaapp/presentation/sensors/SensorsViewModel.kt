package com.vovan.diplomaapp.presentation.sensors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vovan.diplomaapp.data.NetworkCampusRepository
import com.vovan.diplomaapp.di.Injector
import com.vovan.diplomaapp.domain.entity.ConnectionState
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SensorsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NetworkCampusRepository =
        Injector.provideRepository(application.applicationContext)
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

    companion object{
        const val TOPIC_SUB = "esp32/pub"
    }

}