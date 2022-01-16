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
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
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
    private val _particleAnimationIteration = MutableStateFlow(1L)
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

    /**
     * GameState Object
     */
    var gameState by mutableStateOf(GameState())

    /**
     * Loader vars when the Game Starts
     */
    var loader by mutableStateOf<Drawable?>(null)
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

    /**
     * OpenWeather API Keys
     */
    val openWeatherAPIKEY = arrayOf(
        "18b075743cb869bcc0fe9f4977f3696e",
        "89256d338d3202fa44b7deac9b0c208a",
        "1abd7b50c169e42776138698accfefc8",
        "a04c01f715bebacc9295dcf9f22acf12",
        "3867fa45c8569be5ce88e0337c5beba5",
        "7b01cceea663181163db89f3af7e84eb"
    )

    /**
     * Position Objects
     */
    var position by mutableStateOf(
        GeoPosition(
            latitude = 47.4809967,
            longitude = 8.2115859,
            altitude = 522.0
        )
    )

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

    /**
     * ButtonStates
     */

    var luxButton by mutableStateOf(0.0f)
    var loveButton by mutableStateOf(0.0f)
    var fertilizerButton by mutableStateOf(0.0f)
    var waterButton by mutableStateOf(0.0f)


    init {
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
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
        Log.d(TAG, "Current GameState: ${gameState.toJSON()}")
        checkLux()
        checkLove()
        checkFertilizer()
        checkWater()
        updateButton()
        firebaseConnector.updateGameState(gameState)
    }


    /**
     * Update button heights
     */
    private fun updateButton() {
        luxButton = calcButtonHeight(gameState.playerState.lux)
        loveButton = calcButtonHeight(gameState.playerState.love)
        fertilizerButton = calcButtonHeight(gameState.playerState.fertilizer)
        waterButton = calcButtonHeight(gameState.playerState.water)
    }


    private fun checkLux() {
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

    private fun checkLove() {
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

    private fun checkFertilizer() {
        if (gameState.playerState.fertilizer > 0.0) {
            gameState.playerState.fertilizer -= FERTILIZER_DECAY
        } else {
            gameState.playerState.fertilizer = 0.0
        }
    }

    // Todo When Raining then ++
    private fun checkWater() {
        if (gameState.playerState.water > 0.0) {
            gameState.playerState.water -= WATER_DECAY
        } else {
            gameState.playerState.water = 0.0
        }
    }


    /**
     * Data Loop to get Position and Weather Data on a regular Basis
     */
    private fun dataLoop() {
        gpsConnector.getLocation(
            onSuccess = {

                Log.d(TAG, "position data: ${it}")
                position = it
                gameState.playerState.lastPosition = it
            },
            onFailure = {
                position = GeoPosition(
                    latitude = 47.48124530209937,
                    longitude = 8.211087703634524,
                    altitude = 522.0
                )
            },
            onPermissionDenied = {
                val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(activity, permissions, 10)
            }
        )

        //Todo check on real device
        // FHNW
        position = GeoPosition(latitude = 47.4809967, longitude = 8.2115859, altitude = 522.0)

        // GoldenGate Bridge
        //position = GeoPosition(latitude = 37.81913002995137, longitude = -122.47874183489822, altitude = 0.0)

        loadWeatherData(position.latitude, position.longitude)

        Log.d(
            TAG,
            "Data Looped runned, current weather: ${cWeather.hourWeather.state}, Day: ${dark}"
        )
    }

    /**
     * Just some Helper Method to deal with the OpenWeather API Keys
     */
    private fun getAPIKey(): String {
        return openWeatherAPIKEY[(0 until (openWeatherAPIKEY.size)).random()]
    }

    /**
     * Load Current Weather Data from Openweathermap for a specific position
     */
    private fun loadWeatherData(latitude: Double, longitude: Double) {

        val urlString =
            "https://api.openweathermap.org/data/2.5/onecall?lat=$latitude&lon=$longitude&appid=${getAPIKey()}&units=metric"

        Log.d(TAG, urlString)

        modelScope.launch {

            val url = URL(urlString)
            val weatherJSON = apiConnector.getJSONString(url)
            val weatherJSONObject = JSONObject(weatherJSON)
            val currentWeather = weatherJSONObject.getJSONObject("current")
            val currentWeatherWeather =
                JSONObject(currentWeather.getJSONArray("weather")[0].toString())

            val calendar = Calendar.getInstance()
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

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
                state = getWeatherState(currentWeatherWeather.getLong("id").toInt()),


                )
            cWeather = CurrentWeather(
                time = calendar.time,
                hourWeather = weatherFacts,
                sunrise = formatUnix(currentWeather.getString("sunrise")),
                sunset = formatUnix(currentWeather.getString("sunset")),
                minTemperature = 0,//dailyWeatherTemp.getInt("min"),
                maxTemperature = 0,//dailyWeatherTemp.getInt("max")
            )

            calculateDayOrNight(
                currentWeather.getString("sunrise"),
                currentWeather.getString("sunset")
            )

        }
    }

    fun setWeatherState(state: WeatherState) {
        cWeather.hourWeather.state = state
    }

    fun setActualWeather() {
        dataLoop()
    }

    private fun getWeatherState(weatherId: Int): WeatherState {

        return when (weatherId) {
            200, 201, 202, 210, 211, 212, 221, 230, 231, 232, 781 -> WeatherState.THUNDERSTORM
            300, 301, 302, 310, 311, 312, 313, 314, 321, 500 -> WeatherState.LIGHT_RAIN
            501, 511, 520, 521, 522, 531 -> WeatherState.RAIN
            502, 503, 504 -> WeatherState.HEAVY_RAIN
            600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> WeatherState.SNOW
            701, 711, 721, 731, 741, 751, 761, 762, 771 -> WeatherState.FOG
            801 -> WeatherState.FEW_CLOUDS
            802, 803 -> WeatherState.SCATTERED_CLOUDS
            804 -> WeatherState.MOSTLY_CLOUDY
            else -> WeatherState.CLEAR_SKY
        }


    }

    @SuppressLint("SimpleDateFormat")
    private fun formatUnix(unix: String): String {
        val sdf = SimpleDateFormat("HH:mm")
        val t = Date(unix.toLong() * 1000)
        return sdf.format(t).toString()
    }

    /**
     * Check if it is Night or Day
     */
    private fun calculateDayOrNight(sunriseTimestamp: String, sunsetTimestamp: String) {
        val sunrise = Instant
            .ofEpochSecond(sunriseTimestamp.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val sunset = Instant
            .ofEpochSecond(sunsetTimestamp.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val currentDateTime = ZonedDateTime.now().toLocalDateTime()
        lastCheck = currentDateTime.toString()
        dark = !(currentDateTime > sunrise && currentDateTime < sunset)
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
                        gameState.playerId = it.value.playerId
                        gameState.playerState.lux = it.value.playerState.lux
                        gameState.playerState.love = it.value.playerState.love
                        gameState.playerState.fertilizer = it.value.playerState.fertilizer
                        gameState.playerState.water = it.value.playerState.water
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

                    Log.d(
                        TAG,
                        "******************************** START *****************************"
                    )
                    Log.d(TAG, "Created new GameState: ${it.value.playerId}")
                    Log.d(
                        TAG,
                        "******************************** END ******************************"
                    )
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

    fun calcButtonHeight(percent: Double): Float {
        // 100 percent - actuel percent * height of bubble + delta for correct position
        return (((100 - percent) * 1.1) + 55).toFloat()

    }
}