package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.model.Mascota
import com.example.adoptaya.data.model.Notificacion
import com.example.adoptaya.data.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdoptayaApiService {

    // USUARIOS
    @GET("usuarios/{id}")
    suspend fun obtenerUsuarioPorId(@Path("id") id: String): Response<Usuario>

    @POST("usuarios")
    suspend fun guardarUsuario(@Body usuario: Usuario): Response<Usuario>


    // MASCOTAS
    @GET("mascotas")
    suspend fun obtenerMascotas(): Response<List<Mascota>>

    @GET("mascotas/{id}")
    suspend fun obtenerMascotaPorId(@Path("id") id: String): Response<Mascota>

    @POST("mascotas")
    suspend fun publicarMascota(@Body mascota: Mascota): Response<Mascota>

    @PUT("mascotas/{id}")
    suspend fun actualizarMascota(@Path("id") id: String, @Body mascota: Mascota): Response<Mascota>


    // FAVORITOS
    @GET("favoritos/{idUsuario}")
    suspend fun obtenerFavoritosPorUsuario(@Path("idUsuario") idUsuario: String): Response<List<String>>

    @POST("favoritos")
    suspend fun agregarFavorito(@Body favorito: Map<String, String>): Response<Unit>

    @DELETE("favoritos/{idUsuario}/{idMascota}")
    suspend fun eliminarFavorito(@Path("idUsuario") idUsuario: String, @Path("idMascota") idMascota: String): Response<Unit>


    // NOTIFICACIONES
    @GET("notificaciones/{idUsuario}")
    suspend fun obtenerNotificacionesPorUsuario(@Path("idUsuario") idUsuario: String): Response<List<Notificacion>>

    @POST("notificaciones")
    suspend fun guardarNotificacion(@Body notificacion: Notificacion): Response<Notificacion>

    @GET("notificaciones/sincronizar/{idUsuario}")
    suspend fun sincronizarNotificaciones(@Path("idUsuario") idUsuario: String): Response<List<Notificacion>>
}