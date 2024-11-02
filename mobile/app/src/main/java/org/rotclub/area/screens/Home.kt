package org.rotclub.area.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.rotclub.area.composes.HomeCircleChart
import org.rotclub.area.composes.ProfileCard
import org.rotclub.area.composes.ProgramsStats
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.apilink.getUser
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun HomeScreen() {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var createdAt by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                return@launch
            }
            val user = getUser(token)
            if (user != null) {
                username = user.username
                email = user.email
                role = user.role.toString()
                createdAt = user.createdAt
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(893.dp)
            .background(FrispyTheme.Surface700)
            .verticalScroll(rememberScrollState())
            .padding(20.dp, 60.dp, 20.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                append("Hello ")
                withStyle(
                    SpanStyle(
                        color = FrispyTheme.Primary500
                    )
                ) {
                    append(username)
                }
                append(", welcome to your dashboard.")
            },
            fontFamily = fontFamily,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(FrispyTheme.Surface700),
        ) {
            HomeCircleChart()
            ProgramsStats()
            ProfileCard(
                username = username,
                email = email,
                role = role,
                createdAt = createdAt
            )
        }
    }
}