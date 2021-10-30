package fhnw.ws6c.plantagotchi.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import fhnw.ws6c.plantagotchi.model.PlantagotchiModel

@Composable
fun ShopScreen(model: PlantagotchiModel) {
    with(model) {
    }
}


@Preview
@Composable
fun ShopScreenPreview() {
    val context = LocalContext.current as Activity
    ShopScreen(model = PlantagotchiModel(context as ComponentActivity))
}