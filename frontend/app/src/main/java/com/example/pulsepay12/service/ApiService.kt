package com.example.pulsepay12.service

import com.example.pulsepay12.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(val mail: String, val password: String)
data class LoginResponse(val token: String)
data class UpdateUserRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val permisoUbicacion: Boolean,
    val permisoNotificaciones: Boolean,
    val permisoTerceros: Boolean
)

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuario/actualizar")
    suspend fun actualizarUsuario(@Body datos: UpdateUserRequest): Response<Void>
}

