package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.rotclub.area.lib.GlobalRoutes
import org.rotclub.area.composes.CardColumn
import org.rotclub.area.composes.FrispyInput
import org.rotclub.area.composes.InputType
import org.rotclub.area.composes.TitleHeader
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.ui.theme.AreaTheme
import org.rotclub.area.ui.theme.FrispyTheme

private var headerSpacing = 0.dp

@Composable
fun RegisterCard(modifier: Modifier = Modifier, navController: NavHostController) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }

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
        TextButton(
            onClick = {
                println("Login button clicked")
            },
            shape = RoundedCornerShape(roundedValue),
            modifier = Modifier
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FrispyTheme.Primary400,
            )
        ) {
            Text(
                text = "Sign Up",
                color = FrispyTheme.Primary50,
                fontSize = 20.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(horizontal = 35.dp, vertical = 5.dp),
            )
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
        modifier = modifier.fillMaxSize().background(FrispyTheme.Surface700),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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