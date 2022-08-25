package com.vovan.diplomaapp.presentation.components.custom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.text.TextRegular

@Composable
fun SensorInfoCard(
    sensor: String,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    value: Any? = null,
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .padding(16.dp)
        ) {
            val (tTitle, rContent) = createRefs()

            TextRegular(
                text = sensor,
                modifier = Modifier.constrainAs(tTitle) {
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                    top.linkTo(parent.top)
                },
                color = Colors.black
            )

            Row(
                modifier = Modifier.constrainAs(rContent) {
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                    top.linkTo(tTitle.bottom)
                    bottom.linkTo(parent.bottom)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.weight(0.5f)
                    )
                }

                TextRegular(
                    text = "${value ?: " - "}",
                    color = Colors.black,
                    textSize = 25.sp,
                    modifier = Modifier.weight(1.0f)
                )

            }

        }

    }
}

@Preview
@Composable
private fun SensorCardPreview() {
    return SensorInfoCard(sensor = "Compose", icon = R.drawable.temperature, value = "22.3")
}