package fhnw.ws6c.plantagotchi.model

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
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
import com.shopify.promises.Promise
import fhnw.ws6c.R
import fhnw.ws6c.plantagotchi.AppPreferences
import fhnw.ws6c.plantagotchi.data.GeoPosition
import fhnw.ws6c.plantagotchi.data.connectors.ApiConnector
import fhnw.ws6c.plantagotchi.data.connectors.FirebaseConnector
import fhnw.ws6c.plantagotchi.data.connectors.GPSConnector
import fhnw.ws6c.plantagotchi.data.state.GameState
import fhnw.ws6c.plantagotchi.data.weather.CurrentWeather
import fhnw.ws6c.plantagotchi.data.weather.WeatherFacts
import fhnw.ws6c.plantagotchi.data.weather.WeatherState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.concurrent.fixedRateTimer


@SuppressLint("UseCompatLoadingForDrawables")
class PlantagotchiModel(val activity: ComponentActivity) : AppCompatActivity(),
    SensorEventListener {


    /**
     * Weather Things clean up
     */

    var oldSelectedWeatherTime: Date = Date()
    private val _particleAnimationIteration = MutableStateFlow(0L)
    val particleAnimationIteration: StateFlow<Long> = _particleAnimationIteration



    /**
     * Generic Stuff
     */
    private val TAG = "PlantaGotchiModel"
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Sensor Stuff
     */
    private var sensorManager: SensorManager =
        activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var brightness: Sensor? = null
    private var accelerometer: Sensor? = null


    /**
     * Init of Connectors
     */
    var gpsConnector = GPSConnector(activity)
    var apiConnector = ApiConnector()
    var firebaseConnector = FirebaseConnector(AppPreferences)
    var statsTitle = "PlantaGotchi Stats"

    /**
     * GameState Object
     */
    var gameState by mutableStateOf(GameState())

    /**
     * Loader vars when the Game Starts
     */
    var loader by mutableStateOf<Drawable?>(null)
    var loading by mutableStateOf(true)
    var loaderText by mutableStateOf("Loading Plantagotchi")


    /**
     * Decays for LUX, CO2, WATER, FERTILIZER
     */
    val LUX_DECAY = 100.0f / 86400.0f // 100 percent / 867400 seconds

    /**
     * Position Objects
     */
    var positionData by mutableStateOf("Getting position ...")
    var position by mutableStateOf(GeoPosition())

    /**
     * Weather Objects
     */
    var cWeather by mutableStateOf(CurrentWeather.getDefault())
    var currentWeather by mutableStateOf("Getting current weather ...")


    /**
     * Currently used in the Frontend, can be removed afterwards (before MVP)
     */
    var nightDay by mutableStateOf("Checking Night or Day ...")
    var dark by mutableStateOf(false)
    var lastCheck by mutableStateOf("Never checked by now. Wait for next tick")
    var sensorLux by mutableStateOf(0.0f)
    var accelerometerData by mutableStateOf("getting xyz")
    var gameLux by mutableStateOf(0.0)


    init {
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)

        // Todo Change this
        dataLoop()
    }


    /**
     * Sensor Section, Just to get the newest Sensor Values
     */
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            sensorLux = event.values[0]
        }
    }

    /**
     * The GameLoop to calculate all the Data for the Plantagotchi Experience
     */
    private fun gameLoop() {
        checkLux()
        firebaseConnector.updateGameState(gameState)
    }


    /**
     * Calculate Lux Decay or Increase based on current sensor value
     */
    private fun checkLux() {
        if (sensorLux > 1000) {
            if (gameState.playerState.lux <= 100.0) {
                gameState.playerState.lux += 0.1
            } else {
                gameState.playerState.lux = 100.0
            }
        } else {
            gameState.playerState.lux -= LUX_DECAY
        }

        Log.d(TAG, "GameLux: $gameLux")
    }

    /**
     * Loader Animation
     */
    fun setLoader(percent: Int, message: String, running: Boolean) {
        loading = running
        loaderText = "$message"
        when (percent) {
            0 -> loader = activity.getDrawable(R.drawable.p0)
            10 -> loader = activity.getDrawable(R.drawable.p10)
            20 -> loader = activity.getDrawable(R.drawable.p20)
            30 -> loader = activity.getDrawable(R.drawable.p30)
            40 -> loader = activity.getDrawable(R.drawable.p40)
            50 -> loader = activity.getDrawable(R.drawable.p50)
            60 -> loader = activity.getDrawable(R.drawable.p60)
            70 -> loader = activity.getDrawable(R.drawable.p70)
            80 -> loader = activity.getDrawable(R.drawable.p80)
            90 -> loader = activity.getDrawable(R.drawable.p90)
            100 -> loader = activity.getDrawable(R.drawable.p100)
        }
    }

    /**
     * Data Loop to get POsition and Weather Data on a regular Basis
     */
    private fun dataLoop() {
        gpsConnector.getLocation(
            onSuccess = {
                positionData = "${it.latitude},${it.longitude}"
                position = it
                gameState.playerState.lastPosition = it
            },
            onFailure = { positionData = "Cannot get current position" },
            onPermissionDenied = {
                val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(activity, permissions, 10)
            }
        )
        loadWeatherData(position.latitude, position.longitude)
    }

    /**
     * Just some Helper Method to deal with the OpenWeather API Keys
     */
    private fun getAPIKey(): String {
        val openWeatherAPIKEY = arrayOf(
            "18b075743cb869bcc0fe9f4977f3696e",
            "89256d338d3202fa44b7deac9b0c208a",
            "1abd7b50c169e42776138698accfefc8",
            "a04c01f715bebacc9295dcf9f22acf12",
            "3867fa45c8569be5ce88e0337c5beba5",
            "7b01cceea663181163db89f3af7e84eb"
        )
        return openWeatherAPIKEY[(0 until (openWeatherAPIKEY.size)).random()]
    }

    /**
     * Load Current Weather Data from Openweathermap for a specific position
     */
    private fun loadWeatherData(latitude: Double, longitude: Double) {

        val urlString =
            "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&appid=${getAPIKey()}"

        modelScope.launch {

            val url = URL(urlString)
            val weatherJSON = apiConnector.getJSONString(url)
            val weatherJSONObject = JSONObject(weatherJSON)
            val currentWeather = weatherJSONObject.getJSONObject("current")
            //val minutelyWeather = weatherJSONObject.getJSONArray("minutely")[0]
            val dailyWeather = weatherJSONObject.getJSONArray("daily")[0]
            //val dailyWeatherTemp = dailyWeather.getJSONObject("temp")
            Log.d(TAG, weatherJSON)

            Log.d(TAG, "current temp from new loader ${currentWeather.getLong("temp")}")

            val weatherFacts = WeatherFacts(
                temperature = currentWeather.getLong("temp").toFloat(),
                apparentTemperature = currentWeather.getLong("feels_like").toInt(),
                precipitation = 0.0F,//minutelyWeather.getLong("precipitation").toFloat(),
                humidity = currentWeather.getLong("humidity").toFloat(),
                windSpeed = currentWeather.getLong("wind_speed").toFloat(),
                cloudCover = currentWeather.getLong("clouds").toFloat(),
                pressure = currentWeather.getLong("pressure").toFloat(),
                visibility = currentWeather.getLong("visibility").toFloat(),
                uvIndex = currentWeather.getLong("uvi").toInt(),
                dewPoint = currentWeather.getLong("dew_point").toInt(),
                state = WeatherState.CLEAR_SKY
            )
            cWeather = CurrentWeather(
                time = Calendar.getInstance().time,
                hourWeather = weatherFacts,
                sunrise = currentWeather.getString("sunrise"),
                sunset = currentWeather.getString("sunset"),
                minTemperature = 0,//dailyWeatherTemp.getInt("min"),
                maxTemperature = 0,//dailyWeatherTemp.getInt("max")
            )

            calculateDayOrNight(cWeather.sunrise.toLong(), cWeather.sunset.toLong())

        }
    }

    /**
     * Check if it is Night or Day
     */
    private fun calculateDayOrNight(sunriseTimestamp: Long, sunsetTimestamp: Long) {
        val sunrise = Instant
            .ofEpochSecond(sunriseTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val sunset = Instant
            .ofEpochSecond(sunsetTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val currentDateTime = ZonedDateTime.now().toLocalDateTime()
        lastCheck = currentDateTime.toString()
        dark = !(currentDateTime > sunrise && currentDateTime < sunset)
    }

    fun appStartup() {
        /*fixedRateTimer(
            name = "plantagotchi-data-loop",
            initialDelay = 0,
            period = 60000,
            daemon = true
        ) {
            dataLoop()
        }*/

        if (isPlayerSet()) {
            loadGameState()
        } else {
            createNewGameState()
            //loadGameState()
        }
    }

    fun loadGameState() {
        firebaseConnector
            .loadInitialGameState
            .whenComplete {
                when (it) {
                    is Promise.Result.Success -> {
                        gameState = it.value
                        fixedRateTimer(
                            name = "plantagotchi-game-loop",
                            initialDelay = 0,
                            period = 1000,
                            daemon = true
                        ) {
                            gameLoop()
                        }
                    }
                    is Promise.Result.Error -> it.error.message?.let { it1 -> Log.e(TAG, it1) }
                }
            }
    }

    fun createNewGameState() {
        gameState.playerState.lux = 100.0
        gameState.playerState.love = 100.0
        gameState.playerState.co2 = 100.0
        gameState.playerState.fertilizer = 100.0
        gameState.playerState.lastPosition = position
        firebaseConnector.createNewGameState(gameState)
    }


    private fun isPlayerSet(): Boolean {
        val playerIsSet = AppPreferences.contains("PLAYER_ID") &&
                AppPreferences.player_id.isNotBlank() &&
                AppPreferences.player_id.isNotEmpty()
        Log.d(TAG, "playerIsSet: $playerIsSet")
        return playerIsSet
    }
}