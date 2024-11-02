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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.rotclub.area.composes.ActionCard
import org.rotclub.area.composes.BackButton
import org.rotclub.area.composes.PlusButton
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.apilink.ProgramResponse
import org.rotclub.area.lib.apilink.deleteAction
import org.rotclub.area.lib.apilink.deleteProgram
import org.rotclub.area.lib.apilink.deleteReaction
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun NodeScreen(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    val programJson = backStackEntry.arguments?.getString("program")
    val gson = Gson()
    var program by remember { mutableStateOf(gson.fromJson(programJson, ProgramResponse::class.java)) }

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
            fontSize = 26.sp,
            modifier = Modifier.padding(10.dp, 20.dp, 0.dp, 5.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            program.actions.forEach { action ->
                Text(
                    text =  action.reactions.toString(),
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                )
                ActionCard(navController = navController, action = action, program = program, onDelete = {
                    coroutineScope.launch {
                        val token = sharedStorage.getToken()
                        if (token != null) {
                            val success = deleteAction(token, program.id, action.id)
                            if (success) {
                                val updatedProgram = program.copy(actions = program.actions.filter { it.id != action.id })
                                program = updatedProgram
                            }
                        }
                    }
                })
            }
            PlusButton (onClick = {
                navController.navigate("action_screen/${gson.toJson(program)}")
            }, text = "Add action")
        }
    }
}