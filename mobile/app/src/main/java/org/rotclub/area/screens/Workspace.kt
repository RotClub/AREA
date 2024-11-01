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
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.lib.httpapi.getPrograms
import org.rotclub.area.lib.httpapi.postProgram
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme


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
        PlusButton (onClick = {
            coroutineScope.launch {
                val token = sharedStorage.getToken()
                if (token == null) {
                    // TODO: redirect to login
                    return@launch
                }
                programs.value += postProgram(token, "New program", mutableStateOf(""))
            }
        }, text = "")
    }
}
