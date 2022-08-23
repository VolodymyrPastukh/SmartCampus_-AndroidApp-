package com.vovan.diplomaapp.presentation.components.text

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.Fonts

@Composable
fun TextRegular(
    text: String,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Light,
    color: Color = Colors.white,
    fontFamily: FontFamily = Fonts.terminator,
    textAlign: TextAlign = TextAlign.Center,
) {
    return Text(
        text = text,
        fontSize = textSize,
        fontWeight = fontWeight,
        color = color,
        fontFamily = fontFamily,
        textAlign = textAlign,
        modifier = modifier
    )
}

@Preview
@Composable
private fun TextPreview() {
    return com.vovan.diplomaapp.presentation.components.text.TextRegular(text = "Compose", modifier = Modifier.fillMaxSize())
}