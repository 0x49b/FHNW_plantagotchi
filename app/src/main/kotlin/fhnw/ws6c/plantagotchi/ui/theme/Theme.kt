package fhnw.ws6c.plantagotchi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import fhnw.ws6c.plantagotchi.ui.theme.color.UnicornColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color


private val appLightColors = Colors(
    //Background colors
    primary = UnicornColor.LIGHT_SKY,
    primaryVariant = UnicornColor.CORNFLOWER_BLUE,

    secondary = UnicornColor.AMETHYST_SMOKE,
    secondaryVariant = UnicornColor.MOBSTER,

    background = UnicornColor.LIGHT_CYAN,
    surface = UnicornColor.PALE_TURQUOISE,
    error = UnicornColor.BROWN,

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
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = appLightColors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}