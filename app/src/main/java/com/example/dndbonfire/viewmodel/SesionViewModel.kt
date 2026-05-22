package com.example.dndbonfire.viewmodel

import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorSesion
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.Sesion
import com.example.dndbonfire.modelo.Usuario
import kotlinx.coroutines.launch

class SesionViewModel: ViewModel() {
    var sesionesProgramadas = mutableStateListOf<Sesion>()
        private set
    var sesionesPasadas = mutableStateListOf<Sesion>()
        private set
    private val _sinSesiones = mutableStateOf(false)
    val sinSesiones = _sinSesiones

    var sesionesHoy = mutableStateListOf<Sesion>()
        private set
    var sesionesEstaSemana = mutableStateListOf<Sesion>()
        private set
    var sesionesEsteMes = mutableStateListOf<Sesion>()
        private set
    var sesionesMasDeMes = mutableStateListOf<Sesion>()
        private set
    private val _sinSesionesCalendario = mutableStateOf(false)
    val sinSesionesCalendario = _sinSesionesCalendario

    private val _sesionABorrar = mutableStateOf<Sesion?>(null)
    val sesionABorrar = _sesionABorrar

    fun modificarSesionABorrar(sesion: Sesion?) {
        _sesionABorrar.value = sesion
    }

    var idsSesionesABorrar = mutableStateListOf<Long>()
        private set

    fun modificarSesionesABorrar(accion: String, id: Long?) {
        if (id != null) {
            if (accion == "añadir") {
                idsSesionesABorrar.add(id)
            } else {
                idsSesionesABorrar.remove(id)
            }
        } else {
            idsSesionesABorrar.clear()
        }
    }

    private val _fecha = mutableLongStateOf(0)
    val fecha = _fecha

