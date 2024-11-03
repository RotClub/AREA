package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.rotclub.area.composes.ColumnCard
import org.rotclub.area.composes.PlusButton
import org.rotclub.area.composes.SkeletonApiColumnCard
import org.rotclub.area.lib.apilink.ProgramResponse
import org.rotclub.area.lib.apilink.getPrograms
import org.rotclub.area.lib.apilink.postProgram
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme


data class ColumnCardData(val title: String, val text: String)

@Composable
fun WorkspaceScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    val programs = remember { mutableStateOf(emptyList<ProgramResponse?>()) }
    val isResponseReceived = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                // TODO: redirect to login
                return@launch
            }
            programs.value = getPrograms(token)
            isResponseReceived.value = true
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
        if (programs.value.isEmpty() && !isResponseReceived.value) {
            for (i in 0..2) {
                SkeletonApiColumnCard()
            }
        }
        for (program in programs.value) {
            if (program != null) {
                ColumnCard(
                    navController = navController,
                    title = program.name,
                    text = program.actions.size.toString() + " nodes",
                    program = program
                )
            }
        }
        PlusButton (onClick = {
            coroutineScope.launch {
                val token = sharedStorage.getToken()
                if (token == null) {
                    // TODO: redirect to login
                    return@launch
                }
                val newProgram = postProgram(token, "New Program", errorMessage = mutableStateOf(""))
                if (newProgram != null) {
                    programs.value = getPrograms(token)
                }
            }
        }, text = "")
    }
}
