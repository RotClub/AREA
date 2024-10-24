package org.rotclub.area

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.rotclub.area.composes.GlobalRoutes
import org.rotclub.area.composes.Navigation
import org.rotclub.area.screens.MainScreen
import org.rotclub.area.ui.theme.AreaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AreaTheme {
                Navigation(GlobalRoutes.Login)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AreaTheme {
        MainScreen()
    }
}