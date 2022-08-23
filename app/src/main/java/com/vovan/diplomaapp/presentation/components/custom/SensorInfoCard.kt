package com.vovan.diplomaapp.presentation.components.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.text.TextRegular

@Composable
fun SensorInfoCard(
    sensor: String,
    icon: Int,
    value: String,
    modifier: Modifier = Modifier
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
                modifier = Modifier.constrainAs(rContent){
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                    top.linkTo(tTitle.bottom)
                    bottom.linkTo(parent.bottom)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.weight(0.5f)
                )

                TextRegular(
                    text = value,
                    modifier = Modifier.weight(1f),
                    color = Colors.black
                )
            }

        }

    }
}

@Preview
@Composable
private fun SensorCardPreview() {
    return SensorInfoCard(sensor = "Compose", icon = R.drawable.temperature, "22.3")
}