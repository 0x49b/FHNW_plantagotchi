package fhnw.ws6c.plantagotchi.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R

private val Pixelmix = FontFamily(
    Font(R.font.pixelmix, FontWeight.Medium)
)

private val PlayMeGames = FontFamily(
    Font(R.font.playmegames, FontWeight.Medium)
)

val typography = Typography(

    defaultFontFamily = Pixelmix,
    h5 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 29.sp
    ),
    body1 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    body2 = TextStyle(
        fontFamily = PlayMeGames,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
)