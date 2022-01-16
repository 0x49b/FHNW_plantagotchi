package fhnw.ws6c.plantagotchi.data.state

import fhnw.ws6c.plantagotchi.data.GeoPosition

class PlayerState {
    var lux: Double = 0.0
    var water: Double = 0.0
    var love: Double = 0.0
    var fertilizer: Double = 0.0
    var lastPosition: GeoPosition = GeoPosition()

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "lux" to lux,
            "water" to water,
            "love" to love,
            "fertilizer" to fertilizer,
            "lastPosition" to lastPosition
        )
    }

    fun toJSON(): String {
        return "{\"water\":\"${water}\", \"fertilizer\":\"${fertilizer}\", \"love\":\"${love}\", \"lux\":\"${lux}\", \"lastPosition\":\"${lastPosition.toJSON()}\",}"
    }
}