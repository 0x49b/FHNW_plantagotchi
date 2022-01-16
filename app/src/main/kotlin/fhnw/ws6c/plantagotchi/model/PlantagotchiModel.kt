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
import androidx.compose.ui.geometry.Offset
import androidx.core.app.ActivityCompat
import com.shopify.promises.Promise
import com.shopify.promises.onResolve
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
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import java.time.*
import java.util.*
import kotlin.concurrent.fixedRateTimer


@SuppressLint("UseCompatLoadingForDrawables")
class PlantagotchiModel(val activity: ComponentActivity) : AppCompatActivity(),
        SensorEventListener {

    private val TAG = "PlantaGotchiModel"
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var sensorManager: SensorManager =
            activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var brightness: Sensor? = null
    private var accelerometer: Sensor? = null


    private var gameState by mutableStateOf(GameState())
    var gpsConnector = GPSConnector(activity)
    var apiConnector = ApiConnector()
    var firebaseConnector = FirebaseConnector(AppPreferences)

    private var openWeatherAPIKEY = arrayOf(
            "18b075743cb869bcc0fe9f4977f3696e",
            "89256d338d3202fa44b7deac9b0c208a",
            "1abd7b50c169e42776138698accfefc8",
            "a04c01f715bebacc9295dcf9f22acf12",
            "3867fa45c8569be5ce88e0337c5beba5",
            "7b01cceea663181163db89f3af7e84eb"
    )

    var loader by mutableStateOf<Drawable?>(null)
    var loading by mutableStateOf(true)
    var loaderText by mutableStateOf("Loading Plantagotchi")


    /**
     * Decays for LUX, CO2, WATER, FERTILIZER
     * LUX_DECAY = 100 percent / 86_400 seconds --> starts to die after one day without Lux
     * LOVE_DECAY = 100 percent / 604_800 seconds --> starts to die after one week without love
     * FERTILIZER_DECAY = 100 percent / 1_209_600 seconds --> starts to die after two week without fertilizer
     * WATER_DECAY = 100 percent / 259_200 seconds --> starts to die after three days without water
     */

    private val LUX_DECAY = 100.0f / 86400.0f
    private val LOVE_DECAY = 100.0f / (86400.0f * 7.0f)
    private val FERTILIZER_DECAY = 100.0f / (86400.0f * 7.0f * 2.0f)
    private val WATER_DECAY = 100.0f / (86400.0f * 2.0f)


    var positionData by mutableStateOf("Getting position ...")
    var position by mutableStateOf(GeoPosition())

    var currentWeather by mutableStateOf("Getting current weather ...")

    var nightDay by mutableStateOf("Checking Night or Day ...")
    var dark by mutableStateOf(false)
    var lastCheck by mutableStateOf("Never checked by now. Wait for next tick")
    var sensorLux by mutableStateOf(0.0f)
    var accelerometerData by mutableStateOf("getting xyz")

    var gameLux by mutableStateOf(0.0)

    var cWeather by mutableStateOf(CurrentWeather.getDefault())


    init {
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            sensorLux = event.values[0]
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = "${event.values[1]}/${event.values[0]}/${event.values[2]}"
        }
    }

    fun gameLoop() {
        Log.d(TAG, "Current GameState: ${gameState.toJSON()}")
        checkLux()
        checkLove()
        checkFertilizer()
        checkWater()
        firebaseConnector.updateGameState(gameState)
    }


    fun checkLux() {
        if (sensorLux > 1000) {
            if (gameState.playerState.lux <= 100.0) {
                gameState.playerState.lux += 0.1
            } else {
                gameState.playerState.lux = 100.0
            }
        } else {
            if (gameState.playerState.lux > 0.0) {
                gameState.playerState.lux -= LUX_DECAY
            } else {
                gameState.playerState.lux = 0.0
            }
        }
    }

    fun checkLove() {
        if (gameState.playerState.love > 100.0) {
            gameState.playerState.love = 100.0
        } else if (gameState.playerState.love > 0.0) {
            gameState.playerState.love -= LOVE_DECAY
        } else {
            gameState.playerState.love = 0.0
        }
    }

    fun addLove(dragAmount: Offset) {
        if (Math.abs(dragAmount.y) > 25.0f) {
            if (gameState.playerState.love < 100.0f) {
                gameState.playerState.love += 1.0f
            } else {
                gameState.playerState.love = 100.0
            }
        }
    }

    fun checkFertilizer() {
        if (gameState.playerState.fertilizer > 0.0) {
            gameState.playerState.fertilizer -= FERTILIZER_DECAY
        } else {
            gameState.playerState.fertilizer = 0.0
        }
    }

    // Todo When Raining then ++
    fun checkWater() {
        if (gameState.playerState.water > 0.0) {
            gameState.playerState.water -= WATER_DECAY
        } else {
            gameState.playerState.water = 0.0
        }
    }


    fun dataLoop() {
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

    private fun getAPIKey(): String {
        return openWeatherAPIKEY[(0 until (openWeatherAPIKEY.size)).random()]
    }

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
        }
    }


    fun calculateDayOrNight(sunriseTimestamp: Long, sunsetTimestamp: Long) {

        val sunrise = Instant
                .ofEpochSecond(sunriseTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        val sunset =
                Instant
                        .ofEpochSecond(sunsetTimestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()

        val currentDateTime = ZonedDateTime.now().toLocalDateTime()

        lastCheck = currentDateTime.toString()

        if (currentDateTime > sunrise && currentDateTime < sunset) {
            nightDay = "We are in daylight"
            dark = false
        } else {
            nightDay = "It's nighttime"
            dark = true
        }
    }



    fun appStartup() {
        if (isPlayerSet()) {
            loadGameState()
        } else {
            createNewGameState()
        }
    }


    fun startLoops() {
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

    fun loadGameState() {
        firebaseConnector
                .loadInitialGameState
                .whenComplete {
                    when (it) {
                        is Promise.Result.Success -> {
                            gameState.playerState.lux = it.value.playerState.lux
                            gameState.playerState.love = it.value.playerState.love
                        }
                        is Promise.Result.Error -> it.error.message?.let { it1 -> Log.e(TAG, it1) }
                    }

                    startLoops()
                }
    }

    fun createNewGameState() {

        gameState.playerState.love = 0.0
        gameState.playerState.fertilizer = 100.0
        gameState.playerState.lux = 100.0
        gameState.playerState.water = 100.0
        gameState.playerState.lastPosition = position

        firebaseConnector.createGameState.whenComplete { it ->

            when (it) {
                is Promise.Result.Success -> {

                    Log.d(TAG, "******************************** START *****************************")
                    Log.d(TAG, "Created new GameState: ${it.value.playerId}")
                    Log.d(TAG, "******************************** END ******************************")
                    AppPreferences.player_id = it.value.playerId
                    gameState.playerId = it.value.playerId

                    startLoops()
                }
            }
        }
    }


    private fun isPlayerSet(): Boolean {
        val playerIsSet = AppPreferences.contains("PLAYER_ID") &&
                AppPreferences.player_id.isNotBlank() &&
                AppPreferences.player_id.isNotEmpty()
        Log.d(TAG, "playerIsSet: $playerIsSet")
        return playerIsSet
    }
}