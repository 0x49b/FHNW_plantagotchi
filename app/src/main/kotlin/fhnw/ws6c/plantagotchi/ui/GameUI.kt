package fhnw.ws6c.plantagotchi.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.annotation.ExperimentalCoilApi
import com.skydoves.landscapist.glide.GlideImage
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.data.weather.WeatherState
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
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consumeAllChanges()
                                addLove(dragAmount)
                            }
                        }
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
                            .clickable { setWeatherState(WeatherState.RAIN) }
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
                                .clickable { setWeatherState(WeatherState.THUNDERSTORM) }
                        )

                        Text(
                            text = "$coins",
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
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomIndicator(model: PlantagotchiModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
    ) {

        Text(
            text = "What does your plant need?",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )

        Row(modifier = Modifier
            .fillMaxWidth()) {
            IndicatorBubble(model)
        }
    }
}

@Composable
fun IndicatorBubble(model: PlantagotchiModel) {
    with(model) {
        Box(
            contentAlignment = Alignment.Center,
        ) {

            StateBubble(
                value = loveButton,
                color = Color(0xFFD77BBA),
                img = R.drawable.heart
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 20.dp)
        ) {

            StateBubble(
                value = fertilizerButton,
                color = Color(0xFFEEC39A),
                img = R.drawable.fertilizer
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 20.dp)
        ) {

            StateBubble(
                value = waterButton,
                color = Color(0xFF5FCDE4),
                img = R.drawable.water
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 20.dp)
        ) {

            StateBubble(
                value = waterButton,
                color = Color(0xFFFFEB57),
                img = R.drawable.sunshine
            )
        }
    }
}


@Composable
fun StateBubble(value: Float, color: Color, img: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
    ) {

        ConstraintLayout {

            val (cvs, icn) = createRefs()

            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .constrainAs(cvs) {
                        top.linkTo(parent.top, 0.dp)
                        start.linkTo(parent.start, 0.dp)
                        end.linkTo(parent.end, 0.dp)
                        bottom.linkTo(parent.bottom, 0.dp)
                    }
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawCircle(
                    color = color,
                    center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                    radius = size.minDimension / 4
                )
                drawRect(
                    color = Color(0xFF3F282F),
                    size = Size(width = 45.dp.toPx(), height = value - 50.0f),
                    topLeft = Offset(x=50.0f, y=50.0f)
                    //blendMode = BlendMode.DstOut
                )
            }

            Image(
                painter = painterResource(id = img),
                contentDescription = "${img}_indicator",
                modifier = Modifier
                    .size(25.dp)
                    .constrainAs(icn) {
                        top.linkTo(cvs.top, 27.dp)
                        start.linkTo(cvs.start, 27.dp)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_button_bg),
                contentDescription = "${img}_bg_button",
                modifier = Modifier
                    .size(120.dp)
                    .padding((17.5).dp)
            )
        }
    }
}
