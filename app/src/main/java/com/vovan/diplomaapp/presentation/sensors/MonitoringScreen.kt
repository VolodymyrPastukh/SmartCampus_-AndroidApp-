package com.vovan.diplomaapp.presentation.sensors

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.components.custom.SensorInfoCard
import com.vovan.diplomaapp.presentation.components.utils.Loading
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState.Connected

@Composable
fun MonitoringScreen(
    viewModel: SensorsViewModel,
    onBackPressed: () -> Unit,
) {
    return Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val connectionState by viewModel.connectionState.observeAsState()

        when (connectionState) {
            Connected -> Connected(viewModel = viewModel)
            else -> Loading()
        }
    }
}

@Composable
private fun Connected(viewModel: SensorsViewModel) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val sensorState by viewModel.dataState.observeAsState()


        SensorInfoCard(
            sensor = "Time",
            value = sensorState?.data?.time,
            modifier = Modifier.weight(1f)
        )

        SensorInfoCard(
            sensor = "Temperature",
            icon = R.drawable.temperature,
            value = sensorState?.data?.temperature,
            modifier = Modifier.weight(1f)
        )

        SensorInfoCard(
            sensor = "Light",
            icon = R.drawable.light,
            value = sensorState?.data?.light,
            modifier = Modifier.weight(1f)
        )

        SensorInfoCard(
            sensor = "Pressure",
            icon = R.drawable.pressure,
            value = sensorState?.data?.pressure,
            modifier = Modifier.weight(1f)
        )
    }
}