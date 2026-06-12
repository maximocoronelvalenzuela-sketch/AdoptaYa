package com.example.adoptaya.data.remoto

import com.example.adoptaya.data.remoto.model.LocalidadesResponse
import com.example.adoptaya.data.remoto.model.ProvinciasResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GeorefApi {
    // Trae todas las provincias
    @GET("provincias?orden=nombre")
    suspend fun obtenerProvincias(): ProvinciasResponse

    // Trae localidades de una provincia
    @GET("localidades?max=1000&orden=nombre")
    suspend fun obtenerLocalidades(@Query("provincia") provinciaId: String): LocalidadesResponse

    companion object {
        private const val BASE_URL = "https://apis.datos.gob.ar/georef/api/"

        fun crear(): GeorefApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeorefApi::class.java)
        }
    }
}