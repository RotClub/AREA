package org.rotclub.area.lib.apilink

import org.rotclub.area.lib.user.Role

data class UserResponse(
    val email: String,
    val role: Role,
    val createdAt: String,
    val username: String,
)

suspend fun getUser(token: String): UserResponse? {
    try {
        val response = RetrofitClient.authApi.apiGetUser("Bearer $token")
        when (response.code()) {
            200 -> {
                return response.body()
            }
            else -> {
                return null
            }
        }
    } catch (e: Exception) {
        println("Error occurred: $e")
        return null
    }
}
