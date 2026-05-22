package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Aventurero(
    val usuario: Usuario,
    val personaje: Personaje
) {
}