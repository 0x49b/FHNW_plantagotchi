package fhnw.ws6c.plantagotchi.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StateBubble(color: Color) {

    Box {

        /*Image(
            painter = painterResource(id = R.drawable.ic_button_bg),
            contentdescription = "bg_bubble"
        )*/

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
                size = Size(width = canvasWidth, height = canvasHeight / 2),
                blendMode = BlendMode.DstOut

            )
        }

    }


}


@SuppressLint("InvalidColorHexValue")
@Preview
@Composable
fun BubblePreview() {

    StateBubble(color = Color(0xfffFFEB57))

}