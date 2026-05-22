package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Sesion(
    val id: Long,
    val fecha: String,
    val descripcion: String?,
    val jugadoresApuntados: MutableList<Int>,
    val idPartida: Int
) {
}