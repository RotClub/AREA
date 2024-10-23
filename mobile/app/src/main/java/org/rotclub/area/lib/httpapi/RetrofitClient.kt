package org.rotclub.area.lib.httpapi

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8081/"

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

    @GET("api/programs")
    suspend fun apiGetPrograms(@Header("Authorization") token: String): Response<List<ProgramResponse>>

    @POST("api/programs")
    suspend fun apiPostProgram(@Header("Authorization") token: String, @Body programRequest: ProgramRequest): Response<ProgramResponse>
}
