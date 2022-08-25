package com.vovan.diplomaapp.presentation.sensorsList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.presentation.components.custom.SensorInfoDatabaseCard
import com.vovan.diplomaapp.presentation.components.utils.Loading

@Composable
fun DatabaseScreen(
    viewModel: SensorsDataListViewModel
) {
    return Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val sensorsData by viewModel.state.observeAsState()

        when (sensorsData) {
            is SensorsDataListViewState.Data -> SensorsData(data = (sensorsData as SensorsDataListViewState.Data).data)
            else -> Loading()
        }
    }
}

@Composable
private fun SensorsData(data: List<SensorsEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(data.count()) { index ->
            if (index > 0) Spacer(modifier = Modifier.height(4.dp))
            SensorInfoDatabaseCard(data = data[index])
        }
    }
}