package org.rotclub.area

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rotclub.area.ui.theme.FrispyTheme

val roundedValue = 20.dp
val headerSpacing = 10.dp
val fontFamily: FontFamily = FontFamily(
    Font(R.font.nunito, FontWeight.Normal),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.nunito_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.nunito_light_italic, FontWeight.Light, FontStyle.Italic),
)

@Composable
fun LoginInput(value: String, onValueChange: ((String) -> Unit) = {},
               label: String = "", modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value) }
    var thisOnValueChange = onValueChange
    if (onValueChange == {}) {
        thisOnValueChange = { text = it }
    }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedLabelColor = FrispyTheme.Surface400,
            unfocusedBorderColor = FrispyTheme.Surface400,
            focusedBorderColor = FrispyTheme.Primary500,
            focusedLabelColor = FrispyTheme.Primary500,
            cursorColor = FrispyTheme.Primary300,
            focusedTextColor = FrispyTheme.Primary300,
            unfocusedTextColor = FrispyTheme.Surface200
        )
    )
}

@Composable
fun TitleHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.frispyicon),
            colorFilter = ColorFilter.tint(FrispyTheme.Primary500),
            contentDescription = "Frispy Logo",
            modifier = Modifier
                .padding(bottom = 5.dp)
                .size(100.dp)
        )
        Text(
            text = "Frispy",
            fontSize = 70.sp,
            color = FrispyTheme.Primary300,
            fontFamily = fontFamily,
            modifier = Modifier,
        )
    }
}

@Composable
fun LoginCard(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .padding(top = headerSpacing, bottom = headerSpacing + 45.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(roundedValue))
            .background(FrispyTheme.Surface500)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In",
            fontSize = 40.sp,
            color = FrispyTheme.Primary300,
            fontFamily = fontFamily,
            modifier = Modifier.padding(10.dp),
        )
        LoginInput(
            value = "",
            label = "Username",
            modifier = Modifier
        )
        LoginInput(
            value = "",
            label = "Password",
            modifier = Modifier
        )
        TextButton(
            onClick = { },
            shape = RoundedCornerShape(roundedValue),
            modifier = Modifier
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FrispyTheme.Primary400,
            )
        ) {
            Text(
                text = "Login",
                color = FrispyTheme.Primary50,
                fontSize = 20.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(horizontal = 35.dp, vertical = 5.dp),
            )
        }
    }
}

@Composable
fun LoginPage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().background(FrispyTheme.Surface700),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleHeader(modifier = Modifier.padding(headerSpacing))
        LoginCard(modifier = Modifier)
    }
}