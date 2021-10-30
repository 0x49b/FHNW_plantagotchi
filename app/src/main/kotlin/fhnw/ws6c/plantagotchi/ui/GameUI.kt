package fhnw.ws6c.plantagotchi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skydoves.landscapist.glide.GlideImage
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.theme.PlantagotchiTheme


@Composable
fun GameUI(model: PlantagotchiModel) {
    with(model) {

        PlantagotchiTheme(darkTheme = dark) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)
            ) {

                val (background, ground, pot, plant, lux, pos, weather, dn, stats, lc, bottomBar, accel) = createRefs()



                GlideImage(
                    imageModel = R.drawable.ic_bg_day,
                    contentDescription = "background",
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



                Text(
                    text = statsTitle,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.constrainAs(stats) {
                        end.linkTo(parent.end, 5.dp)
                        top.linkTo(parent.top, 5.dp)
                    })

                Text(
                    text = positionData,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.constrainAs(pos) {
                        end.linkTo(parent.end, 5.dp)
                        top.linkTo(stats.bottom, 5.dp)
                    }
                )

                Text(
                    text = currentWeather,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.constrainAs(weather) {
                        top.linkTo(pos.bottom, 1.dp)
                        end.linkTo(parent.end, 5.dp)
                    })

                Text(
                    text = nightDay,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.constrainAs(dn) {
                        top.linkTo(weather.bottom, 1.dp)
                        end.linkTo(parent.end, 5.dp)
                    })

                Text(
                    text = "$sensorLux lux",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.constrainAs(lux) {
                        top.linkTo(dn.bottom, 1.dp)
                        end.linkTo(parent.end, 5.dp)
                    })

                Text(
                    text = accelerometerData,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.constrainAs(accel) {
                        top.linkTo(lux.bottom, 1.dp)
                        end.linkTo(parent.end, 5.dp)
                    })

                Text(
                    text = "Last update: $lastCheck",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .constrainAs(lc) {
                            top.linkTo(accel.bottom, 1.dp)
                            start.linkTo(parent.end, 5.dp)
                        })

                Image(painterResource(id = R.drawable.ic_plant2),
                    contentDescription = "the_plant",
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(plant) {
                            bottom.linkTo(pot.top, (-2).dp)
                        })

                Image(painterResource(id = R.drawable.ic_pot12),
                    contentDescription = "the_pot",
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(pot) {
                            bottom.linkTo(ground.top, (-48).dp)
                        })
                BottomIndicator(
                    model = model,
                    modifier = Modifier.constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom, 5.dp)
                    })

            }
        }
    }
}

@Composable
fun BottomIndicator(model: PlantagotchiModel, modifier: Modifier) {
    with(model) {
        Column(modifier = modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp)) {

            Text(
                text = "What does your plant need?",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                IndicatorBubble(model, title = "lux", color = Color(0xFFFFEB3B))
            }
        }
    }
}

@Composable
fun IndicatorBubble(model: PlantagotchiModel, title: String, color: Color) {
    with(model) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(color, shape = CircleShape)
                .size(60.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%.0f".format(gameState.playerState.lux) + "%",
                    textAlign = TextAlign.Center,
                    color = Color(0xFF003036),
                    modifier = Modifier
                        .padding(6.dp)
                        .defaultMinSize(30.dp) //Use a min size for short text.
                )
                Text(
                    text = title,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}