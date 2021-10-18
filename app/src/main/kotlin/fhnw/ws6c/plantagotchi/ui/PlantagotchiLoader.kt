package fhnw.ws6c.plantagotchi.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

@Composable
fun LoadingScreen(model: PlantagotchiModel) {
    with(model) {

        Box {

            Image(
                painterResource(id = R.drawable.ic_loaderbg),
                contentDescription = "loader-background",
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds,
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Hello Loader",
                    style = TextStyle(fontSize = 35.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )

                CircularProgressIndicator()

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