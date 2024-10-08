package org.rotclub.area

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
import org.rotclub.area.composes.CardColumn
import org.rotclub.area.composes.LoginInput
import org.rotclub.area.composes.TitleHeader
import org.rotclub.area.ui.theme.AreaTheme
import org.rotclub.area.ui.theme.FrispyTheme

private val headerSpacing = 10.dp

@Composable
fun LoginCard(modifier: Modifier = Modifier, navController: NavHostController) {
    CardColumn(modifier = modifier, spacing = headerSpacing)
    {
        Text(
            text = "Sign In",
            fontSize = 40.sp,
            color = FrispyTheme.Primary50,
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
            onClick = {
                println("Login button clicked")
                navController.navigate(GlobalRoutes.MainApp.route)
            },
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
        modifier = modifier.fillMaxSize().background(FrispyTheme.Surface700),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleHeader(modifier = modifier.padding(headerSpacing))
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