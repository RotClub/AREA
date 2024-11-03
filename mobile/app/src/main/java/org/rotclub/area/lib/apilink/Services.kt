package org.rotclub.area.lib.apilink

import androidx.compose.runtime.MutableState
import org.rotclub.area.R

enum class ServiceType(val icon: Int)
{
    DISCORD(R.drawable.discord_icon),
    STEAM(R.drawable.steam_icon),
    SPOTIFY(R.drawable.spotify_icon),
    BATTLENET(R.drawable.battlenet_icon),
    REDDIT(R.drawable.reddit_icon),
    TWITCH(R.drawable.twitch_icon),
}

data class Service(
    val service: ServiceType,
    val link: Boolean,
    val title: String,
    val link_href: String,
    val unlink_href: String,
)

suspend fun getServices(services: MutableState<List<Service>>, token: String) {
    try {
        val response = RetrofitClient.authApi.getServices("Bearer $token")
        when (response.code()) {
            200 -> {
                services.value = response.body() ?: emptyList()
                return
            }
            else -> {
                services.value = emptyList()
                return
            }
        }
    } catch (e: Exception) {
        println("Error occurred: $e")
        services.value = emptyList()
    }
}
