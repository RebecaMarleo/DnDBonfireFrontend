package com.example.dndbonfire.util

import android.os.Build
import android.util.Log
import java.time.LocalDate
import java.util.regex.Matcher
import java.util.regex.Pattern

object Validar {
    // nombre de usuario entre 3 y 20 caracteres
    // acepta mayúsculas, minúsculas, números y barras bajas
    fun validarUsername(username: String?): Boolean {
        val regex = "^[A-Za-z0-9_]{3,20}$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(username)

        return matcher.matches()
    }

    // contraseña entre 6 y 30 caracteres
    // debe contener una mayúscula
    // debe contener una minúscula
    // debe contener un número
    // debe contener un caracter especial
    fun validarContrasena(contrasena: String?): Boolean {
        val regex =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~]{6,30}$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(contrasena)

        return matcher.matches()
    }

    // las contraseñas deben coincidir
    fun validarRecontrasena(contrasena: String?, recontrasena: String?): Boolean {
        return contrasena == recontrasena
    }

    // la cantidad máxima de jugadores no puede ser menor a la cantidad mínima
    fun validarNumJugadores(numMinJugadores: Int, numMaxJugadores: Int): Boolean {
        return numMinJugadores <= numMaxJugadores
    }

    // el rango máximo de jugadores no puede ser menor al rango mínimo
    fun validarRangoJugadores(rangoMinJugadores: Int, rangoMaxJugadores: Int): Boolean {
        return rangoMinJugadores <= rangoMaxJugadores
    }

    // la primera fecha es inferior a la segunda fecha
    // ejemplo: la fecha de inicio no puede ser inferior a la fecha de finalización
    fun validarFechas(fecha1: Long, fecha2: Long): Boolean {
        return fecha1 < fecha2
    }

    // si la partida está finalizada la fecha de finalización no puede ser anterior a hoy
    fun validarFinalizada(fechaFinalizacion: Long?, finalizada: Boolean): Boolean {
        var noFinalizada = false
        if (fechaFinalizacion == null && finalizada) {
            noFinalizada = true
        } else if (fechaFinalizacion != null && finalizada) {
            if (fechaFinalizacion > System.currentTimeMillis()) {
                noFinalizada = true
            }
        }
        return !noFinalizada
    }

    // el texto no puede exceder los x caracteres
    fun validarTexto(texto: String, caracteres: Int): Boolean {
        return texto.length <= caracteres
    }
}
