package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.rotclub.area.composes.ActionCard
import org.rotclub.area.composes.BackButton
import org.rotclub.area.composes.ColumnCard
import org.rotclub.area.composes.ListView
import org.rotclub.area.composes.PlusButton
import org.rotclub.area.composes.TerminateButton
import org.rotclub.area.composes.SkeletonApiColumnCard
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.lib.httpapi.getPrograms
import org.rotclub.area.lib.httpapi.postProgram
import org.rotclub.area.lib.httpapi.deleteProgram
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme
import com.google.gson.Gson

data class ColumnCardData(val title: String, val text: String)

@Composable
fun WorkspaceScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    val programs = remember { mutableStateOf(emptyList<ProgramResponse?>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                // TODO: redirect to login
                return@launch
            }
            programs.value = getPrograms(token)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(893.dp)
            .background(FrispyTheme.Surface700)
            .verticalScroll(rememberScrollState())
            .padding(20.dp, 80.dp, 20.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (programs.value.isEmpty()) {
            for (i in 0..3) {
                SkeletonApiColumnCard()
            }
        }
        for (program in programs.value) {
            if (program != null) {
                ColumnCard(
                    navController = navController,
                    title = program.name,
                    text = "This is a program",
                    program = program
                )
            }
        }
        PlusButton {
            coroutineScope.launch {
                val token = sharedStorage.getToken()
                if (token == null) {
                    // TODO: redirect to login
                    return@launch
                }
                programs.value += postProgram(token, "caca", mutableStateOf(""))
            }
        }
    }
}

data class ActionCardData(val title: String, val text: String)

@Composable
fun NodeScreen(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    val programJson = backStackEntry.arguments?.getString("program")
    val gson = Gson()
    val program = gson.fromJson(programJson, ProgramResponse::class.java)
    val actionCards = remember { mutableStateOf(listOf(null)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(893.dp)
            .background(FrispyTheme.Surface700)
            .padding(25.dp, 60.dp, 20.dp, 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BackButton(navController = navController)
            Text(
                text = "Delete Program",
                color = FrispyTheme.Error500,
                fontFamily = fontFamily,
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    coroutineScope.launch {
                        val token = sharedStorage.getToken()
                        if (token == null) {
                            return@launch
                        }
                        deleteProgram(token, program.id)
                        navController.popBackStack()
                    }
                }
            )
        }
        Text(
            text = program.name,
            color = FrispyTheme.Primary50,
            fontFamily = fontFamily,
            fontSize = 24.sp,
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 5.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            program.actions.forEach { action ->
                ActionCard(navController = navController, action = action)
            }
            PlusButton {
                navController.navigate("action_screen")
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
            TerminateButton(onClick = {
                navController.navigate("node_screen")
            })
        }
    }
}