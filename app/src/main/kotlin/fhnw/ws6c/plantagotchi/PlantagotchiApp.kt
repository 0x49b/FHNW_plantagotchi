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
        model = PlantagotchiModel(activity)
        //model.appStartup()
    }

    @Composable
    override fun CreateUI() {
        GameUI(model)
    }
}

