package fhnw.ws6c.plantagotchi.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R

@Composable
fun StateBubble(color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .padding(start = 20.dp)
    ) {


        Canvas(
            modifier = Modifier
                .size(100.dp)
                .padding(0.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height


            drawCircle(
                color = color,
                center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                radius = size.minDimension / 4
            )

            drawRect(
                color = Color.Red,
                size = Size(width = 100.dp.toPx(), height = (canvasHeight / 2f)),
                blendMode = BlendMode.DstOut
            )
        }

        Image(
            painter = painterResource(id = R.drawable.sunshine),
            contentDescription = "sunshine_indicator",
            modifier = Modifier
                .size(25.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_button_bg),
            contentDescription = "bg_button",
            modifier = Modifier
                .size(100.dp)
                .padding((17.5).dp)
        )
    }
}


@SuppressLint("InvalidColorHexValue")
@Preview
@Composable
fun BubblePreview() {
    StateBubble(color = Color(0xfffFFEB57))
}