package com.example.adoptaya.data.remoto.model

data class ProvinciasResponse(
    val provincias: List<ProvinciaGeoref>
)

data class ProvinciaGeoref(
    val id: String,
    val nombre: String
)

data class LocalidadesResponse(
    val localidades: List<LocalidadGeoref>
)

data class LocalidadGeoref(
    val id: String,
    val nombre: String
)