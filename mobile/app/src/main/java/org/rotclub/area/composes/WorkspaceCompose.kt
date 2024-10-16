package org.rotclub.area.composes

import android.app.Dialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import org.rotclub.area.ui.theme.FrispyTheme
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily

@Composable
fun ColumnCard(navController: NavController, title: String, text: String) {
    var titleState by remember { mutableStateOf(title) }
    val textState by remember { mutableStateOf(text) }

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
                    value = title,
                    onValueChange = { newText: String -> titleState = newText },
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
                    onClick = { navController.navigate("node_screen") },
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
fun PlusButton(onClick: () -> Unit) {
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
fun ActionCard(navController: NavController) {
    var showDialogSet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .height(150.dp)
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(FrispyTheme.Surface900),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "On: action",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .padding(16.dp, 10.dp, 0.dp, 0.dp)
            )
            Row (
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.cog),
                    contentDescription = "Delete Action",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 16.dp, 0.dp)
                        .size(25.dp)
                        //open dialog
                        .clickable { showDialogSet = true }
                )
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Add Action",
                    tint = FrispyTheme.Success500,
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 16.dp, 0.dp)
                        .size(25.dp)
                        .clickable { /* do something */ }
                )
            }
        }
        Button(
            onClick = { navController.navigate("action_screen")  },
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
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Button Icon",
                modifier = Modifier.size(40.dp)
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