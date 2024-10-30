package org.rotclub.area.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            color = FrispyTheme.TextColor,
            fontFamily = fontFamily,
            modifier = modifier,
        )
    }
}
