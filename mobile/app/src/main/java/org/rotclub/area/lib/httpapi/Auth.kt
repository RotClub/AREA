package org.rotclub.area.lib.httpapi

import androidx.compose.runtime.MutableState
import org.rotclub.area.lib.user.Role

data class LoginRequest(
    val email: String,
    val password: String,
)

data class LoginResponse(
    val token: String,
    val role: Role,
)

suspend fun authLogin(
    email: String,
    password: String,
    loadingState: MutableState<Boolean>,
    loginResult: MutableState<LoginResponse?>,
    loginErrorStatus: MutableState<String>
) {
        loadingState.value = true
        try {
            println("Trying to login")
            println(LoginRequest(email, password))
            val response = RetrofitClient.authApi.login(
                LoginRequest(email, password)
            )
            when (response.code()) {
                404 -> {
                    loginResult.value = null
                    loginErrorStatus.value = "No account found, please sign up"
                    return
                }
                200 -> {
                    loginResult.value = response.body()
                    loginErrorStatus.value = ""
                    return
                }
                else -> {
                    loginResult.value = null
                    loginErrorStatus.value = "An error occurred"
                    return
                }
            }
        } catch (e: Exception) {
            println("Error occurred: $e")
            loginResult.value = null
            loginErrorStatus.value = "An error occurred"
        } finally {
            loadingState.value = false
        }
}
