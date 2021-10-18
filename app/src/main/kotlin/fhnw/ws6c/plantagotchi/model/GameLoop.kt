package fhnw.ws6c.plantagotchi.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

class GameLoop {

    val lux by mutableStateOf(0.0f)
    val co2 by mutableStateOf(0.0f)
    val h2o by mutableStateOf(0.0f)
    val fertilizer by mutableStateOf(0.0f)

}