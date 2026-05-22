package com.example.dndbonfire.modelo

import android.graphics.Bitmap
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val nombre: String,
    val username: String,
    val rango: Int,
    val token: String? = null,
    val imagen: String? = null,
)