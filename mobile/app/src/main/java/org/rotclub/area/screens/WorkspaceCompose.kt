package org.rotclub.area.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import org.rotclub.area.ui.theme.FrispyTheme
import org.rotclub.area.R

@Composable
fun ColumnCard() {
    var text by remember { mutableStateOf("Column Card") }

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
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
            ) {
                TextField(
                    value = text,
                    onValueChange = { newText: String -> text = newText },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp, color = Color.White),
                    modifier = Modifier
                        .height(50.dp)

                )
                Text(
                    text = "This is a column card",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp)
                )
            }
            Column {
                Button(
                    onClick = { /* Do something */ },
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