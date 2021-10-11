package fhnw.ws6c.plantagotchi

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel
import fhnw.ws6c.plantagotchi.ui.AppUI


object PlantagotchiApp : EmobaApp {


    override fun initialize(activity: ComponentActivity) {

    }

    @Composable
    override fun CreateUI() {
        AppUI(PlantagotchiModel)
    }

}

