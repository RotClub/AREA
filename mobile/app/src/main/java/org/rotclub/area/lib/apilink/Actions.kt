package org.rotclub.area.lib.apilink

//enum provider
enum class Provider {
    DISCORD,
    STEAM,
    SPOTIFY,
    BATTLENET,
    REDDIT,
    TWITCH
}

data class ActionMetaDataType(
    val id: String,
    val displayName: String,
    val type: String,
)

data class AccAction(
    val id: String,
    val displayName: String,
    val meta: Map<String, ActionMetaDataType>
)

data class AccReaction(
    val id: String,
    val displayName: String,
    val meta: Map<String, ActionMetaDataType>
)

data class NodeType(
    val service: Provider,
    val displayName: String,
    val iconPath: String,
    val actions: List<AccAction>,
    val reactions: List<AccReaction>
)

suspend fun getAccesibleActions(token: String): List<NodeType> {
    try {
        val response = RetrofitClient.authApi.apiGetAccesibleActions("Bearer $token")
        when (response.code()) {
            200 -> {
                return response.body() ?: emptyList()
            }
            else -> {
                return emptyList()
            }
        }
    } catch (e: Exception) {
        println("Error occurred: $e")
        return emptyList()
    }
}