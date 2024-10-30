package org.rotclub.area.composes

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.AccAction
import org.rotclub.area.lib.httpapi.AccReaction
import org.rotclub.area.lib.httpapi.NodeType
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.ui.theme.FrispyTheme

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
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
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
                            val parameterInt = inputText.toIntOrNull()
                            if (parameterInt != null) {
                                onDismiss()
                                navController.navigate("node_screen/${gson.toJson(program)}")
                            }
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
fun TerminateButton(program: ProgramResponse, navController: NavController) {
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