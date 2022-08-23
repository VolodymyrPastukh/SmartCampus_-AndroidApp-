package com.vovan.diplomaapp.presentation.components.button

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.text.TextRegular

@Composable
fun DarkButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    return Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Colors.accent,
            contentColor = Colors.white,
        ),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 12.dp,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(2.dp),
        modifier = modifier,

    ) {
        TextRegular(text = title)
    }
}

@Preview
@Composable
private fun DarkButtonPreview() {
    return DarkButton(title = "Button", onClick = { /*TODO*/ })
}