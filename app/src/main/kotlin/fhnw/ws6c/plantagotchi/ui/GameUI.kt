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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import com.skydoves.landscapist.glide.GlideImage
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.theme.PlantagotchiTheme
import fhnw.ws6c.plantagotchi.ui.weatherui.DynamicWeatherSection


@OptIn(ExperimentalCoilApi::class)
@Composable
fun GameUI(model: PlantagotchiModel) {
    with(model) {
            PlantagotchiTheme(darkTheme = dark) {

                Box(modifier = Modifier.fillMaxSize()) {

                    DynamicWeatherSection(currentWeather = cWeather, viewModel = model)

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        val (ground, pot, plant, chest, coin_counter, bottomBar) = createRefs()


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
                            painter = painterResource(id = R.drawable.chest),
                            contentDescription = "chest",
                            modifier = Modifier
                                .padding(top = 35.dp, end = 8.dp)
                                .size(40.dp)

                                .constrainAs(chest) {
                                    end.linkTo(parent.end, 5.dp)
                                    top.linkTo(parent.top, 5.dp)
                                }
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier

                                .constrainAs(coin_counter) {
                                    top.linkTo(parent.top, 0.dp)
                                }
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.coint_counter),
                                contentDescription = "coin_counter",
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .size(120.dp)
                            )

                            Text(
                                text = "9999",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(start = 25.dp)

                            )
                        }

                        Image(painterResource(id = R.drawable.ic_plant1),
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
}

@Composable
fun BottomIndicator(model: PlantagotchiModel, modifier: Modifier) {
    with(model) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        ) {

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
                .padding(start = 30.dp)

        ) {

            Image(
                painterResource(id = R.drawable.button_love),
                contentDescription = "button_love",
                modifier = Modifier
                    .size(65.dp)
            )

            Image(
                painterResource(id = R.drawable.heart),
                contentDescription = "heart",
                modifier = Modifier
                    .size(25.dp)
            )

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 20.dp)
        ) {

            Image(
                painterResource(id = R.drawable.button_fertilizer),
                contentDescription = "button_fertilizer",
                modifier = Modifier
                    .size(65.dp)
            )

            Image(
                painterResource(id = R.drawable.fertilizer),
                contentDescription = "fertilizer",
                modifier = Modifier
                    .size(25.dp)
            )

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 20.dp)
        ) {

            Image(
                painterResource(id = R.drawable.button_water),
                contentDescription = "button_water",
                modifier = Modifier
                    .size(65.dp)
            )

            Image(
                painterResource(id = R.drawable.water),
                contentDescription = "water",
                modifier = Modifier
                    .size(25.dp)
            )

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 20.dp)
        ) {

            Image(
                painterResource(id = R.drawable.button_sunshine),
                contentDescription = "button_sunshine",
                modifier = Modifier
                    .size(65.dp)
            )

            Image(
                painterResource(id = R.drawable.sunshine),
                contentDescription = "sunshine",
                modifier = Modifier
                    .size(25.dp)
            )

        }
    }
}