package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Bestiario(
    val count: Int,
    val results: List<ResumenBestia>
)

@Serializable
data class ResumenBestia(
    val index: String,
    val name: String,
    val url: String
)