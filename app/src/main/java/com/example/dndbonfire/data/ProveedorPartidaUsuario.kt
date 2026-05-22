package com.example.dndbonfire.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.dndbonfire.data.ProveedorPersonaje.Companion.traduccionesClases
import com.example.dndbonfire.data.ProveedorPersonaje.Companion.traduccionesRazas
import com.example.dndbonfire.data.ProveedorUsuario.Companion.comprobarToken
import com.example.dndbonfire.json
import com.example.dndbonfire.modelo.Aventurero
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.modelo.RespuestaError
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.urlBonfireApi
import com.google.firebase.messaging.FirebaseMessaging
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class ProveedorPartidaUsuario {
    companion object{
        private var appContext: Context? = null
        private lateinit var pref: SharedPreferences
        private var _usuarioLogeado: Usuario? = null

        private var _aventureros: MutableMap<Usuario, Personaje>? = null
        private var _sinAventureros: Boolean = false

        private var traductorInglesEspanol: Translator? = null

        fun inicializar(pref: SharedPreferences, context: Context) {
            appContext = context.applicationContext
            this.pref = pref
            _usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
            inicializarTraductor()
        }

        val aventureros: MutableMap<Usuario, Personaje>
            get() = _aventureros ?: mutableMapOf()
        val sinAventureros: Boolean
            get() = _sinAventureros

        private fun inicializarTraductor() {
            val opciones = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build()
            traductorInglesEspanol = Translation.getClient(opciones)
        }

        suspend fun traducirDatosAventureros(
            aventureros: Map<Usuario, Personaje>
        ): MutableMap<Usuario, Personaje> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                aventureros.map { (usuario, personaje) ->
                    usuario to Personaje(
                        id = personaje.id,
                        nombre = personaje.nombre ?: "Sin nombre",
                        raza = traduccionesRazas[personaje.raza] ?: "Sin raza",
                        subraza = traduccionesRazas[personaje.subraza] ?: "Sin subraza",
                        hp = personaje.hp,
                        hpAuto = personaje.hpAuto,
                        fuerza = personaje.fuerza,
                        destreza = personaje.destreza,
                        constitucion = personaje.constitucion,
                        inteligencia = personaje.inteligencia,
                        sabiduria = personaje.sabiduria,
                        carisma = personaje.carisma,
                        edad = personaje.edad,
                        idiomaExtra = personaje.idiomaExtra,
                        alineamiento = personaje.alineamiento,
                        clase = traduccionesClases[personaje.clase] ?: "Sin clase",
                        subclase = traduccionesClases[personaje.subclase] ?: "Sin subclase",
                        nivel = personaje.nivel,
                        competenciasSeleccionadas = personaje.competenciasSeleccionadas,
                        trucosConocidos = personaje.trucosConocidos,
                        conjurosConocidos = personaje.conjurosConocidos,
                        imagen = personaje.imagen,
                        idUsuario = personaje.idUsuario
                    )
                }
                    .sortedBy { (_, personaje) ->
                        if (personaje.nombre == "Sin nombre") "ZZZZZZ"
                        else personaje.nombre
                    }
                    .toMap()
                    .toMutableMap()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                aventureros.toMutableMap()
            }
        }

        suspend fun crearRelacionPartidaUsuario(personaje: Personaje, partida: Partida): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partidausuario/crear")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val body = """{
                        "idPartida": ${partida.id},
                        "idUsuario": ${_usuarioLogeado!!.id},
                        "idPersonaje": ${personaje.id}
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 201) {
                        // si el usuario no ha seleccionado mantener la sesión iniciada no registra su token ya que las notificaciones solo se reciben cuando la app esta cerrada
                        if (DatosUsuario.cargarUsuarioLogeado(pref)?.username != null) {
                            val token = FirebaseMessaging.getInstance().token.await()
                            ProveedorTokenDispositivo.crearToken(partida.id, token)
                        }
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                        errorResponse.error
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }

        suspend fun eliminarRelacionPartidaUsuario(partida: Partida, id: Int): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partidausuario/eliminar/${partida.id}/$id")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "DELETE"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        ProveedorTokenDispositivo.eliminarToken(partida, id)
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                        errorResponse.error
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }

        suspend fun eliminarRelacionesPartidaUsuarioMultiples(partida: Partida, idsRelaciones: List<Int>): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val query = idsRelaciones.joinToString("&") { "id=$it" }
                    val url = URL("$urlBonfireApi/api/partidausuario/eliminarMultiples/${partida.id}?$query")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "DELETE"
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        ProveedorTokenDispositivo.eliminarTokensMultiples(partida, idsRelaciones)
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                        errorResponse.error
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }

        suspend fun modificarRelacionPartidaUsuario(personaje: Personaje, partida: Partida): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partidausuario/modificar")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val body = """{
                        "idPartida": ${partida.id},
                        "idUsuario": ${_usuarioLogeado!!.id},
                        "idPersonaje": ${personaje.id}
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                        errorResponse.error
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }

        suspend fun cargarAventureros(partida: Partida) {
            _aventureros?.clear()

            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partidausuario/cargar/porPartida/${partida.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val lista = Json.decodeFromString<MutableList<Aventurero>>(responseJson)
                        _aventureros = mutableMapOf()
                        lista.forEach {
                            _aventureros!![it.usuario] = it.personaje
                        }
                        _aventureros = traducirDatosAventureros(_aventureros!!)
                        _sinAventureros = lista.isEmpty()
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                        errorResponse.error
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }
    }
}