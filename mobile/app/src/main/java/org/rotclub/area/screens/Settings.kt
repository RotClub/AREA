package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.rotclub.area.composes.FrispyInput
import org.rotclub.area.lib.baseUrl
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.RetrofitClient
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    val coroutineScope = rememberCoroutineScope()
    val urlInput = remember { mutableStateOf(baseUrl.value) }
    val actionStatus = remember { mutableStateOf(true) }
    val saveLoading = remember { mutableStateOf(false) }

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
                error = !actionStatus.value,
                onValueChange = { urlInput.value = it },
            )
            TextButton(
                onClick = {
                    if (saveLoading.value) return@TextButton
                    saveLoading.value = true
                    actionStatus.value = true
                    coroutineScope.launch {
                        actionStatus.value = RetrofitClient.changeBaseUrl(urlInput.value)
                        println("Action Status: ${actionStatus.value}")
                        saveLoading.value = false
                    }
                },
                shape = RoundedCornerShape(roundedValue),
                modifier = Modifier
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FrispyTheme.Primary400,
                )
            ) {
                if (saveLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .padding(top = 3.dp)
                            .width(37.dp),
                        color = FrispyTheme.Primary300,
                        trackColor = FrispyTheme.Surface600
                    )
                    return@TextButton
                }
                Text(
                    text = "Save",
                    color = FrispyTheme.TextColor,
                    fontSize = 20.sp,
                    fontFamily = fontFamily,
                    modifier = Modifier.padding(horizontal = 35.dp, vertical = 5.dp),
                )
            }
        }
    }
    if (navController != null) {
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
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}
