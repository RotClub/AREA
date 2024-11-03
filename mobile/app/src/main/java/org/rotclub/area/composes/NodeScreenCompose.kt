package org.rotclub.area.composes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.apilink.Action
import org.rotclub.area.lib.apilink.ProgramResponse
import org.rotclub.area.lib.apilink.Reaction
import org.rotclub.area.lib.apilink.deleteReaction
import org.rotclub.area.lib.apilink.patchAction
import org.rotclub.area.lib.apilink.patchReaction
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme
import com.google.gson.JsonParser
import com.google.gson.JsonElement

@Composable
fun BackButton(navController: NavController) {
    Text(
        text = "< Go Back",
        color = FrispyTheme.Primary500,
        fontFamily = fontFamily,
        fontSize = 20.sp,
        modifier = Modifier
            .clickable { navController.popBackStack() }
    )
}

@Composable
fun ActionCard(navController: NavController, action: Action, program: ProgramResponse, onDelete: () -> Unit, onUpdateProgram: (ProgramResponse) -> Unit) {
    var showDialogSet by remember { mutableStateOf(false) }
    val gson = Gson()

    Column(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(FrispyTheme.Surface900),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ActionHeader(action, onDelete, { showDialogSet = true }, program, onUpdateProgram)
        val regex = Regex("\\{[^{}]+\\}")
        if (regex.containsMatchIn(action.metadata.toString())) {
            ActionMetadata(action.metadata.toString())
        } else {
            ActionMetadata("")
        }
        ActionReactions(action.reactions, program, onUpdateProgram)
        AddReactionButton(navController, gson, program, action)
    }
    if (showDialogSet) {
        ActionDialog(onDismissRequest = { showDialogSet = false })
    }
}

