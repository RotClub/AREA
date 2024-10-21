package org.rotclub.area.composes

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun LogoutButton() {
    Button(
        onClick = { /* do something */ },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = FrispyTheme.Surface500,
            containerColor = FrispyTheme.Error500,
            disabledContentColor = FrispyTheme.Surface200,
            disabledContainerColor = FrispyTheme.Surface400
        )
    ) {
        Text(
            text = "Logout",
            color = FrispyTheme.Surface500,
            fontFamily = fontFamily,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun ProfileApiCards(i: Int) {
    Row (
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(FrispyTheme.Surface700)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "API $i",
            color = FrispyTheme.Primary50,
            fontFamily = fontFamily,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Button(
            onClick = { /* do something */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = FrispyTheme.Surface500,
                containerColor = FrispyTheme.Error500,
                disabledContentColor = FrispyTheme.Surface200,
                disabledContainerColor = FrispyTheme.Surface400
            ),
            modifier = Modifier
                .width(170.dp)
        ) {
            Text("Disconnected")
        }
    }
}