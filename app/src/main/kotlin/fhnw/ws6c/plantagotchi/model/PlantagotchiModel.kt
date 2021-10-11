package fhnw.ws6c.plantagotchi.model

import android.Manifest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.beust.klaxon.Klaxon
import fhnw.ws6c.plantagotchi.data.connectors.ApiConnector
import fhnw.ws6c.plantagotchi.data.connectors.GPSConnector
import fhnw.ws6c.plantagotchi.data.sunrisesunset.SunriseSunset
import fhnw.ws6c.plantagotchi.data.weather.WeatherBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.URL


class PlantagotchiModel(val activity: ComponentActivity) {

    private var TAG = "PlantagotchiModel"
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    var title = "Hello ws6C"
    var gpsConnector = GPSConnector(activity)
    var apiConnector = ApiConnector()

    var openWeatherAPIKEY = "4a95a98df24aeeb48956f2c2f3db0502"


    var position by mutableStateOf("")
    var currentWeather by mutableStateOf("")


    fun getCurrentWeather() {
        gpsConnector.getLocation(
            onSuccess = {
                position = "${it.latitude},${it.longitude}"

                modelScope.launch {
                    val url =
                        URL("https://api.openweathermap.org/data/2.5/weather?lat=${it.latitude}&lon=${it.longitude}&appid=${openWeatherAPIKEY}")
                    val weatherJSON = apiConnector.getJSONString(url)
                    Log.i(TAG, weatherJSON)

                    val weather = Klaxon().parse<WeatherBase>(weatherJSON)
                }

                modelScope.launch {
                    val url =
                        URL("https://api.sunrise-sunset.org/json?lat=${it.latitude}&lng=-${it.longitude}&formatted=0\n")
                    val sunriseSunsetJSON = apiConnector.getJSONString(url)
                    Log.i(TAG, sunriseSunsetJSON)

                    val sunriseSunset = Klaxon().parse<SunriseSunset>(sunriseSunsetJSON)
                    Log.i(TAG, sunriseSunset.toString())
                }


            },
            onFailure = { position = "Cannot get current location" },
            onPermissionDenied = {
                val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(activity, permissions, 10)
            }
        )
    }

}