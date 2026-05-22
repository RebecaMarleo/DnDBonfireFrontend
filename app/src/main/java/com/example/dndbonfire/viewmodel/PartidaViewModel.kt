package com.example.dndbonfire.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorPartida
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.data.ProveedorSesion
import com.example.dndbonfire.data.ProveedorUsuario
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.Personaje
import kotlinx.coroutines.launch

class PartidaViewModel: ViewModel() {
    var partidasPropias = mutableStateListOf<Partida>()
        private set
    var partidasUnidas = mutableStateListOf<Partida>()
        private set
    var partidasDisponibles = mutableStateListOf<Partida>()
        private set

    private val _sinPartidas = mutableStateOf(false)
    val sinPartidas = _sinPartidas

    private val _partida = mutableStateOf<Partida?>(null)
    val partida: State<Partida?> = _partida

    private val _selectedIndex = mutableIntStateOf(0)
    val selectedIndex = _selectedIndex

    fun modificarSelectedIndex(index: Int) {
        _selectedIndex.value = index
    }

    private val _titulo = mutableStateOf("")
    val titulo = _titulo

    fun modificarTitulo(titulo: String) {
        _titulo.value = titulo
    }

    private val _descripcion = mutableStateOf("")
    val descripcion = _descripcion

    fun modificarDescripcion(descripcion: String) {
        _descripcion.value = descripcion
    }

    private val _numMinJugadores = mutableIntStateOf(0)
    val numMinJugadores = _numMinJugadores

    fun modificarNumMinJugadores(numMinJugadores: Int) {
        _numMinJugadores.intValue = numMinJugadores
    }

    private val _numMaxJugadores = mutableIntStateOf(0)
    val numMaxJugadores = _numMaxJugadores

    fun modificarNumMaxJugadores(numMaxJugadores: Int) {
        _numMaxJugadores.intValue = numMaxJugadores
    }

    private val _rangoMinJugadores = mutableIntStateOf(0)
    val rangoMinJugadores = _rangoMinJugadores

    fun modificarRangoMinJugadores(rangoMinJugadores: Int) {
        _rangoMinJugadores.intValue = rangoMinJugadores
    }

    private val _rangoMaxJugadores = mutableIntStateOf(0)
    val rangoMaxJugadores = _rangoMaxJugadores

    fun modificarRangoMaxJugadores(rangoMaxJugadores: Int) {
        _rangoMaxJugadores.intValue = rangoMaxJugadores
    }

    private val _tipoFecha = mutableStateOf("")
    val tipoFecha = _tipoFecha

    fun modificarTipoFecha(tipoFecha: String) {
        _tipoFecha.value = tipoFecha
    }

    private val _fechaInicio = mutableStateOf<Long?>(null)
    val fechaInicio = _fechaInicio

//    fun inicializarFechaInicio() {
//        // esta parte calcula la fecha unix para las 00:00 pero requiere api 26
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val hoy = java.time.LocalDate.now()
//            _fechaInicio.longValue = hoy.atStartOfDay(java.time.ZoneId.systemDefault())
//                .toInstant()
//                .toEpochMilli()
//        } else {
//            // si la api es 25 o menos toma la hora exacta actual
//            _fechaInicio.longValue = System.currentTimeMillis()
//        }
//    }

    fun modificarFechaInicio(fechaInicio: Long?) {
        _fechaInicio.value = fechaInicio
    }

    private val _fechaFinalizacion = mutableStateOf<Long?>(null)
    val fechaFinalizacion = _fechaFinalizacion

//    fun inicializarFechaFinalizacion() {
//        // esta parte calcula la fecha unix para las 00:00 pero requiere api 26
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val hoy = java.time.LocalDate.now()
//            val manana = hoy.plusDays(1)
//            _fechaFinalizacion.longValue = manana.atStartOfDay(java.time.ZoneId.systemDefault())
//                .toInstant()
//                .toEpochMilli()
//        } else {
//            // si la api es 25 o menos calcula 24 horas después
//            _fechaFinalizacion.longValue = System.currentTimeMillis() + 24 * 60 * 60 * 1000
//        }
//    }

    fun modificarFechaFinalizacion(fechaFinalizacion: Long?) {
        _fechaFinalizacion.value = fechaFinalizacion
    }

    private val _finalizada = mutableStateOf(false)
    val finalizada = _finalizada

    fun modificarFinalizada(finalizada: Boolean) {
        _finalizada.value = finalizada
    }

    private val _privada = mutableStateOf(false)
    val privada = _privada

    fun modificarPrivada(privada: Boolean) {
        _privada.value = privada
    }

    private val _infoVisible = mutableStateOf(false)
    val infoVisible = _infoVisible

    fun modificarInfoVisible(infoVisible: Boolean) {
        _infoVisible.value = infoVisible
    }

    init {
        cargarPartidas()
    }

    fun cargarPartidas() {
        partidasPropias.clear()
        partidasUnidas.clear()
        partidasDisponibles.clear()
        partidasPropias.addAll(ProveedorPartida.partidasPropias)
        partidasUnidas.addAll(ProveedorPartida.partidasUnidas)
        partidasDisponibles.addAll(ProveedorPartida.partidasDisponibles)
        sinPartidas.value = ProveedorPartida.sinPartidas
    }

