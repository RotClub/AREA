package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.rotclub.area.composes.FrispyInput
import org.rotclub.area.lib.baseUrl
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.RetrofitClient
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController) {
    val urlInput = remember { mutableStateOf(baseUrl.value) }
    val actionStatus = remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxSize()
            .background(FrispyTheme.Surface700)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 50.sp,
            color = FrispyTheme.TextColor,
            fontFamily = fontFamily,
            modifier = modifier.padding(20.dp),
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(roundedValue))
                .background(FrispyTheme.Surface500)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FrispyInput(
                label = "API URL",
                value = baseUrl,
                modifier = Modifier.padding(vertical = 10.dp),
                focusColor = (if (actionStatus.value) FrispyTheme.Primary500 else FrispyTheme.Error500),
                onValueChange = { urlInput.value = it },
            )
            TextButton(
                modifier = Modifier
                    .padding(20.dp)
                    .size(100.dp, 50.dp)
                    .clip(RoundedCornerShape(roundedValue))
                    .background(FrispyTheme.Primary400),
                onClick = {
                    actionStatus.value = RetrofitClient.changeBaseUrl(urlInput.value)
                    println("Action Status: ${actionStatus.value}")
                },
            ) {
                Text(
                    "Save",
                    color = FrispyTheme.TextColor
                )
            }
        }
    }
    TextButton(
        onClick = {
            navController.popBackStack()
        },
        modifier = Modifier
            .padding(10.dp)
            .padding(top = 20.dp)
            .size(100.dp, 50.dp)
            .clip(RoundedCornerShape(roundedValue))
            .background(FrispyTheme.Surface500),
    ) {
        Text(
            "<- Back",
            color = FrispyTheme.TextColor
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}
