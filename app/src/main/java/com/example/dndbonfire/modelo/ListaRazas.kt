package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class ListaRazas(
    val count: Int,
    val results: List<ResumenRaza>
)

@Serializable
data class ResumenRaza(
    val index: String,
    val name: String,
    val url: String
)