package org.rotclub.area.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.rotclub.area.composes.CardColumn
import org.rotclub.area.composes.FrispyInput
import org.rotclub.area.composes.GlobalRoutes
import org.rotclub.area.composes.InputType
import org.rotclub.area.composes.SettingsButton
import org.rotclub.area.composes.TitleHeader
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.LoginResponse
import org.rotclub.area.lib.httpapi.authLogin
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.ui.theme.AreaTheme
import org.rotclub.area.ui.theme.FrispyTheme

private val headerSpacing = 10.dp

@Composable
fun LoginCard(modifier: Modifier = Modifier, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val sharedPreferences = LocalContext.current.getSharedPreferences("Area", Context.MODE_PRIVATE)

    val loadingState = remember { mutableStateOf(false) }
    val loginResult: MutableState<LoginResponse?> = remember { mutableStateOf(null) }
    val loginErrorStatus = remember { mutableStateOf("") }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    if (sharedPreferences.contains("token")) {
        if (sharedPreferences.getString("token", "")!!.isNotEmpty()) {
            navController.navigate(GlobalRoutes.MainApp.route)
        }
    }

    CardColumn(modifier = modifier, spacing = headerSpacing)
    {
        Text(
            text = "Sign In",
            fontSize = 40.sp,
            color = FrispyTheme.TextColor,
            fontFamily = fontFamily,
            modifier = Modifier.padding(10.dp),
        )
        FrispyInput(
            value = email,
            label = "Email",
            modifier = Modifier
        )
        FrispyInput(
            value = password,
            label = "Password",
            inputType = InputType.PASSWORD,
            modifier = Modifier
        )
        TextButton(
            onClick = {
                if (loadingState.value) return@TextButton
                loginErrorStatus.value = ""
                coroutineScope.launch {
                    authLogin(email.value, password.value,
                        loadingState, loginResult, loginErrorStatus)
                    println("Login result: ${loginResult.value}")
                    if (loginResult.value != null) {
                        val token = loginResult.value?.token
                        if (token == null) {
                            loginResult.value = null
                            loginErrorStatus.value = "An error occurred"
                            return@launch
                        }
                        sharedPreferences.edit().putString("token", token).apply()
                        println("Login successful. Token: $token")
                        navController.navigate(GlobalRoutes.MainApp.route)
                    }
                }
            },
            shape = RoundedCornerShape(roundedValue),
            modifier = Modifier
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FrispyTheme.Primary400,
            )
        ) {
            if (loadingState.value) {
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
                text = "Login",
                color = FrispyTheme.TextColor,
                fontSize = 20.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(horizontal = 35.dp, vertical = 5.dp),
            )
        }
        if (loginErrorStatus.value.isNotEmpty()) {
            val width = 250.dp
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .width(width)
                    .background(FrispyTheme.Error500, RoundedCornerShape(roundedValue))
            ) {
                Text(
                    text = loginErrorStatus.value,
                    color = FrispyTheme.TextColor,
                    fontSize = 15.sp,
                    fontFamily = fontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(width).padding(5.dp)
                )
            }
        }
        val annotatedString = buildAnnotatedString {
            withStyle(style = TextStyle(color = FrispyTheme.Surface300).toSpanStyle()) {
                append("Don't have an account? ")
            }
            pushStringAnnotation(tag = "none", annotation = "")
            withStyle(style = TextStyle(color = FrispyTheme.Primary500).toSpanStyle()) {
                append("Make one here")
            }
            pop()
        }
        Text(
            text = annotatedString,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    println("Register button clicked")
                    navController.navigate(GlobalRoutes.Register.route)
                }
        )
    }
}

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsButton(Modifier.align(Alignment.End), navController)
        TitleHeader(modifier = modifier, iconSize = 90.dp, titleSize = 60.sp)
        LoginCard(modifier = modifier, navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    AreaTheme {
        LoginPage(navController = rememberNavController())
    }
}