package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Partida(
    val id: Int,
    val titulo: String? = null,
    val descripcion: String? = null,
    val numMinJugadores: Int? = null,
    val numMaxJugadores: Int? = null,
    val rangoMinJugadores: Int? = null,
    val rangoMaxJugadores: Int? = null,
    val fechaInicio: String? = null,
    val fechaFinalizacion: String? = null,
    val finalizada: Boolean,
    val privada: Boolean,
    val infoVisible: Boolean,
    val idDM: Int
) {
}