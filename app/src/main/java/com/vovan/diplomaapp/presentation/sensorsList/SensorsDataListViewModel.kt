package com.vovan.diplomaapp.presentation.sensorsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovan.diplomaapp.TODAY_L
import com.vovan.diplomaapp.domain.SensorsRepository
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorsDataListViewModel @Inject constructor(
    private val api: SensorsRepository
) : ViewModel() {

    private var _state = MutableLiveData<SensorsDataListViewState>()
    val state: LiveData<SensorsDataListViewState>
        get() = _state

    init {
        _state.value = SensorsDataListViewState.Loading
        fetchData(TODAY_L)
    }


    fun updateData(tableName: String) {
        fetchData(tableName)
    }

    private fun fetchData(tableName: String) {
        viewModelScope.launch {
            val sensors = api.getSensors(tableName)
            _state.postValue(SensorsDataListViewState.Data(sensors))
        }
    }
}