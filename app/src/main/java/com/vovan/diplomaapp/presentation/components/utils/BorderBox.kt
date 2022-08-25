package com.vovan.diplomaapp.presentation.components.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.vovan.diplomaapp.presentation.components.Colors

@Composable
fun BorderBox(modifier: Modifier, content: @Composable () -> Unit){
    Box(modifier = modifier.border(2.dp, Colors.black)) {
        content()
    }
}