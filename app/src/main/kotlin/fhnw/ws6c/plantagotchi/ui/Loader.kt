package fhnw.ws6c.plantagotchi.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel

@Composable
fun LoadingScreen(model: PlantagotchiModel) {
    with(model) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = loaderText,
                    style = TextStyle(fontSize = 35.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Image(
                    painter = rememberImagePainter(loader),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    //circularReveal = CircularReveal(duration = 250)
                )
            }
        }
    }
}


@Preview
@Composable
fun LoadingScreenPreview() {
    val context = LocalContext.current as Activity
    LoadingScreen(model = PlantagotchiModel(context as ComponentActivity))
}