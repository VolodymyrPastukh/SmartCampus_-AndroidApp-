package com.vovan.diplomaapp.presentation.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.vovan.diplomaapp.presentation.components.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElevatedBox(
    modifier: Modifier,
    elevation: Int = 8,
    shape: Shape = CardDefaults.elevatedShape,
    composable: @Composable () -> Unit) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = elevation.dp,
        ),
        shape = shape,
    ) {
        Box(
            modifier = Modifier.background(color = Colors.white).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            composable()
        }
    }
}