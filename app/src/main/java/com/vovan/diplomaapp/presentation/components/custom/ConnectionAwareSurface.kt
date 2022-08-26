package com.vovan.diplomaapp.presentation.components.custom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.vovan.diplomaapp.presentation.base.BaseViewModel
import com.vovan.diplomaapp.presentation.components.utils.Loading
import com.vovan.diplomaapp.presentation.model.SensorsConnectionState

@Composable
fun <V: BaseViewModel> ConnectionAwareSurface(viewModel: V, compose: @Composable (V) -> Unit) {
    return Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val connectionState by viewModel.connectionState.observeAsState()

        when (connectionState) {
            SensorsConnectionState.Connected -> compose(viewModel)
            else -> Loading()
        }
    }
}