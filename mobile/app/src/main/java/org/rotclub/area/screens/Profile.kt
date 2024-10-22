package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.ui.theme.FrispyTheme
import org.rotclub.area.composes.LogoutButton
import org.rotclub.area.composes.ProfileApiCards
import org.rotclub.area.lib.SharedStorageUtils
import org.rotclub.area.lib.httpapi.Service
import org.rotclub.area.lib.httpapi.getServices

@Composable
fun ProfileScreen() {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val services = remember { mutableStateOf(emptyList<Service>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                // TODO: redirect to login
                return@launch
            }
            getServices(services, token)
        }
    }
    
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
                HorizontalDivider(
                    modifier = Modifier.padding(0.dp, 20.dp),
                    color = FrispyTheme.Surface700,
                    thickness = 2.dp
                )
                for (service in services.value) {
                    ProfileApiCards(service.service, service.link, service.title, service.link_href, service.unlink_href)
                }
            }
            LogoutButton()
        }
    }
}