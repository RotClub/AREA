package org.rotclub.area.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.httpapi.RetrofitClient
import org.rotclub.area.lib.httpapi.ServiceType
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.lib.utils.BrowserUtils
import org.rotclub.area.lib.utils.SharedStorageUtils
import org.rotclub.area.ui.theme.AreaTheme
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun LogoutButton(modifier: Modifier = Modifier, globalNavController: NavHostController) {
    val sharedStorage = SharedStorageUtils(LocalContext.current)
    Button(
        onClick = {
            sharedStorage.clearToken()
            globalNavController.navigate(GlobalRoutes.Login.route)
        },
        modifier = modifier
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
fun ProfileApiCards(service: ServiceType?, link: Boolean, title: String,
    linkHref: String, unlinkHref: String, token: String?) {
    if (service == null)
        return
    var thisLink by remember { mutableStateOf(link) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val linkIcon: Int = if (!thisLink) R.drawable.unlink else R.drawable.link
    val linkIconColor = if (!thisLink) FrispyTheme.TextColor else FrispyTheme.Surface900
    val buttonColor = if (!thisLink) FrispyTheme.Error500 else FrispyTheme.Success500
    Row (
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, FrispyTheme.Surface400, RoundedCornerShape(10.dp))
            .background(FrispyTheme.Surface700)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (service.icon != 0) {
                Image(
                    painter = painterResource(id = service.icon),
                    contentDescription = "Service Icon",
                    modifier = Modifier
                        .width(50.dp)
                        .padding(end = 15.dp)
                )
            }
            Text(
                text = title,
                color = FrispyTheme.Primary50,
                fontFamily = fontFamily,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold,
            )
        }
        Button(
            onClick = {
                if (!link) {
                    if (linkHref == "") {
                        return@Button
                    }
                    coroutineScope.launch {
                        val oauthUrl = RetrofitClient.authApi.apiGetServiceOauth(linkHref, token!!).body()?.url
                        BrowserUtils.openUrl(
                            context, oauthUrl,
                            token
                        )
                    }
                    return@Button
                }
                if (unlinkHref == "") {
                    return@Button
                }
                coroutineScope.launch {
                    val res = RetrofitClient.authApi.apiUnlinkService(unlinkHref, "Bearer $token")
                    if (res.isSuccessful) {
                        thisLink = false
                    }
                }
            },
            shape = RoundedCornerShape(roundedValue),
            colors = ButtonDefaults.buttonColors(
                contentColor = FrispyTheme.Surface500,
                containerColor = buttonColor,
                disabledContentColor = FrispyTheme.Surface200,
                disabledContainerColor = FrispyTheme.Surface400
            ),
            modifier = Modifier
                .width(90.dp)
        ) {
            Image(
                painter = painterResource(id = linkIcon),
                contentDescription = "Link Icon",
                colorFilter = ColorFilter.tint(linkIconColor),
                modifier = Modifier
                    .width(30.dp)
                    .padding(end = 5.dp)
            )
        }
    }
}

@Composable
fun SkeletonProfileApiCards() {
    Box (
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, FrispyTheme.Surface400, RoundedCornerShape(10.dp))
            .background(FrispyTheme.Surface700)
            .skeletonLoading()
            .padding(35.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileApiCardsPreview() {
    AreaTheme {
        ProfileApiCards(
            ServiceType.SPOTIFY,
            false,
            "Spotify",
            "https://spotify.com",
            "https://spotify.com",
            "token"
        )
    }
}
