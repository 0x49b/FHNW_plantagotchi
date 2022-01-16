package fhnw.ws6c.plantagotchi.data

data class GeoPosition(
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val altitude: Double = 0.0
) {

    fun toJSON(): String {
        return "{  \"longitude\":\"${longitude}\",  \"latitude\":\"${latitude}\", \"altitude\":\"${altitude}\"}"
    }

}