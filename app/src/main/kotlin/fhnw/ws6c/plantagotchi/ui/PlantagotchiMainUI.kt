package fhnw.ws6c.plantagotchi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import androidx.compose.material.Scaffold
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import fhnw.ws6c.R


@Composable
fun AppUI(model: PlantagotchiModel) {
    with(model) {


        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF0EE4FF))
        ) {


            val (pot, plant, co2, lux, love, pos, weather, dn, stats, lc) = createRefs()

            Text(
                text = title,
                style = TextStyle(fontSize = 35.sp),
                modifier = Modifier.constrainAs(stats) {
                    end.linkTo(parent.end, 5.dp)
                    top.linkTo(parent.top, 5.dp)
                })

            Text(
                text = position,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.constrainAs(pos) {
                    end.linkTo(parent.end, 5.dp)
                    top.linkTo(stats.bottom, 5.dp)
                }
            )

            Text(
                text = currentWeather,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.constrainAs(weather) {
                    top.linkTo(pos.bottom, 1.dp)
                    end.linkTo(parent.end, 5.dp)
                })

            Text(
                text = nightDay,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.constrainAs(dn) {
                    top.linkTo(weather.bottom, 1.dp)
                    end.linkTo(parent.end, 5.dp)
                })

            Text(
                text = "$currentLux lux",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.constrainAs(lux) {
                    top.linkTo(dn.bottom, 1.dp)
                    end.linkTo(parent.end, 5.dp)
                })

            Text(
                text = "Last update: $lastCheck",
                modifier = Modifier
                    .constrainAs(lc) {
                        top.linkTo(lux.bottom, 1.dp)
                        end.linkTo(parent.end, 5.dp)
                    })



            Image(painterResource(id = R.drawable.ic_plant1),
                contentDescription = "the_plant",
                modifier = Modifier
                    .constrainAs(plant) {
                        bottom.linkTo(pot.top, (-2).dp)
                    })

            Image(painterResource(id = R.drawable.ic_pot11),
                contentDescription = "the_pot",
                modifier = Modifier
                    .constrainAs(pot) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    })
        }
    }
}
