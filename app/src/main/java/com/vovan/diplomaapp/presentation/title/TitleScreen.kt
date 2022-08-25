package com.vovan.diplomaapp.presentation.title

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.vovan.diplomaapp.R
import com.vovan.diplomaapp.presentation.Routes
import com.vovan.diplomaapp.presentation.components.Colors
import com.vovan.diplomaapp.presentation.components.button.DarkButton
import com.vovan.diplomaapp.presentation.components.text.TextRegular


@Composable
fun TitleScreen(
    onNavigateToNext: (String) -> Unit,
) {

    return Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            val (iImage, tTitle, cButtons) = createRefs()
            val imagePainter = painterResource(id = R.drawable.smarthome)

            Image(
                painter = imagePainter,
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier.constrainAs(iImage) {
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )

            TextRegular(
                text = stringResource(id = R.string.smart_campus),
                textSize = 35.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Colors.black,
                modifier = Modifier.constrainAs(tTitle) {
                    width = Dimension.fillToConstraints
                    start.linkTo(iImage.start)
                    end.linkTo(iImage.end)
                    top.linkTo(iImage.bottom)
                },
            )

            Column(
                modifier = Modifier.constrainAs(cButtons) {
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                    top.linkTo(tTitle.bottom, margin = 16.dp)
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
            ) {
                DarkButton(
                    title = stringResource(R.string.monitoring),
                    onClick = { onNavigateToNext(Routes.MONITORING)},
                    modifier = Modifier.fillMaxWidth()
                )

                DarkButton(
                    title = stringResource(R.string.management),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                DarkButton(
                    title = stringResource(R.string.database),
                    onClick = { onNavigateToNext(Routes.DATABASE) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}