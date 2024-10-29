package org.rotclub.area.lib.httpapi

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.HTTP

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

    // Programs
    @GET("api/programs")
    suspend fun apiGetPrograms(@Header("Authorization") token: String): Response<List<ProgramResponse>>
    @POST("api/programs")
    suspend fun apiPostProgram(@Header("Authorization") token: String, @Body programRequest: ProgramRequest): Response<ProgramResponse>
    @DELETE("api/programs/{id}")
    suspend fun apiDeleteProgram(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>
    @PATCH("api/programs/{id}")
    suspend fun apiPatchProgramName(@Header("Authorization") token: String, @Path("id") id: Int, @Body programRequest: ProgramRequest): Response<ProgramResponse>

    // Nodes
    @HTTP(method = "DELETE", path = "api/programs/{programId}/node", hasBody = true)
    suspend fun apiDeleteAction(@Header("Authorization") token: String, @Path("programId") programId: Int, @Body request: ActionIdRequest): Response<Unit>

    // Actions
    @GET("api/action")
    suspend fun apiGetAccesibleActions(@Header("Authorization") token: String): Response<List<NodeType>>

    @GET("api/user")
    suspend fun apiGetUser(@Header("Authorization") token: String): Response<UserResponse>
}
