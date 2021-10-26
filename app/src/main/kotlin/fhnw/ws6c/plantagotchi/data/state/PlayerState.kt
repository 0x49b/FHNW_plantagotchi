package fhnw.ws6c.plantagotchi.data.state

import fhnw.ws6c.plantagotchi.data.GeoPosition

class PlayerState {
    var lux: Double = 0.0
    var co2: Double = 0.0
    var love: Double = 0.0
    var fertilizer: Double = 0.0
    var lastPosition: GeoPosition = GeoPosition()

    fun toHashMap(): HashMap<String, Any> {
        val playerState = hashMapOf(
            "lux" to lux,
            "co2" to co2,
            "love" to love,
            "fertilizer" to fertilizer,
            "lastPosition" to lastPosition
        )

        return playerState
    }
}