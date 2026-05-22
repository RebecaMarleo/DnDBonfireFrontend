package com.example.dndbonfire.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.dndbonfire.data.ProveedorUsuario.Companion.comprobarToken
import com.example.dndbonfire.json
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.RespuestaError
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.urlBonfireApi
import com.example.dndbonfire.util.ConversorUnix
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class ProveedorPartida {
    companion object {
        private var appContext: Context? = null
        private lateinit var pref: SharedPreferences
        private var _usuarioLogeado: Usuario? = null

        private var _partidasPropias: MutableList<Partida>? = null
        private var _partidasUnidas: MutableList<Partida>? = null
        private var _partidasDisponibles: MutableList<Partida>? = null
        private var _sinPartidas: Boolean = false
        private var _partida: Partida? = null

        private var traductorInglesEspanol: Translator? = null

        fun inicializar(pref: SharedPreferences, context: Context) {
            appContext = context.applicationContext
            this.pref = pref
            _usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
            inicializarTraductor()
        }

        val partidasPropias: MutableList<Partida>
            get() = _partidasPropias ?: mutableListOf()
        val partidasUnidas: MutableList<Partida>
            get() = _partidasUnidas ?: mutableListOf()
        val partidasDisponibles: MutableList<Partida>
            get() = _partidasDisponibles ?: mutableListOf()
        val sinPartidas: Boolean
            get() = _sinPartidas

        val partida: Partida?
            get() = _partida

        private fun inicializarTraductor() {
            val opciones = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build()
            traductorInglesEspanol = Translation.getClient(opciones)
        }

        suspend fun crearPartida(titulo: String, descripcion: String, numMinJugadores: Int, numMaxJugadores: Int, rangoMinJugadores: Int, rangoMaxJugadores: Int, fechaInicio: Long?, fechaFinalizacion: Long?, finalizada: Boolean, privada: Boolean, infoVisible: Boolean): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partida/crear")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val tituloAjuste = if (titulo != "") "\"$titulo\"" else null
                    val descripcionAjuste = if (descripcion != "") "\"$descripcion\"" else null
                    val numMinJugadoresAjuste = if (numMinJugadores != 0) numMinJugadores else null
                    val numMaxJugadoresAjuste = if (numMaxJugadores != 0) numMaxJugadores else null
                    val rangoMinJugadoresAjuste = if (rangoMinJugadores != 0) rangoMinJugadores else null
                    val rangoMaxJugadoresAjuste = if (rangoMaxJugadores != 0) rangoMaxJugadores else null
                    val fechaInicioAjuste = if (fechaInicio != null) "\"${ConversorUnix.milisAFechaYMD(fechaInicio)}\"" else null
                    val fechaFinalizacionAjuste = if (fechaFinalizacion != null) "\"${ConversorUnix.milisAFechaYMD(fechaFinalizacion)}\"" else null
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "id": 0,
                        "titulo": $tituloAjuste,
                        "descripcion": $descripcionAjuste,
                        "numMinJugadores": $numMinJugadoresAjuste,
                        "numMaxJugadores": $numMaxJugadoresAjuste,
                        "rangoMinJugadores": $rangoMinJugadoresAjuste,
                        "rangoMaxJugadores": $rangoMaxJugadoresAjuste,
                        "fechaInicio": $fechaInicioAjuste,
                        "fechaFinalizacion": $fechaFinalizacionAjuste,
                        "finalizada": $finalizada,
                        "privada": $privada,
                        "infoVisible": $infoVisible,
                        "idDM": ${_usuarioLogeado?.id}
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 201) {
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

        suspend fun eliminarPartida(id: Int): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partida/eliminar/$id")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "DELETE"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

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

        suspend fun modificarPartida(id: Int, titulo: String, descripcion: String, numMinJugadores: Int, numMaxJugadores: Int, rangoMinJugadores: Int, rangoMaxJugadores: Int, fechaInicio: Long?, fechaFinalizacion: Long?, finalizada: Boolean, privada: Boolean, infoVisible: Boolean): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partida/modificar/$id")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val tituloAjuste = if (titulo != "") "\"$titulo\"" else null
                    val descripcionAjuste = if (descripcion != "") "\"$descripcion\"" else null
                    val numMinJugadoresAjuste = if (numMinJugadores != 0) numMinJugadores else null
                    val numMaxJugadoresAjuste = if (numMaxJugadores != 0) numMaxJugadores else null
                    val rangoMinJugadoresAjuste = if (rangoMinJugadores != 0) rangoMinJugadores else null
                    val rangoMaxJugadoresAjuste = if (rangoMaxJugadores != 0) rangoMaxJugadores else null
                    val fechaInicioAjuste = if (fechaInicio != null) "\"${ConversorUnix.milisAFechaYMD(fechaInicio)}\"" else null
                    val fechaFinalizacionAjuste = if (fechaFinalizacion != null) "\"${ConversorUnix.milisAFechaYMD(fechaFinalizacion)}\"" else null
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "id": $id,
                        "titulo": $tituloAjuste,
                        "descripcion": $descripcionAjuste,
                        "numMinJugadores": $numMinJugadoresAjuste,
                        "numMaxJugadores": $numMaxJugadoresAjuste,
                        "rangoMinJugadores": $rangoMinJugadoresAjuste,
                        "rangoMaxJugadores": $rangoMaxJugadoresAjuste,
                        "fechaInicio": $fechaInicioAjuste,
                        "fechaFinalizacion": $fechaFinalizacionAjuste,
                        "finalizada": $finalizada,
                        "privada": $privada,
                        "infoVisible": $infoVisible,
                        "idDM": ${_usuarioLogeado?.id}
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        _partida = json.decodeFromString<Partida>(responseJson)
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

        suspend fun cargarPartidas(usuario: Usuario) {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/partida/cargar/porUsuario/${usuario.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val lista = Json.decodeFromString<Map<String, MutableList<Partida>>>(responseJson)
                        _partidasPropias = lista["partidasPropias"]
                        _partidasUnidas = lista["partidasUnidas"]
                        _partidasDisponibles = lista["partidasDisponibles"]
                        _sinPartidas = lista["partidasPropias"]!!.isEmpty() && lista["partidasUnidas"]!!.isEmpty() && lista["partidasDisponibles"]!!.isEmpty()
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

        suspend fun cargarPartida(id: Int?) {
            return withContext(Dispatchers.IO) {
                try {
                    if (id == null) {
                        _partida = null
                    } else {
                        _usuarioLogeado = comprobarToken()

                        val url = URL("$urlBonfireApi/api/partida/cargar/$id")

                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 2000
                        connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                        val responseCode = connection.responseCode

                        if (responseCode == 200) {
                            val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                            _partida = Json.decodeFromString<Partida>(responseJson)
                            "Exito"
                        } else {
                            Log.e("ERROR", "Código de respuesta: $responseCode")
                            val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                            val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                            errorResponse.error
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }
    }
}