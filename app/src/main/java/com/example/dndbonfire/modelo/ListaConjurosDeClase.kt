package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class ListaConjurosDeClase(
    val count: Int,
    val results: List<ResumenConjuroDeClase>
)

@Serializable
data class ResumenConjuroDeClase(
    val index: String,
    val name: String,
    val level: Int,
    val url: String
)