package org.rotclub.area.lib.httpapi

import androidx.compose.runtime.MutableState

data class Reaction(
    val id: Int,
    val actionId: Int?,
    val metadata: String?,
    val reactionId: String,
)

data class Action(
    val id: Int,
    val actionId: String,
    val metadata: String,
    val reactions: List<Reaction>,
)

data class ProgramResponse(
    val id: Int,
    val name: String,
    val actions: List<Action>,
)

data class ProgramRequest(
    val name: String
)

suspend fun getPrograms(token: String): List<ProgramResponse> {
    try {
        val response = RetrofitClient.authApi.apiGetPrograms("Bearer $token")
        println(response)
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

suspend fun postProgram(token: String, name: String, errorMessage: MutableState<String>): ProgramResponse? {
    try {
        val response = RetrofitClient.authApi.apiPostProgram("Bearer $token", ProgramRequest(name))
        println(response)
        when (response.code()) {
            400 -> {
                errorMessage.value = "Bad request"
                return null
            }
            404 -> {
                errorMessage.value = "Not found"
                return null
            }
            200 -> {
                return response.body()
            }
            else -> {
                errorMessage.value = "An error occurred"
                return null
            }
        }
    } catch (e: Exception) {
        println("Error occurred: $e")
        errorMessage.value = "An error occurred"
        return null
    }
}