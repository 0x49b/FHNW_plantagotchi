package fhnw.ws6c.plantagotchi.data

import java.util.*

/**
 * From emoba Module, but changed to our needs
 */
data class GeoPosition(
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val altitude: Double = 0.0
) {

    override fun toString(): String {
        return "$latitude,$longitude"
    }

    private fun format(latitude: Double, longitude: Double): String {
        val latCompassDirection = if (latitude > 0.0) "N" else "S"
        val lonCompassDirection = if (longitude > 0.0) "E" else "W"

        return "${getDMS(latitude)} $latCompassDirection, ${getDMS(longitude)} $lonCompassDirection"
    }

    private fun getDMS(value: Double): String {
        val absValue = Math.abs(value)
        val degree = absValue.toInt()
        val minutes = ((absValue - degree) * 60.0).toInt()
        val seconds = (absValue - degree - minutes / 60.0) * 3600.0

        return "${degree}° ${minutes}′ ${String.format(Locale.ENGLISH, "%.4f", seconds)}″";
    }

}