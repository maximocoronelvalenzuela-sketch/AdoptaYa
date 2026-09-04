package com.example.adoptaya.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun hayConexionAInternet(contexto: Context): Boolean {
    val connectivityManager = contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val redActiva = connectivityManager.activeNetwork ?: return false
    val capacidades = connectivityManager.getNetworkCapabilities(redActiva) ?: return false

    return capacidades.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capacidades.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capacidades.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}