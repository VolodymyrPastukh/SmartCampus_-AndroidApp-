package com.vovan.diplomaapp.presentation.components.utils

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ElementDivider(
    size: Int = 16,
    color: Color = Color(0xFF000000)
) {
    Spacer(
        modifier = Modifier
            .height((size / 2).dp)
            .width(IntrinsicSize.Max)
    )
    Divider(color = color, modifier = Modifier.height((size / 4).dp))
    Spacer(
        modifier = Modifier
            .height((size / 2).dp)
            .width(IntrinsicSize.Max)
    )
}