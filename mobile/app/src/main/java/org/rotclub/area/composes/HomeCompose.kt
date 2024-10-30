package org.rotclub.area.composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.Service
import org.rotclub.area.lib.httpapi.getServices
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
private fun ChartCirclePie(
    modifier: Modifier,
    value: Float,
    size: Dp,
    strokeWidth: Dp,
    midText: String
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(midText),
        style = TextStyle(fontSize = 30.sp, color = Color.White))
    val textSize = textLayoutResult.size

    Canvas(modifier = modifier
        .size(size)
        .background(FrispyTheme.Surface600)
        .padding(12.dp), onDraw = {

        val startAngle = -87f
        val sweepAngle = (value / 100) * 360

        drawArc(
            color = FrispyTheme.Surface500,
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Square,
                join = StrokeJoin.Round
            )
        )

        drawArc(
            color = FrispyTheme.Primary500,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Square,
                join = StrokeJoin.Round
            )
        )

        drawText(
            textMeasurer = textMeasurer,
            text = midText,
            topLeft = Offset(
                (this.size.width - textSize.width) / 2f,
                (this.size.height - textSize.height) / 2f
            ),
            style = TextStyle(
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
        )
    })
}

@Composable
fun HomeCircleChart() {
    val coroutineScope = rememberCoroutineScope()
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    val services = remember { mutableStateOf(emptyList<Service>()) }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = sharedStorage.getToken()
            if (token == null) {
                return@launch
            }
            // TODO: Avoid spamming requests on login
            getServices(services, token)
        }
    }

    val linkedServicesCount = services.value.count { it.link }
    val value = 100f / 7
    val multiplier : Int = linkedServicesCount

    Column (
        modifier = Modifier
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(FrispyTheme.Surface600)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 15.dp),
            text = "Connected services:",
            color = Color.White,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        services.value.forEach { service ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.title,
                    color = if (service.link) FrispyTheme.Success500 else FrispyTheme.Error500,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = if (service.link) painterResource(id = R.drawable.check) else painterResource(id = R.drawable.x),
                    tint = if (service.link) FrispyTheme.Success500 else FrispyTheme.Error500,
                    contentDescription = "Icon",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
        ChartCirclePie(
            modifier = Modifier
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = value * multiplier,
            size = 300.dp,
            strokeWidth = 16.dp,
            "${linkedServicesCount}/7"
        )
    }
}

@Composable
fun ProgramsStats()
{
    val statsData = listOf(
        "Programs" to "2",
        "Nodes avg" to "5.7",
        "Empty programs" to "0"
    )

    Column (
        modifier = Modifier
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(FrispyTheme.Surface600)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 15.dp),
            text = "Programs statistics:",
            color = Color.White,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                statsData.forEach { (label, value) ->
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier,
                            text = label,
                            color = Color.White,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                        Text(
                            modifier = Modifier,
                            text = value,
                            color = FrispyTheme.Primary500,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard () {
    val profileData = listOf(
        "Username" to "Paul_le_BG",
        "Email" to "Paul_le_BG@AREA.fr",
        "Role" to "ADMIN",
        "Created at" to "2024-10-03"
    )

    Column (
        modifier = Modifier
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(FrispyTheme.Surface600)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
                text = "Profile",
                color = Color.White,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Icon(
                painter = painterResource(id = R.drawable.user),
                tint = Color.White,
                contentDescription = "Profile",
                modifier = Modifier.size(25.dp)
            )
        }
        profileData.forEach { (label, value) ->
            Text(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 15.dp),
                text = buildAnnotatedString {
                    append("$label: ")
                    withStyle(
                        SpanStyle(
                            color = FrispyTheme.Primary500
                        )
                    ) {
                        append(value)
                    }
                },
                color = Color.White,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }
    }
}