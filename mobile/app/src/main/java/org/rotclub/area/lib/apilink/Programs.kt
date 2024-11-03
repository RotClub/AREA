package org.rotclub.area.lib.apilink

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
    var metadata: JsonElement?,
    var reactions: List<Reaction>,
)

data class ProgramResponse(
    val id: Int,
    val name: String,
    val actions: List<Action>,
)

data class ProgramRequest(
    val name: String
)

data class NewActionIdRequest(
    val id: String,
    val isAction: Boolean = true,
    val metadata: JsonElement?
)

data class NewReactionIdRequest(
    val isReaction: Boolean = true,
    val id: Int,
    val reactionId: String,
    val metadata: JsonElement?
)

data class ActionIdRequest(
    val id: Int,
    val isAction: Boolean = true
)

data class ReactionIdRequest(
    val id: Int,
    val isReaction: Boolean = true
)

data class PatchActionRequest(
    val isAction: Boolean = true,
    val id: Int,
    val metadata: JsonElement?
)

suspend fun getPrograms(token: String): List<ProgramResponse> {
    try {
        val response = RetrofitClient.authApi.apiGetPrograms("Bearer $token")
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
    return response
}

suspend fun deleteAction(token: String, programId: Int, actionId: Int): Boolean {
    if (actionId <= 0) {
        println("Invalid action ID")
        return false
    }

    return try {
        val response = RetrofitClient.authApi.apiDeleteAction(
            "Bearer $token",
            programId,
            ActionIdRequest(isAction = true, id = actionId)
        )
        response.code() == 200
    } catch (e: Exception) {
        println("Error occurred: $e")
        false
    }
}

suspend fun deleteReaction(token: String, programId: Int, reactionId: Int): Boolean {
    if (reactionId <= 0) {
        println("Invalid reaction ID")
        return false
    }

    return try {
        val response = RetrofitClient.authApi.apiDeleteReaction(
            "Bearer $token",
            programId,
            ReactionIdRequest(isReaction = true, id = reactionId)
        )
        response.code() == 200
    } catch (e: Exception) {
        println("Error occurred: $e")
        false
    }
}

suspend fun putAction(token: String, inspectingNode: Int, newActionId: String, newActionMeta: JsonElement): Boolean {
    try {
        val response = RetrofitClient.authApi.apiPutAction("Bearer $token", inspectingNode, NewActionIdRequest(id = newActionId, isAction = true, metadata = newActionMeta))
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

suspend fun putReaction(token: String, inspectingNode: Int, actionId: Int, newReactionId: String, newReactionMeta: JsonElement): Boolean {
    try {
        val response = RetrofitClient.authApi.apiPutReaction("Bearer $token", inspectingNode, NewReactionIdRequest(isReaction = true, id = actionId, reactionId = newReactionId, metadata = newReactionMeta))
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

suspend fun patchAction(token: String, programId: Int, actionId: Int, newActionMeta: JsonElement): Boolean {
    try {
        val response = RetrofitClient.authApi.apiPatchAction("Bearer $token", programId, PatchActionRequest(isAction = true, id = actionId, metadata = newActionMeta))
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