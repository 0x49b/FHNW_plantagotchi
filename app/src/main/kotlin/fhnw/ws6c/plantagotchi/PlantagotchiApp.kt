package fhnw.ws6c.plantagotchi

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.plantagotchi.data.connectors.AppPreferences
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.AppUI
import fhnw.ws6c.plantagotchi.ui.LoadingScreen


object PlantagotchiApp : EmobaApp {


    @SuppressLint("StaticFieldLeak")
    private lateinit var model: PlantagotchiModel

    override fun initialize(activity: ComponentActivity) {
        AppPreferences.init(activity)
        model = PlantagotchiModel(activity)
    }

    @Composable
    override fun CreateUI() {
        LoadingScreen(model)
        //AppUI(model)
    }
}

