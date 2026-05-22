package com.example.dndbonfire.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.dndbonfire.data.ProveedorUsuario.Companion.comprobarToken
import com.example.dndbonfire.json
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.RespuestaError
import com.example.dndbonfire.modelo.Sesion
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.urlBonfireApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class ProveedorTokenDispositivo {
    companion object {
        private var appContext: Context? = null
        private lateinit var pref: SharedPreferences
        private var _usuarioLogeado: Usuario? = null

        fun inicializar(pref: SharedPreferences, context: Context) {
            appContext = context.applicationContext
            this.pref = pref
            _usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
        }

        suspend fun crearToken(idPartida: Int, token: String): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/tokendispositivopartida/crear")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val tokenAjuste = if (token != "") "\"$token\"" else null
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "token": $tokenAjuste,
                        "activo": true,
                        "idUsuario": ${_usuarioLogeado!!.id},
                        "idPartida": $idPartida
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

        suspend fun clonarTokens(token: String): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/tokendispositivopartida/clonar/${_usuarioLogeado!!.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val tokenAjuste = if (token != "") "\"$token\"" else null
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "token": $tokenAjuste,
                        "activo": true,
                        "idUsuario": ${_usuarioLogeado!!.id},
                        "idPartida": 0
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

        suspend fun eliminarToken(partida: Partida, idUsuario: Int) {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/tokendispositivopartida/eliminar/${partida.id}/$idUsuario")

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

        suspend fun eliminarTokensMultiples(partida: Partida, idsRelaciones: List<Int>) {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val query = idsRelaciones.joinToString("&") { "id=$it" }
                    val url = URL("$urlBonfireApi/api/tokendispositivopartida/eliminarMultiples/${partida.id}?$query")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "DELETE"
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

        suspend fun activarTokens(token: String, usuario: Usuario, activar: Boolean) :String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/tokendispositivopartida/activar")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val tokenAjuste = if (token != "") "\"$token\"" else null
                    val body = """{
                        "token": $tokenAjuste,
                        "activo": $activar,
                        "idUsuario": ${usuario.id},
                        "idPartida": 0
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
    }
}