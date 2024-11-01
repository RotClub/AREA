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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import org.rotclub.area.composes.BackButton
import org.rotclub.area.composes.ListViewReactions
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.AccAction
import org.rotclub.area.lib.httpapi.AccReaction
import org.rotclub.area.lib.httpapi.NodeType
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.lib.httpapi.getAccesibleActions
import org.rotclub.area.lib.httpapi.getPrograms
import org.rotclub.area.lib.httpapi.putAction
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun ReactionScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    val programJson = backStackEntry.arguments?.getString("program")
    val gson = Gson()
    var program by remember { mutableStateOf(gson.fromJson(programJson, ProgramResponse::class.java)) }

    var accessibleReactions by remember { mutableStateOf(emptyList<NodeType>()) }
    var selectedService by remember { mutableStateOf(String()) }
    var selectedReaction by remember { mutableStateOf<AccReaction?>(null) }

    var metadata by remember { mutableStateOf(TextFieldValue("")) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                // TODO: redirect to login
                return@launch
            }
            accessibleReactions = getAccesibleActions(token)
        }
    }

    Column(
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
            text = "Reaction",
            color = FrispyTheme.Primary50,
            fontFamily = fontFamily,
            fontSize = 24.sp,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp),
                text = "Select here a reaction, they represent events to be triggered, can be configured later.",
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
            accessibleReactions.groupBy { it.service }.forEach { (_, reactionsList) ->
                reactionsList.forEach { action ->
                    ListViewReactions(
                        action = action,
                        selectedReaction = selectedReaction,
                        onClick = { reaction, service ->
                            selectedService = service
                            selectedReaction = reaction
                        }
                    )
                }
            }
        }
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = FrispyTheme.Primary500,
                disabledContainerColor = FrispyTheme.Primary500.copy(alpha = 0.5f),
                contentColor = Color.White,
                disabledContentColor = Color.White.copy(alpha = 0.5f),
            )
        ) {
            Text("Configurer")
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = FrispyTheme.Surface700,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Configuration",
                    color = Color.White,
                    fontFamily = fontFamily,
                )
            },
            text = {
                Column {
                    Text(
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp),
                        text = "Edit here the configuration of the node.",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 18.sp,
                    )
                    Text(
                        text = selectedReaction?.meta?.values?.joinToString(", ") { it.displayName + ":" } ?: "",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 16.sp,
                    )
                    BasicTextField(
                        value = metadata,
                        onValueChange = { metadata = it },
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 5.dp)
                            .background(FrispyTheme.Surface500)
                            .fillMaxWidth()
                            .height(25.dp),
                        textStyle = androidx.compose.ui.text.TextStyle.Default.copy(
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = fontFamily
                        ),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val token = sharedStorage.getToken()
                            if (token != null) {
                                val newMetadata = JsonObject().apply {
                                    addProperty(
                                        selectedReaction?.meta?.values?.joinToString(", ") { it.id } ?: "",
                                        metadata.text
                                    )
                                }
                                val actionUpdated = putAction(token, program.id, "${selectedService}:${selectedReaction?.id}", newMetadata)
                                if (actionUpdated) {
                                    val updatedPrograms = getPrograms(token)
                                    showToast = true
                                    toastMessage = "Reaction updated successfully"
                                    showDialog = false
                                    val updatedProgram = updatedPrograms.find { it.id == program.id }
                                    val jsonUpdatedProgram = gson.toJson(updatedProgram)
                                    navController.navigate("node_screen/$jsonUpdatedProgram")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrispyTheme.Success500,
                        disabledContainerColor = FrispyTheme.Success500.copy(alpha = 0.5f),
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = 0.5f),
                    )
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FrispyTheme.Error500,
                        disabledContainerColor = FrispyTheme.Error500.copy(alpha = 0.5f),
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = 0.5f),
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showToast) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
        showToast = false
    }
}