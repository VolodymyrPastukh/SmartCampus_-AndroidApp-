package com.vovan.diplomaapp.presentation.sensors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.components.custom.SensorInfoCard

@Composable
fun MonitoringScreen(
    viewModel: SensorsViewModel,
    onBackPressed: () -> Unit,
) {
    return Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SensorInfoCard(sensor = "Temperature", icon = R.drawable.temperature, value = "25.0", modifier = Modifier.weight(1f))
            SensorInfoCard(sensor = "Light", icon = R.drawable.light, value = "1423", modifier = Modifier.weight(1f))
            SensorInfoCard(sensor = "Pressure", icon = R.drawable.pressure, value = "780.2", modifier = Modifier.weight(1f))
        }
    }
}