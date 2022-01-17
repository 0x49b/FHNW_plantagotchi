package fhnw.ws6c.plantagotchi.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fhnw.ws6c.plantagotchi.ui.theme.color.PlantagotchiDarkColor
import fhnw.ws6c.plantagotchi.ui.theme.color.PlantagotchiLightColor


private val appNightColors = Colors(
//Background colors
    primary = PlantagotchiDarkColor.SKY,
    primaryVariant = PlantagotchiDarkColor.CORNFLOWER_BLUE,

    secondary = PlantagotchiDarkColor.AMETHYST_SMOKE,
    secondaryVariant = PlantagotchiDarkColor.MOBSTER,

    background = PlantagotchiDarkColor.SKY,
    surface = PlantagotchiDarkColor.PALE_TURQUOISE,
    error = PlantagotchiDarkColor.BROWN,

    //Typography and icon colors
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,

    onError = Color.White,

    isLight = false
)

private val appDayColors = Colors(
    //Background colors
    primary = PlantagotchiLightColor.LIGHT_SKY,
    primaryVariant = PlantagotchiLightColor.CORNFLOWER_BLUE,

    secondary = PlantagotchiLightColor.AMETHYST_SMOKE,
    secondaryVariant = PlantagotchiLightColor.MOBSTER,

    background = PlantagotchiLightColor.SKY,
    surface = PlantagotchiLightColor.PALE_TURQUOISE,
    error = PlantagotchiLightColor.BROWN,

    //Typography and icon colors
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,

    onError = Color.White,

    isLight = true
)


@Composable
fun PlantagotchiTheme(
    darkTheme: Boolean = false,
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = appNightColors,//if(darkTheme) appNightColors.copy() else appDayColors.copy(),
        typography = typography,
        shapes = shapes,
        content = content
    )
}