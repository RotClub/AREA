package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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

data class ActionCardData(val title: String, val text: String)

@Composable
fun NodeScreen(navController: NavHostController) {
    val actionCards = remember { mutableStateOf(listOf(
        ActionCardData("First Action Card", "This is the first action card"),
    )) }

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
            actionCards.value.forEach {
                ActionCard(navController = navController)
            }
            PlusButton {
                actionCards.value += ActionCardData("New Action Card", "This is a new action card")
            }
        }
    }
}

@Composable
fun ActionScreen(navController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .padding(25.dp, 60.dp, 20.dp, 0.dp),
    ) {
        BackButton(navController = navController)
    }
}