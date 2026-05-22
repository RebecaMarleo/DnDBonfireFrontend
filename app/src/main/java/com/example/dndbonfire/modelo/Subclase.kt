package com.example.dndbonfire.modelo

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subclase(
    val index: String,
    @SerialName("class") val clase: ResumenClase,
    val name: String,
    val subclass_flavor: String,
    val desc: List<String>,
    val subclass_levels: String,
    val url: String
) {
}