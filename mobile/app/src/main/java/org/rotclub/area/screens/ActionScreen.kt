package org.rotclub.area.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.rotclub.area.composes.BackButton
import org.rotclub.area.composes.ListView
import org.rotclub.area.composes.TerminateButton
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.NodeType
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.lib.httpapi.getAccesibleActions
import org.rotclub.area.lib.httpapi.putAction
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun ActionScreen(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    val programJson = backStackEntry.arguments?.getString("program")
    val gson = Gson()
    var program by remember { mutableStateOf(gson.fromJson(programJson, ProgramResponse::class.java)) }

    var accessibleActions by remember { mutableStateOf(emptyList<NodeType>()) }

    var parameter by remember { mutableStateOf(TextFieldValue("")) }
    var metadata1 by remember { mutableStateOf(TextFieldValue("")) }
    var metadata2 by remember { mutableStateOf(TextFieldValue("")) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                // TODO: redirect to login
                return@launch
            }
            accessibleActions = getAccesibleActions(token)
        }
    }

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
            accessibleActions.groupBy { it.service }.forEach { (_, actionsList) ->
                actionsList.forEach { action ->
                    ListView(action, true)
                }
            }
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                value = metadata1,
                onValueChange = { metadata1 = it },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    .background(Color.White)
                    .fillMaxWidth()
            )
            BasicTextField(
                value = metadata2,
                onValueChange = { metadata2 = it },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    .background(Color.White)
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        val token = sharedStorage.getToken()
                        if (token != null) {
                            val newMetadata = JsonObject().apply {
                                addProperty("metadata1", metadata1.text)
                                addProperty("metadata2", metadata2.text)
                            }
                            putAction(token, program.id, 80, newMetadata)
                            showToast = true
                            toastMessage = "Action updated successfully"
                        }
                    }
                },
                modifier = Modifier
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }

    if (showToast) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
        showToast = false
    }
}