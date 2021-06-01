package com.vovan.diplomaapp.presentation.sensorsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vovan.diplomaapp.domain.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SensorsDataListViewModel @Inject constructor(
    private val api: SensorsRepository
) : ViewModel() {

    private var disposable: Disposable? = null

    private var _state = MutableLiveData<SensorsDataListViewState>()
    val state: LiveData<SensorsDataListViewState>
        get() = _state

    init {
        _state.value = SensorsDataListViewState.Loading
        fetchData()
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun updateData() {
        fetchData()
    }

    private fun fetchData() {
        disposable = api.getSensors()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listSensors -> _state.value = SensorsDataListViewState.Data(listSensors) },
                { error ->
                    _state.value =
                        SensorsDataListViewState.Error(error.message ?: "Unknown Error (null)")
                }
            )
    }
}