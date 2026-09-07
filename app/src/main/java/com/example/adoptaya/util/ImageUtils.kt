package com.example.adoptaya.util

import com.example.adoptaya.data.remoto.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

suspend fun subirFotoAImgBB(rutaArchivoLocal: String): String? {
    return try {
        val file = File(rutaArchivoLocal)
        if (!file.exists()) return null

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val response = RetrofitClient.imgBBApi.subirImagen(image = body)
        if (response.isSuccessful) response.body()?.data?.url else null
    } catch (e: Exception) {
        null
    }
}