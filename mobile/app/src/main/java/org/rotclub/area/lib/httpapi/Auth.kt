package org.rotclub.area.lib.httpapi

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

enum class Role {
    ADMIN,
    API_USER,
    USER,
}

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}

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
    println("Logging in with $email and $password")
        loadingState.value = true
        try {
            println("Trying to login")
            println(LoginRequest(email, password))
            val response = RetrofitClient.authApi.login(
                LoginRequest(email, password)
            )
            println("Response: $response")
            println("Response body: ${response.body()}")
            loginResult.value = response.body()
            loginErrorStatus.value = ""
            println("Login result: ${loginResult.value}")
        } catch (e: Exception) {
            println("Error occurred: $e")
            loginResult.value = null
            if (e is retrofit2.HttpException) {
                loginErrorStatus.value = "Invalid username or password"
            } else {
                loginErrorStatus.value = "An error occurred"
            }
        } finally {
            loadingState.value = false
        }
}
