package org.rotclub.area.composes

import android.app.Dialog
import android.widget.ListView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.rotclub.area.ui.theme.FrispyTheme
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.composes.skeletonLoading
import org.rotclub.area.lib.httpapi.AccAction
import org.rotclub.area.lib.httpapi.AccReaction
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.lib.httpapi.patchProgramName
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.lib.httpapi.Action
import org.rotclub.area.lib.httpapi.NodeType

@Composable
fun SkeletonApiColumnCard() {
    Box(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(8.dp)) // Apply clip first
            .border(BorderStroke(2.dp, FrispyTheme.Primary500), shape = RoundedCornerShape(8.dp)) // Then apply border
            .background(FrispyTheme.Surface500)
            .skeletonLoading(),
    )
}

@Composable
fun ColumnCard(navController: NavController, title: String, text: String, program: ProgramResponse) {
    var titleState by remember { mutableStateOf(title) }
    val textState by remember { mutableStateOf(text) }
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    val gson = Gson()
    val programJson = gson.toJson(program)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(8.dp)) // Apply clip first
            .border(BorderStroke(2.dp, FrispyTheme.Primary500), shape = RoundedCornerShape(8.dp)) // Then apply border
            .background(FrispyTheme.Surface500),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                TextField(
                    value = titleState,
                    onValueChange = { newText: String ->
                        titleState = newText
                        coroutineScope.launch {
                            val token = sharedStorage.getToken()
                            if (token != null) {
                                patchProgramName(token, program.id, newText)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = fontFamily
                    ),
                    modifier = Modifier
                        .height(55.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
                Text(
                    text = textState,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                    fontFamily = fontFamily
                )
            }
            Column {
                Button(
                    onClick = { navController.navigate("node_screen/$programJson")},
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = FrispyTheme.Surface500,
                        disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(80.dp)
                        .border(BorderStroke(2.dp, FrispyTheme.Primary500)),
                    enabled = true
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.file_sliders),
                        contentDescription = "Button Icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PlusButton(onClick: () -> Unit, text: String?) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = FrispyTheme.Primary500,
            disabledContainerColor = FrispyTheme.Primary300.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        enabled = true
    ) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = "Button Icon",
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = if (text != null) {
                text
            } else {
                ""
            },
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 20.sp
        )
    }
}

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
fun ActionCard(navController: NavController, action: Action, program: ProgramResponse, onDelete: () -> Unit) {
    var showDialogSet by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }
    val gson = Gson()

    Column(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(FrispyTheme.Surface900),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (

        ) {
            Row (
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
                Row (
                    modifier = Modifier
                        .width(125.dp)
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.cog),
                        contentDescription = "Settings Action",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 16.dp, 0.dp)
                            .size(25.dp)
                            //open dialog
                            .clickable { showDialogSet = true }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.trash_2),
                        contentDescription = "Delete Action",
                        tint = FrispyTheme.Error500,
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 16.dp, 0.dp)
                            .size(25.dp)
                            //open dialog
                            .clickable { onDelete() }
                    )
                    Icon(
                        painter = if (!started) {
                            painterResource(id = R.drawable.play)
                        } else {
                            painterResource(id = R.drawable.square)
                        },
                        contentDescription = "Add Action",
                        tint = if (!started) {
                            FrispyTheme.Success500
                        } else {
                            FrispyTheme.Error500
                        },
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 16.dp, 0.dp)
                            .size(25.dp)
                            .clickable { started = !started }
                    )
                }
            }
            Text(
                text = action.metadata.toString(),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .padding(16.dp, 10.dp, 0.dp, 5.dp)
            )
        }
        if (action.reactions.isNotEmpty()) {
            for (reaction in action.reactions) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(5.dp, 5.dp, 5.dp, 0.dp),
                    color = FrispyTheme.Surface700,
                    thickness = 2.dp
                )
                Row (
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
                    Row (
                        modifier = Modifier
                            .width(125.dp)
                    )
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.cog),
                            contentDescription = "Settings Action",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(0.dp, 10.dp, 16.dp, 0.dp)
                                .size(25.dp)
                                .clickable { showDialogSet = true }
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
                        Icon(
                            painter = if (!started) {
                                painterResource(id = R.drawable.play)
                            } else {
                                painterResource(id = R.drawable.square)
                            },
                            contentDescription = "Add Action",
                            tint = if (!started) {
                                FrispyTheme.Success500
                            } else {
                                FrispyTheme.Error500
                            },
                            modifier = Modifier
                                .padding(0.dp, 10.dp, 16.dp, 0.dp)
                                .size(25.dp)
                                .clickable { started = !started }
                        )
                    }
                }
                Text(
                    text = reaction.metadata.toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    modifier = Modifier
                        .padding(16.dp, 10.dp, 0.dp, 5.dp)
                )
            }
        }
        Button(
            onClick = { navController.navigate("reaction_screen/${gson.toJson(program)}")},
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
    if (showDialogSet) {
        ActionDialog(onDismissRequest = { showDialogSet = false })
    }
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

@Composable
fun RadioButtonSelectAction(accAction: AccAction) {
    var selected by remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(FrispyTheme.Surface700),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { selected = !selected },
            colors = RadioButtonDefaults.colors(
                selectedColor = FrispyTheme.Primary500,
                unselectedColor = FrispyTheme.Surface400
            )
        )
        Text(
            text = accAction.displayName,
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun RadioButtonSelectReaction(accReaction: AccReaction) {
    var selected by remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(FrispyTheme.Surface700),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { selected = !selected },
            colors = RadioButtonDefaults.colors(
                selectedColor = FrispyTheme.Primary500,
                unselectedColor = FrispyTheme.Surface400
            )
        )
        Text(
            text = accReaction.displayName,
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun ListView(action: NodeType, check: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(FrispyTheme.Surface700)
            .clip(RoundedCornerShape(8.dp))
            .animateContentSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(FrispyTheme.Surface700)
                .clip(RoundedCornerShape(8.dp))
                .animateContentSize()
        ) {
            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(FrispyTheme.Surface700)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FrispyTheme.Surface700)
                ) {
                    IconButton(
                        onClick = { expanded = !expanded },
                    ) {
                        Icon(
                            painter = if (expanded) {
                                painterResource(id = R.drawable.chevron_down)
                            } else {
                                painterResource(id = R.drawable.chevron_up)
                            },
                            contentDescription = if (expanded) {
                                "Collapse"
                            } else {
                                "Expand"
                            },
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = action.service.toString(),
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                    )
                }
                if (expanded) {
                    if (check) {
                        for (accAction in action.actions) {
                            RadioButtonSelectAction(accAction = accAction)
                        }
                    } else {
                        for (accReaction in action.reactions) {
                            RadioButtonSelectReaction(accReaction = accReaction)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TerminateDialog(showDialog: Boolean, onDismiss: () -> Unit, navController: NavController, program: ProgramResponse) {
    var inputText by remember { mutableStateOf("") }
    val gson = Gson()

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FrispyTheme.Surface500)
                        .padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 16.dp),
                        text = "Configuration",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 26.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 16.dp),
                        text = "Edit here the configuration of the node.",
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 18.sp
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 16.dp),
                        color = FrispyTheme.Surface700,
                        thickness = 2.dp
                    )
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("Track") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    )
                    Button(
                        onClick = {
                            onDismiss()
                            navController.navigate("node_screen/${gson.toJson(program)}")
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
                            text = "Confirm",
                            color = Color.White,
                            fontFamily = fontFamily,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TerminateButton( program: ProgramResponse, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true  },
        modifier = Modifier
            .padding(0.dp, 16.dp, 0.dp, 0.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = FrispyTheme.Primary500,
            disabledContainerColor = FrispyTheme.Surface300.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        enabled = true
    ) {
        Text(
            text = "Confirm",
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 20.sp
        )
    }
    TerminateDialog(showDialog, onDismiss = { showDialog = false }, navController = navController, program = program)
}