package org.rotclub.area.lib

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.rotclub.area.R

var baseUrl = mutableStateOf("http://10.0.2.2:8081/")
const val BASE_DEEPLINK_URL = "https://area-app.vercel.app/"
val roundedValue = 20.dp
val fontFamily: FontFamily = FontFamily(
    Font(R.font.nunito, FontWeight.Normal),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.nunito_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.nunito_light_italic, FontWeight.Light, FontStyle.Italic),
)
