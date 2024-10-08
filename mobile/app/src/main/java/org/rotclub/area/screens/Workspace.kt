package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.rotclub.area.R
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun WorkspaceScreen() {

    val columnCards = remember { mutableIntStateOf(0) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700)
            .padding(20.dp, 80.dp, 20.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until columnCards.intValue) {
            ColumnCard()
        }
        Button(
            onClick = { columnCards.intValue++ },
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
}