@Composable
fun ActionHeader(action: Action, onDelete: () -> Unit, onSettingsClick: () -> Unit, program: ProgramResponse, onUpdateProgram: (ProgramResponse) -> Unit) {
    var showSettingsDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "On: ${action.actionId}",
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = fontFamily,
            modifier = Modifier
                .padding(16.dp, 10.dp, 0.dp, 0.dp)
        )
        Row(
            modifier = Modifier
                .width(82.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cog),
                contentDescription = "Settings Action",
                tint = Color.White,
                modifier = Modifier
                    .padding(0.dp, 10.dp, 16.dp, 0.dp)
                    .size(25.dp)
                    .clickable { showSettingsDialog = true }
            )
            Icon(
                painter = painterResource(id = R.drawable.trash_2),
                contentDescription = "Delete Action",
                tint = FrispyTheme.Error500,
                modifier = Modifier
                    .padding(0.dp, 10.dp, 16.dp, 0.dp)
                    .size(25.dp)
                    .clickable { onDelete() }
            )
        }
    }
    if (showSettingsDialog) {
        ActionSettingsDialog(
            action = action,
            onDismissRequest = { showSettingsDialog = false },
            onSave = { newMetadata ->
                coroutineScope.launch {
                    val token = sharedStorage.getToken()
                    if (token != null) {
                        val newMetadataJson: JsonElement = JsonParser.parseString(newMetadata.toString().replace(" ", ""))
                        val success = patchAction(token, program.id, action.id, newMetadataJson)
                        if (success) {
                            val updatedActions = program.actions.map {
                                if (it.id == action.id) {
                                    it.copy(metadata = newMetadataJson)
                                } else {
                                    it
                                }
                            }
                            val updatedProgram = program.copy(actions = updatedActions)
                            onUpdateProgram(updatedProgram)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ActionMetadata(metadata: String) {
    val serializedMetadata = Gson().fromJson(metadata, Map::class.java)
    var finalString = ""

    if (serializedMetadata != null) {
        for ((key, value) in serializedMetadata) {
            finalString += "$key: $value\n"
        }
    }
    if (finalString.isNotEmpty()) {
        finalString = finalString.dropLast(1) // Remove the last /n
    }
    Text(
        text = finalString,
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = fontFamily,
        modifier = Modifier
            .padding(16.dp, 10.dp, 0.dp, 5.dp)
    )
}

@Composable
fun ActionReactions(reactions: List<Reaction>, program: ProgramResponse, onUpdateProgram: (ProgramResponse) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    val regex = Regex("\\{[^{}]+\\}")
    var serializedMetadata: Map<*, *>? = null
    var showSettingsDialog by remember { mutableStateOf(false) }
    var selectedReaction by remember { mutableStateOf<Reaction?>(null) }

    if (reactions.isNotEmpty()) {
        for (reaction in reactions) {
            var finalString = ""

            HorizontalDivider(
                modifier = Modifier
                    .padding(5.dp, 5.dp, 5.dp, 0.dp),
                color = FrispyTheme.Surface700,
                thickness = 2.dp
            )
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Do: ${reaction.reactionId}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    modifier = Modifier
                        .padding(16.dp, 10.dp, 0.dp, 0.dp)
                )
                Row(
                    modifier = Modifier
                        .width(82.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cog),
                        contentDescription = "Settings Reaction",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 16.dp, 0.dp)
                            .size(25.dp)
                            .clickable {
                                selectedReaction = reaction
                                showSettingsDialog = true
                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.trash_2),
                        contentDescription = "Delete Reaction",
                        tint = FrispyTheme.Error500,
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 16.dp, 0.dp)
                            .size(25.dp)
                            .clickable {
                                coroutineScope.launch {
                                    val token = sharedStorage.getToken()
                                    if (token != null) {
                                        val success = deleteReaction(token, program.id, reaction.id)
                                        if (success) {
                                            val updatedReactions = program.actions.find { it.id == reaction.actionId }?.reactions?.toMutableList()
                                            val updatedProgram = program.copy(actions = program.actions.map {
                                                if (it.id == reaction.actionId) {
                                                    it.copy(reactions = updatedReactions ?: emptyList())
                                                } else {
                                                    it
                                                }
                                            })
                                            onUpdateProgram(updatedProgram)
                                        }
                                    }
                                }
                            }
                    )
                }
            }
            if (regex.containsMatchIn(reaction.metadata.toString())) {
                serializedMetadata = Gson().fromJson(reaction.metadata.toString(), Map::class.java)
            }
            if (serializedMetadata != null) {
                for ((key, value) in serializedMetadata) {
                    finalString += "$key: $value\n"
                }
            }
            if (finalString.isNotEmpty()) {
                finalString = finalString.dropLast(1)
            }
            Text(
                text = finalString,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .padding(16.dp, 10.dp, 0.dp, 5.dp)
            )
        }
    }

    if (showSettingsDialog && selectedReaction != null) {
        ReactionSettingsDialog(
            reaction = selectedReaction!!,
            onDismissRequest = { showSettingsDialog = false },
            onSave = { newMetadata ->
                coroutineScope.launch {
                    val token = sharedStorage.getToken()
                    if (token != null) {
                        val newMetadataJson: JsonElement = JsonParser.parseString(newMetadata.toString().replace(" ", ""))
                        val success = patchReaction(token, program.id, selectedReaction!!.id, newMetadataJson)
                        if (success) {
                            val updatedReactions = program.actions.find { it.id == selectedReaction!!.actionId }?.reactions?.map {
                                if (it.id == selectedReaction!!.id) {
                                    it.copy(metadata = newMetadataJson)
                                } else {
                                    it
                                }
                            }
                            val updatedProgram = program.copy(actions = program.actions.map {
                                if (it.id == selectedReaction!!.actionId) {
                                    it.copy(reactions = updatedReactions ?: emptyList())
                                } else {
                                    it
                                }
                            })
                            onUpdateProgram(updatedProgram)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun AddReactionButton(navController: NavController, gson: Gson, program: ProgramResponse, action: Action) {
    Button(
        onClick = { navController.navigate("reaction_screen/${gson.toJson(program)}/${action.id}") },
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = FrispyTheme.Primary500,
            disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth()
            .padding(0.dp, 5.dp, 0.dp, 0.dp),
        enabled = true
    ) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = "Button Icon",
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "Add reaction",
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 20.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionSettingsDialog(
    action: Action,
    onDismissRequest: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    var metadataMap by remember { mutableStateOf(Gson().fromJson(action.metadata.toString(), Map::class.java) as Map<String, String>) }

    AlertDialog(
        containerColor = FrispyTheme.Surface700,
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = "Edit Action",
                color = FrispyTheme.Primary500,
                fontFamily = fontFamily,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (metadataMap.isEmpty()) {
                    Text(
                        text = "No configuration needed",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 18.sp
                    )
                } else {
                    metadataMap.forEach { (key, value) ->
                        var textValue by remember { mutableStateOf(value) }
                        Text(
                            text = key,
                            color = Color.White,
                            fontFamily = fontFamily,
                            fontSize = 18.sp
                        )
                        BasicTextField(
                            value = textValue,
                            onValueChange = {
                                textValue = it
                                metadataMap = metadataMap.toMutableMap().apply { put(key, it) }
                            },
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 5.dp)
                                .background(FrispyTheme.Surface500)
                                .fillMaxWidth()
                                .height(25.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 18.sp,
                                color = Color.White,
                                fontFamily = fontFamily
                            ),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (metadataMap.isNotEmpty()) {
                Button(
                    onClick = {
                        onSave(metadataMap)
                        onDismissRequest()
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = FrispyTheme.Primary500,
                        disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    enabled = true
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 20.sp
                    )
                }
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = FrispyTheme.Error500,
                    disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                enabled = true
            ) {
                Text(
                    text = "Cancel",
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontSize = 20.sp
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReactionSettingsDialog(
    reaction: Reaction,
    onDismissRequest: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    var metadataMap by remember { mutableStateOf(Gson().fromJson(reaction.metadata.toString(), Map::class.java) as Map<String, Any>) }

    AlertDialog(
        containerColor = FrispyTheme.Surface700,
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = "Edit Reaction",
                color = FrispyTheme.Primary500,
                fontFamily = fontFamily,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (metadataMap.isEmpty()) {
                    Text(
                        text = "No configuration needed",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 18.sp
                    )
                } else {
                    metadataMap.forEach { (key, value) ->
                        var textValue by remember { mutableStateOf(value.toString()) }
                        Text(
                            text = key,
                            color = Color.White,
                            fontFamily = fontFamily,
                            fontSize = 18.sp
                        )
                        BasicTextField(
                            value = textValue,
                            onValueChange = {
                                textValue = it
                                metadataMap = metadataMap.toMutableMap().apply { put(key, it) }
                            },
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 5.dp)
                                .background(FrispyTheme.Surface500)
                                .fillMaxWidth()
                                .height(25.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 18.sp,
                                color = Color.White,
                                fontFamily = fontFamily
                            ),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (metadataMap.isNotEmpty()) {
                Button(
                    onClick = {
                        onSave(metadataMap.mapValues { it.value.toString() })
                        onDismissRequest()
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = FrispyTheme.Primary500,
                        disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    enabled = true
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 20.sp
                    )
                }
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = FrispyTheme.Error500,
                    disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                enabled = true
            ) {
                Text(
                    text = "Cancel",
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontSize = 20.sp
                )
            }
        },
    )
}

@Composable
fun ActionDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(FrispyTheme.Surface500)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 16.dp),
                    text = "Action",
                    color = FrispyTheme.Primary500,
                    fontFamily = fontFamily,
                    fontSize = 20.sp
                )
                Button(
                    onClick = { onDismissRequest() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = FrispyTheme.Primary500,
                        disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    enabled = true
                ) {
                    Text(
                        text = "Close",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}