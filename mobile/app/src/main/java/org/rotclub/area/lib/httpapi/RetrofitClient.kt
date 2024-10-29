package org.rotclub.area.lib.httpapi

import org.rotclub.area.lib.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

object RetrofitClient {
    val authApi: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}

interface Api {
    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>


    @GET("api/services")
    suspend fun getServices(@Header("Authorization") token: String): Response<List<Service>>

    @GET("api/user")
    suspend fun apiGetUser(@Header("Authorization") token: String): Response<UserResponse>
}
