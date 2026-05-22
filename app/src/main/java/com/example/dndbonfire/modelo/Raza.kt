package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Raza(
    val index: String,
    val name: String,
    val speed: Int,
    val ability_bonuses: List<BonoDeHabilidad>,
    val alignment: String,
    val age: String,
    val size: String,
    val size_description: String,
    val languages: List<Idioma>,
    val language_desc: String,
    val traits: List<Rasgo>,
    val subraces: List<ResumenSubraza>,
    val url: String
) {
}

@Serializable
data class BonoDeHabilidad(
    val ability_score: PuntajeHabilidad,
    val bonus: Int
)

@Serializable
data class PuntajeHabilidad(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class Idioma(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class Rasgo(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class ResumenSubraza(
    val index: String,
    val name: String,
    val url: String
)