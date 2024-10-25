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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.ui.theme.FrispyTheme
import org.rotclub.area.composes.LogoutButton
import org.rotclub.area.composes.ProfileApiCards
import org.rotclub.area.composes.SkeletonProfileApiCards
import org.rotclub.area.composes.skeletonLoading
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.lib.httpapi.Service
import org.rotclub.area.lib.httpapi.getServices
import org.rotclub.area.lib.httpapi.getUser
import org.rotclub.area.ui.theme.AreaTheme

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
            val user = getUser(token)
            if (user != null) {
                username = user.username
                email = user.email
            }
            getServices(services, token)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(FrispyTheme.Surface700),
    ) {
        Column (
            modifier = Modifier
                .padding(15.dp, 50.dp, 15.dp, 150.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(FrispyTheme.Surface500)
                .padding(25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .skeletonLoading((username == "" || email == "")),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = username,
                    color = FrispyTheme.Primary50,
                    fontFamily = fontFamily,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = email,
                    color = FrispyTheme.Primary50,
                    fontFamily = fontFamily,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 15.dp),
                color = FrispyTheme.Surface700,
                thickness = 2.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (services.value.isEmpty()) {
                    for (i in 1..7) {
                        SkeletonProfileApiCards()
                    }
                }
                for (service in services.value) {
                    ProfileApiCards(service.service, service.link, service.title, service.link_href, service.unlink_href)
                }
            }
            LogoutButton(
                Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Preview()
@Composable
fun ProfileScreenPreview() {
    AreaTheme {
        ProfileScreen()
    }
}
