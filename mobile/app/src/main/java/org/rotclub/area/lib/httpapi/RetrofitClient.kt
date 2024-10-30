package org.rotclub.area.lib.httpapi

import org.rotclub.area.lib.baseUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

object RetrofitClient {
    var authApi: Api = Retrofit.Builder()
        .baseUrl(baseUrl.value)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    fun changeBaseUrl(newBaseUrl: String) {
        authApi = Retrofit.Builder()
            .baseUrl(newBaseUrl)
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

    @GET
    suspend fun apiGetServiceOauth(@Url url: String, @Header("Authorization") token: String): Response<ServiceOauthResponse>

    @GET
    suspend fun apiUnlinkService(@Url url: String, @Header("Authorization") token: String): Response<ServiceOauthResponse>
}
