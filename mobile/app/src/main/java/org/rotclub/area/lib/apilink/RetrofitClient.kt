package org.rotclub.area.lib.apilink

import org.rotclub.area.lib.baseUrl
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
import retrofit2.http.Url

object RetrofitClient {
    var authApi: Api = Retrofit.Builder()
        .baseUrl(baseUrl.value)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    suspend fun changeBaseUrl(newBaseUrl: String): Boolean {
        println("Changing base url to $newBaseUrl")
        try {
            authApi = Retrofit.Builder()
                .baseUrl(newBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
            baseUrl.value = newBaseUrl
            val res = authApi.testConnection()
            if (!res.isSuccessful) {
                println("Error changing base url: ${res.errorBody()?.string()}")
                return false
            }
            return true
        } catch (e: Exception) {
            println("Error changing base url: $e")
            return false
        }
    }
}

interface Api {
    @GET("/")
    suspend fun testConnection(): Response<Unit>

    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

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
    @PUT("api/programs/{inspecting_node}/node")
    suspend fun apiPutAction(@Header("Authorization") token: String, @Path("inspecting_node") inspectingNode: Int, @Body request: NewActionIdRequest): Response<Unit>
    @PUT("api/programs/{inspecting_node}/node")
    suspend fun apiPutReaction(@Header("Authorization") token: String, @Path("inspecting_node") inspectingNode: Int, @Body request: NewReactionIdRequest): Response<Unit>

    // Nodes
    @HTTP(method = "DELETE", path = "api/programs/{programId}/node", hasBody = true)
    suspend fun apiDeleteAction(@Header("Authorization") token: String, @Path("programId") programId: Int, @Body request: ActionIdRequest): Response<Unit>

    // Actions
    @GET("api/action")
    suspend fun apiGetAccesibleActions(@Header("Authorization") token: String): Response<List<NodeType>>

    @GET("api/user")
    suspend fun apiGetUser(@Header("Authorization") token: String): Response<UserResponse>

    @GET
    suspend fun apiGetServiceOauth(@Url url: String, @Header("Authorization") token: String): Response<ServiceOauthResponse>

    @GET
    suspend fun apiUnlinkService(@Url url: String, @Header("Authorization") token: String): Response<ServiceOauthResponse>
}