    fun recargarPartidas() {
        viewModelScope.launch {
            try {
                partidasPropias.clear()
                partidasUnidas.clear()
                partidasDisponibles.clear()
                ProveedorPartida.cargarPartidas(ProveedorUsuario.usuarioLogeado!!)
                cargarPartidas()
            } catch (e: Exception) {}
        }
    }

    fun vaciarPartidas() {
        viewModelScope.launch {
            partidasPropias.clear()
            partidasUnidas.clear()
            partidasDisponibles.clear()
            _sinPartidas.value = false
        }
    }

    fun filtrarPartidas(textoBusqueda: String) {
        partidasPropias.clear()
        partidasUnidas.clear()
        partidasDisponibles.clear()

        // si la cadena recibida está vacía devuelve la lista completa
        if (textoBusqueda.isEmpty()) {
            cargarPartidas()
        } else {
            ProveedorPartida.partidasPropias.forEach { partida ->
                if ((partida.titulo != null && partida.titulo.lowercase().contains(textoBusqueda.lowercase()) && partida.titulo != "Sin título")
                    || (partida.descripcion != null && partida.descripcion.lowercase().contains(textoBusqueda.lowercase()) && partida.descripcion != "Sin descripción")
                ) {
                    partidasPropias.add(partida)
                }
            }
            ProveedorPartida.partidasUnidas.forEach { partida ->
                if ((partida.titulo != null && partida.titulo.lowercase().contains(textoBusqueda.lowercase()) && partida.titulo != "Sin título")
                    || (partida.descripcion != null && partida.descripcion.lowercase().contains(textoBusqueda.lowercase()) && partida.descripcion != "Sin descripción")
                ) {
                    partidasUnidas.add(partida)
                }
            }
            ProveedorPartida.partidasPropias.forEach { partida ->
                if ((partida.titulo != null && partida.titulo.lowercase().contains(textoBusqueda.lowercase()) && partida.titulo != "Sin título")
                    || (partida.descripcion != null && partida.descripcion.lowercase().contains(textoBusqueda.lowercase()) && partida.descripcion != "Sin descripción")
                ) {
                    partidasDisponibles.add(partida)
                }
            }
        }
    }

    fun cargarPartida(id: Int?) {
        viewModelScope.launch {
            ProveedorPartida.cargarPartida(id)
            _partida.value = ProveedorPartida.partida
        }
    }

    private val _crearPartidaResultado = mutableStateOf("")
    val crearPartidaResultado = _crearPartidaResultado

    fun modificarCrearPartidaResultado(crearPartidaResultado: String) {
        _crearPartidaResultado.value = crearPartidaResultado
    }

    fun crearPartida(titulo: String, descripcion: String, numMinJugadores: Int, numMaxJugadores: Int, rangoMinJugadores: Int, rangoMaxJugadores: Int, fechaInicio: Long?, fechaFinalizacion: Long?, finalizada: Boolean, privada: Boolean, infoVisible: Boolean) {
        viewModelScope.launch {
            _crearPartidaResultado.value = ProveedorPartida.crearPartida(titulo, descripcion, numMinJugadores, numMaxJugadores, rangoMinJugadores, rangoMaxJugadores, fechaInicio, fechaFinalizacion, finalizada, privada, infoVisible)
        }
    }

    private val _eliminarPartidaResultado = mutableStateOf("")
    val eliminarPartidaResultado = _eliminarPartidaResultado

    fun modificarEliminarPartidaResultado(eliminarPartidaResultado: String) {
        _eliminarPartidaResultado.value = eliminarPartidaResultado
    }

    fun eliminarPartida(id: Int) {
        viewModelScope.launch {
            _eliminarPartidaResultado.value = ProveedorPartida.eliminarPartida(id)
        }
    }

    private val _modificarPartidaResultado = mutableStateOf("")
    val modificarPartidaResultado = _modificarPartidaResultado

    fun modificarModificarPartidaResultado(modificarPartidaResultado: String) {
        _modificarPartidaResultado.value = modificarPartidaResultado
    }

    fun modificarPartida(id: Int, titulo: String, descripcion: String, numMinJugadores: Int, numMaxJugadores: Int, rangoMinJugadores: Int, rangoMaxJugadores: Int, fechaInicio: Long?, fechaFinalizacion: Long?, finalizada: Boolean, privada: Boolean, infoVisible: Boolean) {
        viewModelScope.launch {
            _modificarPartidaResultado.value = ProveedorPartida.modificarPartida(id, titulo, descripcion, numMinJugadores, numMaxJugadores, rangoMinJugadores, rangoMaxJugadores, fechaInicio, fechaFinalizacion, finalizada, privada, infoVisible)
            _partida.value = ProveedorPartida.partida
        }
    }

    private val _modoEdicion = mutableStateOf(false)
    val modoEdicion = _modoEdicion

    fun activarModoEdicion(activar: Boolean) {
        _modoEdicion.value = activar
    }

    private val _mostrarDatePicker = mutableStateOf(false)
    val mostrarDatePicker = _mostrarDatePicker

    fun mostrarDatePicker(mostrar: Boolean) {
        _mostrarDatePicker.value = mostrar
    }

    private val _mostrarDialogo = mutableStateOf(false)
    val mostrarDialogo = _mostrarDialogo

    fun mostrarDialogo(mostrar: Boolean) {
        _mostrarDialogo.value = mostrar
    }
}