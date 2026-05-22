package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class RespuestaError(
    val error: String
)