package com.vovan.diplomaapp.presentation.ledController

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.custom.ConnectionAwareSurface
import com.vovan.diplomaapp.presentation.components.text.TextRegular
import com.vovan.diplomaapp.presentation.components.utils.ElevatedBox

@Composable
fun ManagementScreen(
    viewModel: LedControllerViewModel
) {
    return ConnectionAwareSurface(
        viewModel
    ) {

        val dataState = viewModel.dataState.observeAsState()

        dataState.value?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                TextRegular(
                    text = "MANAGEMENT",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    color = Colors.black
                )

                it.data.forEachIndexed { index, value ->
                    val color = Color.getByIndex(index)
                    Spacer(modifier = Modifier.height(20.dp))
                    ElevatedBox(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .weight(1f)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(
                                    bounded = true,
                                    color = defineRippleColor(index, value),
                                ),
                                onClick = { viewModel.clickOnLed(color) }
                            ),
                        shape = RectangleShape,
                    ) {
                        Image(
                            painter = painterResource(defineImageResource(index, value)),
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    }
}

private fun defineImageResource(led: Int, state: Boolean) =
    if (!state) R.drawable.led
    else when (led) {
        0 -> R.drawable.redled
        1 -> R.drawable.greenled
        2 -> R.drawable.blueled
        else -> R.drawable.led
    }

private fun defineRippleColor(led: Int, state: Boolean) =
    if (state) Colors.white
    else when (led) {
        0 -> Colors.red
        1 -> Colors.green
        2 -> Colors.blue
        else -> Colors.white
    }