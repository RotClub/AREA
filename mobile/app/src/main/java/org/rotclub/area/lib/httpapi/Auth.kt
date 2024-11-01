package org.rotclub.area.lib.httpapi

import androidx.compose.runtime.MutableState
import com.google.gson.Gson
import org.rotclub.area.lib.user.Role

data class LoginRequest(
    val email: String,
    val password: String,
)

data class LoginResponse(
    val token: String,
    val role: Role,
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: Role
)

data class RegisterResponse(
    val user: RegisterUserResponse?,
    val error: String?,
)

data class RegisterUserResponse(
    val id: Int,
    val email: String,
    val role: Role,
    val token: String,
    val hashedPassword: String,
    val createdAt: String,
    val username: String,
)

data class ErrorResponse(
    val error: String,
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
            val response = RetrofitClient.authApi.login(
                LoginRequest(email, password)
            )
            val errorResponse: ErrorResponse? = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
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
                    loginErrorStatus.value = errorResponse?.error ?: "An error occurred"
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

suspend fun authRegister(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    loadingState: MutableState<Boolean>,
    registerResult: MutableState<RegisterResponse?>,
    registerErrorStatus: MutableState<String>
) {
    if (password != confirmPassword) {
        registerErrorStatus.value = "Passwords do not match"
        return
    }
    loadingState.value = true
    try {
        val response = RetrofitClient.authApi.register(
            RegisterRequest(username, email, password, Role.USER)
        )
        val errorResponse: ErrorResponse? = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
        when (response.code()) {
            200 -> {
                registerResult.value = response.body()
                registerErrorStatus.value = ""
                return
            }
            else -> {
                registerResult.value = null
                registerErrorStatus.value = errorResponse?.error ?: "An error occurred"
                return
            }
        }
    } catch (e: Exception) {
        println("Error occurred: $e")
        registerResult.value = null
        registerErrorStatus.value = "An error occurred"
    } finally {
        loadingState.value = false
    }
}
