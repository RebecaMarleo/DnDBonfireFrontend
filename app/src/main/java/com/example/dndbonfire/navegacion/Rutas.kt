package com.example.dndbonfire.navegacion

import kotlinx.serialization.Serializable

sealed class Ruta {
    @Serializable
    object Partidas: Ruta()

    @Serializable
    object CrearPartida: Ruta()

    @Serializable
    data class Partida(val idPartida: Int): Ruta()

    @Serializable
    object Personajes: Ruta()

    @Serializable
    data class Personaje(val idPersonaje: Int): Ruta()

    @Serializable
    object CrearPersonaje: Ruta()

    @Serializable
    object Bestiario: Ruta()

    @Serializable
    data class Bestia(val idBestia: String): Ruta()

    @Serializable
    object Calendario: Ruta()

    @Serializable
    object IniciarSesion: Ruta()

    @Serializable
    object CrearCuenta: Ruta()

    @Serializable
    object Cuenta: Ruta()
}