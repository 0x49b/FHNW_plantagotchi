package fhnw.ws6c.plantagotchi.model

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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
import java.time.ZonedDateTime
import kotlin.concurrent.fixedRateTimer


class PlantagotchiModel(val activity: ComponentActivity) : AppCompatActivity(),
    SensorEventListener {

    private val TAG = "PlantagotchiModel"
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var sensorManager: SensorManager =
        activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var brightness: Sensor? = null


    var statsTitle = "Plantagotchi Stats"
    var gpsConnector = GPSConnector(activity)
    var apiConnector = ApiConnector()

    var openWeatherAPIKEY = "4a95a98df24aeeb48956f2c2f3db0502"


    /**
     * Decays for LUX, CO2, WATER, FERTILIZER
     */
    val LUX_DECAY = 0.0025f
    val WATER_DECAY = 0.1f
    val FERTILIZER_DECAY = 0.1f


    var position by mutableStateOf("Getting position ...")
    var currentWeather by mutableStateOf("Getting current weather ...")
    var nightDay by mutableStateOf("Checking Night or Day ...")
    var lastCheck by mutableStateOf("Never checked by now. Wait for next tick")
    var sensorLux by mutableStateOf(0.0f)

    var gameLux by mutableStateOf(100.0f)

    // Todo: Maybe redesign later
    init {

        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_FASTEST)

        fixedRateTimer(
            name = "plantagotchi-data-loop",
            initialDelay = 0,
            period = 60000,
            daemon = true
        ) {
            dataLoop()
        }

        fixedRateTimer(
            name = "plantagotchi-game-loop",
            initialDelay = 0,
            period = 1000,
            daemon = true
        ) {
            gameLoop()
        }

    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // braucht es aktuell nicht
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            sensorLux = event.values[0]
            Log.d(TAG, "Current lux: $sensorLux")
        }
    }

    fun gameLoop() {

        checkLux()

    }


    fun checkLux() {
        if (sensorLux > 1000) {
            if (gameLux <= 100.0) {
                gameLux += 0.1f
            }else {
                gameLux = 100.0f
            }
        } else {
            gameLux -= LUX_DECAY
        }

        Log.d(TAG, "GameLux: $gameLux")
    }


    fun dataLoop() {
        gpsConnector.getLocation(
            onSuccess = {
                position = "${it.latitude},${it.longitude}"
                loadSunriseSunsetData(it.latitude, it.longitude)
                loadWeatherData(it.latitude, it.longitude)
            },
            onFailure = { position = "Cannot get current position" },
            onPermissionDenied = {
                val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(activity, permissions, 10)
            }
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun loadWeatherData(latitude: Double, longitude: Double) {
        modelScope.launch {
            val url =
                URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$openWeatherAPIKEY")
            val weatherJSON = apiConnector.getJSONString(url)
            Log.d(TAG, weatherJSON)

            try {
                val weather = Klaxon().parse<WeatherBase>(weatherJSON)
                Log.d(TAG, weather.toString())
                if (weather != null) {
                    currentWeather = weather.weather[0].main
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in OpenWeatherCall: $e")
            }

        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun loadSunriseSunsetData(latitude: Double, longitude: Double) {
        modelScope.launch {
            val url =
                URL("https://api.sunrise-sunset.org/json?lat=$latitude&lng=-$longitude&formatted=0")
            val sunriseSunsetJSON = apiConnector.getJSONString(url)
            Log.d(TAG, sunriseSunsetJSON)

            try {

                val sunriseSunset = Klaxon().parse<SunriseSunset>(sunriseSunsetJSON)
                Log.d(TAG, sunriseSunset.toString())

                if (sunriseSunset != null) {

                    val sunrise = ZonedDateTime.parse(sunriseSunset.results.sunrise)
                    val sunset = ZonedDateTime.parse(sunriseSunset.results.sunset)
                    val currentDateTime = ZonedDateTime.now()

                    lastCheck = currentDateTime.toString()

                    if (currentDateTime > sunrise && currentDateTime < sunset) {
                        nightDay = "We are in daylight"
                    } else {
                        nightDay = "It's nighttime"
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in SunriseSunset Call: $e")
            }
        }
    }
}