package org.rotclub.area.lib.httpapi

import androidx.compose.runtime.MutableState
import com.google.gson.JsonElement
import retrofit2.Response

data class Reaction(
    val id: Int,
    val actionId: Int?,
    val metadata: JsonElement?,
    val reactionId: String,
)

data class Action(
    val id: Int,
    val actionId: String,
    val metadata: JsonElement?,
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

suspend fun deleteProgram(token: String, id: Int): Boolean {
    try {
        val response = RetrofitClient.authApi.apiDeleteProgram("Bearer $token", id)
        println(response)
        when (response.code()) {
            200 -> {
                return true
            }
            else -> {
                return false
            }
        }
    } catch (e: Exception) {
        println("Error occurred: $e")
        return false
    }
}

suspend fun patchProgramName(token: String, id: Int, newName: String): Response<ProgramResponse> {
    val programRequest = ProgramRequest(name = newName)
    val response = RetrofitClient.authApi.apiPatchProgramName(token, id, programRequest)
    println(response)
    return response
}