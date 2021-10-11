package fhnw.ws6c.plantagotchi.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel


@Composable
fun AppUI(model: PlantagotchiModel) {
    with(model) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Text(
                    text = title,
                    style = TextStyle(fontSize = 20.sp)
                )

                Text(text = position, style = TextStyle(fontSize = 20.sp))
                Text(text = currentWeather, style = TextStyle(fontSize = 20.sp))

                Button(onClick = { getCurrentWeather() }) {
                    Text("Get position")
                }
            }

        }
    }
}

