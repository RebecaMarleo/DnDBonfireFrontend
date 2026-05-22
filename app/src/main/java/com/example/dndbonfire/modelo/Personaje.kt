package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Personaje(
    val id: Int,
    val nombre: String? = null,
    val raza: String? = null,
    val subraza: String? = null,
    val hp: Int,
    val hpAuto: Boolean,
    val fuerza: Int,
    val destreza: Int,
    val constitucion: Int,
    val inteligencia: Int,
    val sabiduria: Int,
    val carisma: Int,
    val edad: Int,
    val idiomaExtra: String? = null,
    val alineamiento: String,
    val clase: String? = null,
    val subclase: String? = null,
    val nivel: Int,
    val competenciasSeleccionadas: List<List<String>>,
    val trucosConocidos: List<String>,
    val conjurosConocidos: List<String>,
    val imagen: String? = null,
    val idUsuario: Int
) {
}