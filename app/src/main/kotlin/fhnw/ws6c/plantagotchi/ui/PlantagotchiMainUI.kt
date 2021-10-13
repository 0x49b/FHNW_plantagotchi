package fhnw.ws6c.plantagotchi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color


@Composable
fun AppUI(model: PlantagotchiModel) {
    with(model) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().background(color = Color(0xFF0EE4FF))
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text(text = title,style = TextStyle(fontSize = 35.sp))
                Text(text = position, style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = currentWeather, style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = nightDay, style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "$currentLux lux", style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = "Last update: ${lastCheck}")
            }
        }
    }
}

