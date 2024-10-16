package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun ProfileScreen() {
    var username by remember { mutableStateOf("Paul_le_BG") }
    var email by remember { mutableStateOf("paullebg@area.fr") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrispyTheme.Surface700),
    ) {
        Column (
            modifier = Modifier
                .padding(25.dp, 100.dp, 25.dp, 150.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(FrispyTheme.Surface500)
                .padding(25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pseudo: $username",
                    color = FrispyTheme.Primary50,
                    fontFamily = fontFamily,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Email: $email",
                    color = FrispyTheme.Primary50,
                    fontFamily = fontFamily,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
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
    }
}