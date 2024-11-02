package org.rotclub.area.screens

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
import org.rotclub.area.lib.httpapi.RegisterResponse
import org.rotclub.area.lib.httpapi.authRegister
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.AreaTheme
import org.rotclub.area.ui.theme.FrispyTheme

private var headerSpacing = 0.dp

@Composable
fun RegisterCard(modifier: Modifier = Modifier, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    val loadingState = remember { mutableStateOf(false) }
    val registerResult: MutableState<RegisterResponse?> = remember { mutableStateOf(null) }
    val registerErrorStatus = remember { mutableStateOf("") }

    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    CardColumn(modifier = modifier, spacing = headerSpacing)
    {
        Text(
            text = "Register",
            fontSize = 40.sp,
            color = FrispyTheme.Primary50,
            fontFamily = fontFamily,
            modifier = Modifier.padding(10.dp),
        )
        FrispyInput(
            value = username,
            label = "Username",
            modifier = Modifier
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
        FrispyInput(
            value = confirmPassword,
            label = "Confirm Password",
            inputType = InputType.PASSWORD,
            modifier = Modifier
        )
        TextButton(
            onClick = {
                if (loadingState.value) return@TextButton
                registerErrorStatus.value = ""
                coroutineScope.launch {
                    authRegister(username.value, email.value, password.value,
                        confirmPassword.value, loadingState, registerResult, registerErrorStatus)
                    if (registerErrorStatus.value.isNotEmpty()) {
                        loadingState.value = false
                        return@launch
                    }
                    if (registerResult.value != null) {
                        val token = registerResult.value?.token
                        println("Token: $token")
                        if (token == null) {
                            registerResult.value = null
                            registerErrorStatus.value = "An error occurred"
                            return@launch
                        }
                        sharedStorage.saveToken(token)
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
                text = "Sign Up",
                color = FrispyTheme.Primary50,
                fontSize = 20.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(horizontal = 35.dp, vertical = 5.dp),
            )
        }
        if (registerErrorStatus.value.isNotEmpty()) {
            val width = 250.dp
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .width(width)
                    .background(FrispyTheme.Error500, RoundedCornerShape(roundedValue))
            ) {
                Text(
                    text = registerErrorStatus.value,
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
                append("Already have an account? ")
            }
            pushStringAnnotation(tag = "none", annotation = "")
            withStyle(style = TextStyle(color = FrispyTheme.Primary500).toSpanStyle()) {
                append("Login here")
            }
            pop()
        }
        Text(
            text = annotatedString,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    println("Login button clicked")
                    navController.navigate(GlobalRoutes.Login.route)
                }
        )
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsButton(Modifier.align(Alignment.End), navController)
        TitleHeader(
            modifier = modifier,
            iconSize = 80.dp,
            titleSize = 50.sp
        )
        RegisterCard(modifier = modifier, navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPagePreview() {
    AreaTheme {
        RegisterPage(navController = rememberNavController())
    }
}