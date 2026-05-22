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
import com.example.dndbonfire.util.ConversorUnix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class ProveedorSesion {
    companion object {
        private var appContext: Context? = null
        private lateinit var pref: SharedPreferences
        private var _usuarioLogeado: Usuario? = null

        private var _sesionesProgramadas: MutableList<Sesion>? = null
        private var _sesionesPasadas: MutableList<Sesion>? = null
        private var _sinSesiones: Boolean = false

        private var _sesionesHoy: MutableList<Sesion>? = null
        private var _sesionesEstaSemana: MutableList<Sesion>? = null
        private var _sesionesEsteMes: MutableList<Sesion>? = null
        private var _sesionesMasDeMes: MutableList<Sesion>? = null
        private var _sinSesionesCalendario: Boolean = false

        fun inicializar(pref: SharedPreferences, context: Context) {
            appContext = context.applicationContext
            this.pref = pref
            _usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
        }

        val sesionesProgramadas: MutableList<Sesion>
            get() = _sesionesProgramadas ?: mutableListOf()
        val sesionesPasadas: MutableList<Sesion>
            get() = _sesionesPasadas ?: mutableListOf()
        val sinSesiones: Boolean
            get() = _sinSesiones

        val sesionesHoy: MutableList<Sesion>
            get() = _sesionesHoy ?: mutableListOf()
        val sesionesEstaSemana: MutableList<Sesion>
            get() = _sesionesEstaSemana ?: mutableListOf()
        val sesionesEsteMes: MutableList<Sesion>
            get() = _sesionesEsteMes ?: mutableListOf()
        val sesionesMasDeMes: MutableList<Sesion>
            get() = _sesionesMasDeMes ?: mutableListOf()
        val sinSesionesCalendario: Boolean
            get() = _sinSesionesCalendario

        suspend fun crearSesion(fecha: Long, descripcion: String, jugadoresApuntados: List<Int>, partida: Partida): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/sesion/crear")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val fechaAjuste = "\"${ConversorUnix.milisAFechaYMDHMS(fecha)}\""
                    val descripcionAjuste = if (descripcion != "") "\"$descripcion\"" else null
                    val jugadoresApuntadosAjuste = jugadoresApuntados.map { "\"$it\"" }
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "id": 0,
                        "fecha": $fechaAjuste,
                        "descripcion": $descripcionAjuste,
                        "jugadoresApuntados": $jugadoresApuntadosAjuste,
                        "idPartida": ${partida.id}
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

        suspend fun eliminarSesion(id: Long): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/sesion/eliminar/$id")

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

        suspend fun eliminarSesionesMultiples(idsRelaciones: List<Long>): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val query = idsRelaciones.joinToString("&") { "id=$it" }
                    val url = URL("$urlBonfireApi/api/sesion/eliminarMultiples?$query")

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

        suspend fun modificarSesion(sesion: Sesion, fecha: Long? = null, descripcion: String? = null): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/sesion/modificar/${sesion.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val fechaAjuste =
                        if (fecha != null) {
                            "\"${ConversorUnix.milisAFechaYMDHMS(fecha)}\""
                        } else
                            "\"${ConversorUnix.milisAFechaYMDHMS(ConversorUnix.fechaYMDHMSConTAMilis(sesion.fecha))}\""
                    val descripcionAjuste =
                        if (descripcion != null) {
                            if (descripcion != "")
                                "\"$descripcion\""
                            else null
                        } else {
                            if (sesion.descripcion != null)
                                "\"${sesion.descripcion}\""
                            else null
                        }

                    val jugadoresApuntadosAjuste = sesion.jugadoresApuntados.map { "\"$it\"" }
                    val body = """{
                        "id": ${sesion.id},
                        "fecha": $fechaAjuste,
                        "descripcion": $descripcionAjuste,
                        "jugadoresApuntados": $jugadoresApuntadosAjuste,
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

        suspend fun cargarSesiones(partida: Partida) {
            _sesionesProgramadas?.clear()
            _sesionesPasadas?.clear()

            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/sesion/cargar/porPartida/${partida.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val lista = Json.decodeFromString<Map<String, MutableList<Sesion>>>(responseJson)
                        _sesionesProgramadas = lista["sesionesProgramadas"]
                        _sesionesPasadas = lista["sesionesPasadas"]
                        _sinSesiones = lista["sesionesProgramadas"]!!.isEmpty() && lista["sesionesPasadas"]!!.isEmpty()
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

        suspend fun cargarSesionesCalendario(usuario: Usuario) {
            _sesionesHoy?.clear()
            _sesionesEstaSemana?.clear()
            _sesionesEsteMes?.clear()
            _sesionesMasDeMes?.clear()

            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/sesion/cargar/porUsuario/${usuario.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val lista = Json.decodeFromString<Map<String, MutableList<Sesion>>>(responseJson)
                        _sesionesHoy = lista["sesionesHoy"]
                        _sesionesEstaSemana = lista["sesionesEstaSemana"]
                        _sesionesEsteMes = lista["sesionesEsteMes"]
                        _sesionesMasDeMes = lista["sesionesMasDeMes"]
                        _sinSesionesCalendario = lista["sesionesHoy"]!!.isEmpty() && lista["sesionesEstaSemana"]!!.isEmpty() && lista["sesionesEsteMes"]!!.isEmpty() && lista["sesionesMasDeMes"]!!.isEmpty()
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