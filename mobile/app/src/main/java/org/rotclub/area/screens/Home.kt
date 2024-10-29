package org.rotclub.area.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.ui.theme.FrispyTheme
import org.rotclub.area.composes.HomeCircleChart
import org.rotclub.area.composes.ProgramsStats
import org.rotclub.area.composes.ProfileCard
import org.rotclub.area.lib.BottomBarScreen

@Composable
fun HomeScreen() {
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
                    append("Paul_le_BG")
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
            ProfileCard()
        }
    }
}