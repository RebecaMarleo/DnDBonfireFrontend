package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Descripcion(
    val index: String,
    val desc: List<String>,
    val url: String
) {
}