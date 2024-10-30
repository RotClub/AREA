package org.rotclub.area.composes

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.AccAction
import org.rotclub.area.lib.httpapi.AccReaction
import org.rotclub.area.lib.httpapi.Action
import org.rotclub.area.lib.httpapi.NodeType
import org.rotclub.area.lib.httpapi.ProgramResponse
import org.rotclub.area.lib.httpapi.patchProgramName
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme

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

