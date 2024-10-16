package org.rotclub.area.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun LoginInput(value: String, onValueChange: ((String) -> Unit) = {},
               label: String = "", modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value) }
    var thisOnValueChange = onValueChange
    if (onValueChange == {}) {
        thisOnValueChange = { text = it }
    }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedLabelColor = FrispyTheme.Surface400,
            unfocusedBorderColor = FrispyTheme.Surface400,
            focusedBorderColor = FrispyTheme.Primary500,
            focusedLabelColor = FrispyTheme.Primary500,
            cursorColor = FrispyTheme.Primary300,
            focusedTextColor = FrispyTheme.Primary300,
            unfocusedTextColor = FrispyTheme.Surface200
        )
    )
}

@Composable
fun CardColumn(modifier: Modifier, spacing: Dp, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .padding(top = spacing / 2, bottom = spacing + 30.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(roundedValue))
            .background(FrispyTheme.Surface500)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun TitleHeader(modifier: Modifier = Modifier, iconSize: Dp = 100.dp, titleSize: TextUnit = 70.sp) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.frispyicon),
            colorFilter = ColorFilter.tint(FrispyTheme.Primary500),
            contentDescription = "Frispy Logo",
            modifier = modifier
                .padding(bottom = 5.dp)
                .size(iconSize)
        )
        Text(
            text = "Frispy",
            fontSize = titleSize,
            color = FrispyTheme.Primary50,
            fontFamily = fontFamily,
            modifier = modifier,
        )
    }
}
