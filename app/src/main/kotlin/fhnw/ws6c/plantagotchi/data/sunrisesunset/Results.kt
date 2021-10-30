package fhnw.ws6c.plantagotchi.data.sunrisesunset

data class Results(

    val sunrise: String,
    val sunset: String,
    val solar_noon: String,
    val day_length: Int,
    val civil_twilight_begin: String,
    val civil_twilight_end: String,
    val nautical_twilight_begin: String,
    val nautical_twilight_end: String,
    val astronomical_twilight_begin: String,
    val astronomical_twilight_end: String
)