    fun inicializarFecha() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // esta parte requiere api 26
            val ahora = java.time.ZonedDateTime.now()
            val proximoSabado = ahora
                .with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.SATURDAY))
                .withHour(17)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

            _fecha.longValue = proximoSabado.toInstant().toEpochMilli()
        } else {
            // api 25 o menos
            val cal = java.util.Calendar.getInstance()
            val hoy = cal.get(java.util.Calendar.DAY_OF_WEEK)
            val sabado = java.util.Calendar.SATURDAY
            // si es sábado se va al sábado que viene
            val diasHastaSabado = if (hoy == sabado) {
                7
            } else {
                (sabado - hoy + 7) % 7
            }
            val anadirDias = diasHastaSabado

            cal.add(java.util.Calendar.DAY_OF_YEAR, anadirDias)
            cal.set(java.util.Calendar.HOUR_OF_DAY, 17)
            cal.set(java.util.Calendar.MINUTE, 0)
            cal.set(java.util.Calendar.SECOND, 0)
            cal.set(java.util.Calendar.MILLISECOND, 0)

            _fecha.longValue = cal.timeInMillis
        }
    }

    fun modificarFecha(fecha: Long) {
        _fecha.longValue = fecha
    }

    private val _descripcion = mutableStateOf("")
    val descripcion = _descripcion

    fun modificarDescripcion(descripcion: String) {
        _descripcion.value = descripcion
    }

    var jugadoresApuntados = mutableStateListOf<Int>()
        private set

    fun forzarJugadoresApuntados(jugadores: List<Int>) {
        jugadoresApuntados.clear()
        jugadoresApuntados.addAll(jugadoresApuntados.toMutableStateList())
    }

    fun modificarJugadoresApuntados(sesion: Sesion, accion: String, id: Int?) {
        if (id != null) {
            if (accion == "añadir") {
                sesion.jugadoresApuntados.add(id)
            } else {
                sesion.jugadoresApuntados.remove(id)
            }
        } else {
            sesion.jugadoresApuntados.clear()
        }
        modificarSesion(sesion)
    }

    private val _accion = mutableStateOf("")
    val accion = _accion

    fun modificarAccion(accion: String) {
        _accion.value = accion
    }

    init {
        inicializarFecha()
        cargarSesiones()
        cargarSesionesCalendario()
    }

    fun cargarSesionesCalendario() {
        sesionesHoy.clear()
        sesionesEstaSemana.clear()
        sesionesEsteMes.clear()
        sesionesMasDeMes.clear()
        sesionesHoy.addAll(ProveedorSesion.sesionesHoy)
        sesionesEstaSemana.addAll(ProveedorSesion.sesionesEstaSemana)
        sesionesEsteMes.addAll(ProveedorSesion.sesionesEsteMes)
        sesionesMasDeMes.addAll(ProveedorSesion.sesionesMasDeMes)
        sinSesionesCalendario.value = ProveedorSesion.sinSesionesCalendario
    }

    fun recargarSesionesCalendario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                sesionesHoy.clear()
                sesionesEstaSemana.clear()
                sesionesEsteMes.clear()
                sesionesMasDeMes.clear()
                ProveedorSesion.cargarSesionesCalendario(usuario)
                cargarSesionesCalendario()
            } catch (e: Exception) {}
        }
    }

    fun cargarSesiones() {
        sesionesProgramadas.clear()
        sesionesPasadas.clear()
        sesionesProgramadas.addAll(ProveedorSesion.sesionesProgramadas)
        sesionesPasadas.addAll(ProveedorSesion.sesionesPasadas)
        _sinSesiones.value = ProveedorSesion.sinSesiones
    }

    fun recargarSesiones(partida: Partida) {
        viewModelScope.launch {
            try {
                sesionesProgramadas.clear()
                sesionesPasadas.clear()
                ProveedorSesion.cargarSesiones(partida)
                cargarSesiones()
            } catch (e: Exception) {}
        }
    }

    fun vaciarSesiones() {
        viewModelScope.launch {
            sesionesProgramadas.clear()
            sesionesPasadas.clear()
            _sinSesiones.value = false
        }
    }

    fun modificarSinSesiones(sinSesiones: Boolean) {
        _sinSesiones.value = sinSesiones
    }

    private val _creandoSesion = mutableStateOf(false)
    val creandoSesion = _creandoSesion

    fun modificarCreandoSesion(creando: Boolean) {
        _creandoSesion.value = creando
    }

    private val _crearSesionResultado = mutableStateOf("")
    val crearSesionResultado = _crearSesionResultado

    fun modificarCrearSesionResultado(crearSesionResultado: String) {
        _crearSesionResultado.value = crearSesionResultado
    }

    fun crearSesion(partida: Partida) {
        viewModelScope.launch {
            _crearSesionResultado.value = ProveedorSesion.crearSesion(fecha.longValue, descripcion.value, jugadoresApuntados, partida)
        }
    }

    fun modificarEliminarSesionResultado(eliminarSesionResultado: String) {
        _eliminarSesionResultado.value = eliminarSesionResultado
    }

    private val _eliminarSesionResultado = mutableStateOf("")
    val eliminarSesionResultado = _eliminarSesionResultado

    fun eliminarSesion(id: Long) {
        viewModelScope.launch {
            _eliminarSesionResultado.value = ProveedorSesion.eliminarSesion(id)
        }
    }

    private val _eliminarSesionesMultiplesResultado = mutableStateOf("")
    val eliminarSesionesMultiplesResultado = _eliminarSesionesMultiplesResultado

    fun modificarEliminarSesionesMultiplesResultado(eliminarSesionesMultiplesResultado: String) {
        _eliminarSesionesMultiplesResultado.value = eliminarSesionesMultiplesResultado
    }

    fun eliminarSesionesMultiples(idsRelaciones: List<Long>) {
        viewModelScope.launch {
            _eliminarSesionesMultiplesResultado.value = ProveedorSesion.eliminarSesionesMultiples(idsRelaciones)
        }
    }

    private val _modificarSesionResultado = mutableStateOf("")
    val modificarSesionResultado = _modificarSesionResultado

    fun modificarModificarSesionResultado(modificarSesionResultado: String) {
        _modificarSesionResultado.value = modificarSesionResultado
    }

    fun modificarSesion(sesion: Sesion) {
        viewModelScope.launch {
            _modificarSesionResultado.value = ProveedorSesion.modificarSesion(sesion)
        }
    }

    fun modificarSesionDatos(sesion: Sesion, fecha: Long, descripcion: String) {
        viewModelScope.launch {
            _modificarSesionResultado.value = ProveedorSesion.modificarSesion(sesion, fecha, descripcion)
        }
    }

    private val _idSesion = mutableStateOf<Long?>(null)
    val idSesion = _idSesion

    fun modificarIdSesion(id: Long?) {
        _idSesion.value = id
    }

    private val _mostrarDatePicker = mutableStateOf(false)
    val mostrarDatePicker = _mostrarDatePicker

    fun mostrarDatePicker(mostrar: Boolean) {
        _mostrarDatePicker.value = mostrar
    }

    private val _mostrarDialTimePicker = mutableStateOf(false)
    val mostrarDialTimePicker = _mostrarDialTimePicker

    fun mostrarDialTimePicker(mostrar: Boolean) {
        _mostrarDialTimePicker.value = mostrar
    }
}