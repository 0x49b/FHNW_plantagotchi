package fhnw.ws6c.plantagotchi.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R

private val Pixelmix = FontFamily(
    Font(R.font.pixelmix, FontWeight.Medium),
    Font(R.font.pixelmix_bold, FontWeight.Bold)
)

private val PlayMeGames = FontFamily(
    Font(R.font.playmegames, FontWeight.Medium)
)

val typography = Typography(

    defaultFontFamily = Pixelmix,

    h1 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 57.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 64.sp
    ),

    h2 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 45.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 52.sp
    ),
    h3 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 44.sp
    ),
    h4 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 40.sp
    ),

    h5 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 36.sp
    ),
    h6 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 32.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    body1 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp
    ),
    body2 = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    caption = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    button = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    overline = TextStyle(
        fontFamily = Pixelmix,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp
    ),
)