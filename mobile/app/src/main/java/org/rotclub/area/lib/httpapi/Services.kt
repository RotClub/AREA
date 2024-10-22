package org.rotclub.area.lib.httpapi

import androidx.compose.runtime.MutableState
import org.rotclub.area.R

enum class ServiceType(val icon: Int)
{
    DISCORD(0),
    STEAM(0),
    SPOTIFY(R.drawable.spotify_icon),
    BATTLENET(0),
    EPICGAMES(0),
    RIOT(0),
    X(0),
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
        println(response)
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
