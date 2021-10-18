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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.theme.PlantagotchiTheme


@Composable
fun AppUI(model: PlantagotchiModel) {
    with(model) {

        PlantagotchiTheme {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF0EE4FF))
            ) {

                val (pot, plant, co2, lux, love, pos, weather, dn, stats, lc, luxMeter) = createRefs()

                Text(
                    text = statsTitle,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.constrainAs(stats) {
                        end.linkTo(parent.end, 5.dp)
                        top.linkTo(parent.top, 5.dp)
                    })

                Text(
                    text = position,
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
                    text = "Last update: $lastCheck",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .constrainAs(lc) {
                            top.linkTo(lux.bottom, 1.dp)
                            end.linkTo(parent.end, 5.dp)
                        })



                Image(painterResource(id = R.drawable.ic_plant2),
                    contentDescription = "the_plant",
                    modifier = Modifier
                        .constrainAs(plant) {
                            bottom.linkTo(pot.top, (-2).dp)
                        })

                Image(painterResource(id = R.drawable.ic_pot12),
                    contentDescription = "the_pot",
                    modifier = Modifier
                        .constrainAs(pot) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        })



                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .constrainAs(luxMeter) {
                            bottom.linkTo(parent.bottom, 5.dp)
                            end.linkTo(parent.end, 5.dp)
                        }
                        .background(Color(0xFFFFEB3B), shape = CircleShape)
                        .size(90.dp)
                        /*.layout() { measurable, constraints ->
                            // Measure the composable
                            val placeable = measurable.measure(constraints)

                            //get the current max dimension to assign width=height
                            val currentHeight = placeable.height
                            var heightCircle = currentHeight
                            if (placeable.width > heightCircle)
                                heightCircle = placeable.width

                            //assign the dimension and the center position
                            layout(heightCircle, heightCircle) {
                                // Where the composable gets placed
                                placeable.placeRelative(0, (heightCircle - currentHeight) / 2)
                            }
                        }*/) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){

                        Text(
                            text = "%.0f".format(gameLux) + "%",
                            textAlign = TextAlign.Center,
                            color = Color(0xFF003036),
                            modifier = Modifier
                                .padding(6.dp)
                                .defaultMinSize(30.dp) //Use a min size for short text.
                        )
                        Text(
                            text = "lux",
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
    }
}