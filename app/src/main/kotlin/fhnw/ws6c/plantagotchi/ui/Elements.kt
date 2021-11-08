package fhnw.ws6c.plantagotchi.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skydoves.landscapist.glide.GlideImage
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.theme.PlantagotchiTheme

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