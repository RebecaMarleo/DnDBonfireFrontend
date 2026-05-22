package com.example.dndbonfire.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.dndbonfire.json
import com.example.dndbonfire.modelo.RespuestaError
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.urlBonfireApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class ProveedorUsuario {
    companion object {
        private lateinit var pref: SharedPreferences
        private var appContext: Context? = null
        private var _usuarioLogeado: Usuario? = null
        private var _mantenerseLogeado = false

        private var _usuarioDM: Usuario? = null

        fun inicializar(pref: SharedPreferences, context: Context) {
            appContext = context.applicationContext
            this.pref = pref
            _usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
        }

        fun cambiarEstadoMantenerseLogeado(mantenerseLogeado: Boolean) {
            _mantenerseLogeado = mantenerseLogeado
        }

        val usuarioLogeado: Usuario?
            get() = _usuarioLogeado

        val usuarioDM: Usuario?
            get() = _usuarioDM

        suspend fun logearUsuario(username: String, contrasena: String): String {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL("$urlBonfireApi/api/usuario/login")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000

                    val params = "username=$username&contrasena=$contrasena"
                    connection.outputStream.use {
                        it.write(params.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        _usuarioLogeado = Json.decodeFromString<Usuario>(responseJson)
                        if (_mantenerseLogeado) {
                            DatosUsuario.guardarUsuarioLogeado(pref, _usuarioLogeado)
                        }
                        val token = FirebaseMessaging.getInstance().token.await()
                        ProveedorTokenDispositivo.clonarTokens(token)
                        ProveedorTokenDispositivo.activarTokens(token, _usuarioLogeado!!, true)
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        _usuarioLogeado = null
                        // si se ha llegado a este punto con credenciales vacíos es porque se está cerrando sesión
                        // en este caso no debe mostrar el diálogo de error así que devuelve una cadena vacía
                        if (username.isEmpty() && contrasena.isEmpty()) {
                            DatosUsuario.guardarUsuarioLogeado(pref, null)
                            val usuarioMal = DatosUsuario.cargarUsuarioLogeado(pref)
                            ""
                        } else {
                            "No se encontró un usuario con esas credenciales."
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _usuarioLogeado = null
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }

        suspend fun crearUsuario(nombre: String, username: String, contrasena: String): String {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL("$urlBonfireApi/api/usuario/crear")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")

                    // el id se sobreescribe después por un número incremental y el rango hay que ponerlo en 1 que es el nivel 1
                    val body = """{
                        "id": 0,
                        "nombre": "$nombre",
                        "username": "$username",
                        "contrasena": "$contrasena",
                        "rango": 1
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 201) {
                        "Exito"
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        _usuarioLogeado = null
                        val responseJson = connection.errorStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val errorResponse = json.decodeFromString<RespuestaError>(responseJson)
                        errorResponse.error
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _usuarioLogeado = null
                    "Se ha producido un error al conectarse al servidor, vuelve a intentarlo más tarde."
                }
            }
        }

        suspend fun modificarUsuario(nombre: String?, username: String?, contrasena: String?, rango: Int, imagen: String?): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/usuario/modificar/${_usuarioLogeado?.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val nombreAjuste = if (nombre != "") "\"$nombre\"" else null
                    val usernameAjuste = if (username != "") "\"$username\"" else null
                    val contrasenaAjuste = if (contrasena != "") "\"$contrasena\"" else null
                    val imagenAjuste = if (imagen != null) "\"$imagen\"" else null
                    val tokenAjuste = if (_usuarioLogeado?.token != "") "\"${_usuarioLogeado?.token}\"" else null
                    // TODO cuando tenga hecho el "sistema de experiencia" cambiar el tema del rango
                    val body = """{
                        "id": ${_usuarioLogeado?.id},
                        "nombre": $nombreAjuste,
                        "username": $usernameAjuste,
                        "contrasena": $contrasenaAjuste,
                        "rango": $rango,
                        "token": $tokenAjuste,
                        "imagen": $imagenAjuste
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        _usuarioLogeado = json.decodeFromString<Usuario>(responseJson)
                        // si había datos en el shared preferences es porque el usuario tiene que guardarse después de que
                        // se cierre la aplicación así que hay que actualizar eso también
                        if (DatosUsuario.cargarUsuarioLogeado(pref)?.username != null) {
                            DatosUsuario.guardarUsuarioLogeado(pref, _usuarioLogeado)
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

        suspend fun eliminarUsuario(id: Int): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/usuario/eliminar/${_usuarioLogeado?.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "DELETE"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        _usuarioLogeado = null
                        // si había datos en el shared preferences es porque el usuario tiene que guardarse después de que
                        // se cierre la aplicación así que hay que actualizar eso también
                        if (DatosUsuario.cargarUsuarioLogeado(pref)?.username != null) {
                            DatosUsuario.guardarUsuarioLogeado(pref, _usuarioLogeado)
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

        suspend fun comprobarToken(): Usuario? {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL("$urlBonfireApi/api/usuario/renovartoken")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")

                    val nombreAjuste = if (_usuarioLogeado?.nombre != "") "\"${_usuarioLogeado?.nombre}\"" else null
                    val usernameAjuste = if (_usuarioLogeado?.username != "") "\"${_usuarioLogeado?.username}\"" else null
                    val tokenAjuste = if (_usuarioLogeado?.token != "") "\"${_usuarioLogeado?.token}\"" else null
                    // el id se sobreescribe después por un número incremental y el rango hay que ponerlo en 1 que es el nivel 1
                    val body = """{
                        "id": ${_usuarioLogeado?.id},
                        "nombre": $nombreAjuste,
                        "username": $usernameAjuste,
                        "rango": 1,
                        "token": $tokenAjuste
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 201) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        _usuarioLogeado = json.decodeFromString<Usuario>(responseJson)
                        if (_mantenerseLogeado) {
                            DatosUsuario.guardarUsuarioLogeado(pref, _usuarioLogeado)
                        }
                    } else if (responseCode == 200) {
                        // si el código es 200 no tiene que cambiar nada
                    } else {
                        Log.e("ERROR", "Código de respuesta: $responseCode")
                        _usuarioLogeado = null
                    }
                    _usuarioLogeado
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _usuarioLogeado = null
                    null
                }
            }
        }

        suspend fun cargarUsuarioDM(id: Int?) {
            return withContext(Dispatchers.IO) {
                try {
                    if (id == null) {
                        _usuarioDM = null
                    } else {
                        _usuarioLogeado = comprobarToken()

                        val url = URL("$urlBonfireApi/api/usuario/cargar/$id")

                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 2000
                        connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                        val responseCode = connection.responseCode

                        if (responseCode == 200) {
                            val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                            _usuarioDM = Json.decodeFromString<Usuario>(responseJson)
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