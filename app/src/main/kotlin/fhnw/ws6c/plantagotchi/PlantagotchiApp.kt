package fhnw.ws6c.plantagotchi

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.GameUI


object PlantagotchiApp : EmobaApp {


    @SuppressLint("StaticFieldLeak")
    private lateinit var model: PlantagotchiModel

    override fun initialize(activity: ComponentActivity) {
        AppPreferences.init(activity)

        if (!AppPreferences.contains("PLAYER_ID") &&
            AppPreferences.player_id.isBlank() &&
            AppPreferences.player_id.isEmpty()
        ) {
            model = PlantagotchiModel(activity)
            model.createNewGameStateInFirebase()
            //todo then show loader
        } else {
            model = PlantagotchiModel(activity)
            // todo show loader
        }
    }

    @Composable
    override fun CreateUI() {
        with(model) {
            GameUI(model)
            // AboutScreen()
        }
    }
}

