package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.rotclub.area.composes.ColumnCard
import org.rotclub.area.composes.BackButton
import org.rotclub.area.composes.PlusButton
import org.rotclub.area.composes.ActionCard
import org.rotclub.area.composes.ListView
import org.rotclub.area.composes.TerminateButton
import org.rotclub.area.lib.fontFamily
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
            .fillMaxWidth()
            .height(893.dp)
            .background(FrispyTheme.Surface700)
            .verticalScroll(rememberScrollState())
            .padding(20.dp, 80.dp, 20.dp, 20.dp),
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
            .fillMaxWidth()
            .height(893.dp)
            .background(FrispyTheme.Surface700)
            .padding(25.dp, 60.dp, 20.dp, 20.dp),
    ) {
        BackButton(navController = navController)
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
            .fillMaxWidth()
            .height(893.dp)
            .verticalScroll(rememberScrollState())
            .background(FrispyTheme.Surface700)
            .padding(25.dp, 60.dp, 20.dp, 20.dp),
    ) {
        BackButton(navController = navController)
        Text(
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 5.dp),
            text = "Action",
            color = FrispyTheme.Primary50,
            fontFamily = fontFamily,
            fontSize = 24.sp,
        )
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp),
                text = "Select here an action to be used to trigger events, can be configured later.",
                color = FrispyTheme.Primary50,
                fontFamily = fontFamily,
                fontSize = 16.sp,
            )
            Text(
                text = "If you don't see anything here, that means you have no linked services.",
                color = FrispyTheme.Primary50,
                fontFamily = fontFamily,
                fontSize = 16.sp,
            )
            for (i in 0..5) {
                ListView("Action $i")
            }
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TerminateButton(onClick = { navController.navigate("node_screen") })
        }
    }
}