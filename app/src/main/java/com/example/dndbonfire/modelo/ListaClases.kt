package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class ListaClases(
    val count: Int,
    val results: List<ResumenClase>
)

@Serializable
data class ResumenClase(
    val index: String,
    val name: String,
    val url: String
)