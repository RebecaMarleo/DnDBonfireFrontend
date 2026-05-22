package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class ListaIdiomas(
    val count: Int,
    val results: List<Idioma>
)