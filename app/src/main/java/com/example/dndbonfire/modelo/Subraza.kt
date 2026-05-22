package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Subraza(
    val index: String,
    val name: String,
    val race: ResumenRaza,
    val desc: String,
    val ability_bonuses: List<BonoDeHabilidad>,
    val racial_traits: List<Rasgo>,
    val url: String
) {
}