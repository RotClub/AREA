package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rotclub.area.R
import org.rotclub.area.composes.ColumnCard
import org.rotclub.area.composes.BackButton
import org.rotclub.area.composes.PlusButton
import org.rotclub.area.composes.ActionCard
import org.rotclub.area.ui.theme.FrispyTheme

data class ColumnCardData(val title: String, val text: String)

@Composable
fun WorkspaceScreen(navController: NavHostController) {
    val columnCards = remember { mutableStateOf(listOf(
        ColumnCardData("First Column Card", "This is the first column card"),
        ColumnCardData("Second Column Card", "This is the second column card")
    )) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .padding(20.dp, 80.dp, 20.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        columnCards.value.forEach { cardData ->
            ColumnCard(navController = navController, title = cardData.title, text = cardData.text)
        }
        PlusButton {
            columnCards.value += ColumnCardData("New Column Card", "This is a new column card")
        }
    }
}

@Composable
fun ActionScreen(navController: NavHostController) {

    val columnCards = remember { mutableIntStateOf(0) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .padding(25.dp, 60.dp, 20.dp, 0.dp),
    ) {
        BackButton(navController = navController)
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 0 until columnCards.intValue) {
                ActionCard(navController = navController)
            }
            PlusButton {
                columnCards.intValue++
            }
        }
    }
}

@Composable
fun ServiceScreen(navController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .padding(25.dp, 60.dp, 20.dp, 0.dp),
    ) {
        BackButton(navController = navController)
    }
}