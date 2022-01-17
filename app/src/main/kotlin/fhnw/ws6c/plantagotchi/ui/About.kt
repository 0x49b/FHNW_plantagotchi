package fhnw.ws6c.plantagotchi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.skydoves.landscapist.glide.GlideImage
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.ui.theme.PlantagotchiTheme

@Composable
fun AboutScreen() {

    PlantagotchiTheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (background, ground, teamrocket, title, by, tr) = createRefs()

            GlideImage(
                imageModel = R.drawable.ic_bg_about,
                contentDescription = "Background",
                modifier = Modifier.constrainAs(background) {
                    start.linkTo(parent.start, 0.dp)
                    top.linkTo(parent.top, 0.dp)
                }
            )

            GlideImage(
                imageModel = R.drawable.ic_ground,
                contentDescription = "ground",
                modifier = Modifier
                    .height(128.dp)
                    .constrainAs(ground) {
                        bottom.linkTo(parent.bottom, 0.dp)
                        start.linkTo(parent.start, 0.dp)
                    }
            )

            Image(
                painter = rememberImagePainter(R.drawable.ic_teamrocket),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .constrainAs(teamrocket) {
                        bottom.linkTo(ground.top)
                        start.linkTo(parent.start, 40.dp)
                    }
            )

            Text(
                text = "Planta\nGotchi",
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                color = Color(0xFFEDEDED),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        top.linkTo(parent.top, 60.dp)
                    })

            Text(text = "by",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                color = Color(0xFFEDEDED),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(by) {
                        top.linkTo(title.bottom, 25.dp)
                    })

            Text(text = "Team\nRocket",
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                color = Color(0xFFEDEDED),
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(tr) {
                        top.linkTo(by.bottom, 25.dp)
                    })
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}