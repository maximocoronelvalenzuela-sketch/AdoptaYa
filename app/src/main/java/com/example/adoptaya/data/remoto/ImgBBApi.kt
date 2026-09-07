package com.example.adoptaya.data.remoto

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

data class ImgBBResponse(val data: ImgBBData)
data class ImgBBData(val url: String)

interface ImgBBApi {
    @Multipart
    @POST("1/upload")
    suspend fun subirImagen(
        @Query("key") apiKey: String = "1d14e578bf0c20137526a98aa73a0f1c",
        @Part image: okhttp3.MultipartBody.Part
    ): retrofit2.Response<ImgBBResponse>
}