package com.vovan.diplomaapp.presentation.components.custom

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vovan.diplomaapp.domain.entity.SensorsEntity
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.text.TextRegular
import com.vovan.diplomaapp.presentation.components.utils.BorderBox
import com.vovan.diplomaapp.presentation.components.utils.ElevatedBox

@Composable
fun SensorInfoDatabaseCard(data: SensorsEntity) {

    ElevatedBox(
        modifier = Modifier
            .fillMaxSize(),
        elevation = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextRegular(
                text = data.time,
                textSize = 25.sp,
                color = Colors.black,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
            )

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
            ) {
                with(data) {
                    listOf<Any>(temperature, light, pressure)
                        .forEach { value ->
                            BorderBox(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                TextRegular(
                                    text = "$value",
                                    textSize = 20.sp,
                                    color = Colors.black,
                                    modifier = Modifier
                                        .padding(vertical = 6.dp)
                                        .fillMaxSize()
                                )
                            }
                        }
                }
            }
        }
    }
}
