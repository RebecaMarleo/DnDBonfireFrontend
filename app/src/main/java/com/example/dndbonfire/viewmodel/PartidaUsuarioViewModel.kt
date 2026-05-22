package com.example.dndbonfire.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorPartidaUsuario
import com.example.dndbonfire.modelo.Aventurero
import com.example.dndbonfire.modelo.Partida
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.modelo.Usuario
import kotlinx.coroutines.launch

class PartidaUsuarioViewModel: ViewModel() {
    var aventureros = mutableStateMapOf<Usuario, Personaje>()
        private set
    private val _sinAventureros = mutableStateOf(false)
    val sinAventureros = _sinAventureros

    fun cargarAventureros() {
        viewModelScope.launch {
            aventureros.clear()
            aventureros.putAll(ProveedorPartidaUsuario.aventureros)
            sinAventureros.value = ProveedorPartidaUsuario.sinAventureros
        }
    }

    fun recargarAventureros(partida: Partida) {
        viewModelScope.launch {
            ProveedorPartidaUsuario.cargarAventureros(partida)
            cargarAventureros()
        }
    }

    fun vaciarAventureros() {
        viewModelScope.launch {
            aventureros.clear()
            _sinAventureros.value = false
        }
    }

    private val _personaje = mutableStateOf<Personaje?>(null)
    val personaje: State<Personaje?> = _personaje

    fun seleccionarPersonaje(personaje: Personaje?) {
        viewModelScope.launch {
            _personaje.value = personaje
        }
    }

    private val _aventureroABorrar = mutableStateOf<Aventurero?>(null)
    val aventureroABorrar = _aventureroABorrar

    fun modificarAventureroABorrar(aventurero: Aventurero?) {
        _aventureroABorrar.value = aventurero
    }

    var idsAventurerosABorrar = mutableStateListOf<Int>()
        private set

    fun modificarAventurerosABorrar(accion: String, id: Int?) {
        if (id != null) {
            if (accion == "añadir") {
                idsAventurerosABorrar.add(id)
            } else {
                idsAventurerosABorrar.remove(id)
            }
        } else {
            idsAventurerosABorrar.clear()
        }
    }

    private val _crearRelacionResultado = mutableStateOf("")
    val crearRelacionResultado = _crearRelacionResultado

    fun modificarCrearRelacionResultado(crearRelacionResultado: String) {
        _crearRelacionResultado.value = crearRelacionResultado
    }

    fun crearRelacionPartidaUsuario(partida: Partida) {
        viewModelScope.launch {
            _crearRelacionResultado.value = ProveedorPartidaUsuario.crearRelacionPartidaUsuario(personaje.value!!, partida)
        }
    }

    private val _autoEliminado = mutableStateOf(false)
    val autoEliminado = _autoEliminado

    fun modificarAutoEliminado(autoEliminado: Boolean) {
        _autoEliminado.value = autoEliminado
    }

    fun modificarEliminarRelacionResultado(eliminarRelacionResultado: String) {
        _eliminarRelacionResultado.value = eliminarRelacionResultado
    }

    private val _eliminarRelacionResultado = mutableStateOf("")
    val eliminarRelacionResultado = _eliminarRelacionResultado

    fun eliminarRelacionPartidaUsuario(partida: Partida, id: Int) {
        viewModelScope.launch {
            _eliminarRelacionResultado.value = ProveedorPartidaUsuario.eliminarRelacionPartidaUsuario(partida, id)
        }
    }

    private val _eliminarRelacionesMultiplesResultado = mutableStateOf("")
    val eliminarRelacionesMultiplesResultado = _eliminarRelacionesMultiplesResultado

    fun modificarEliminarRelacionesMultiplesResultado(eliminarRelacionesMultiplesResultado: String) {
        _eliminarRelacionesMultiplesResultado.value = eliminarRelacionesMultiplesResultado
    }

    fun eliminarRelacionesPartidaUsuarioMultiples(partida: Partida, idsRelaciones: List<Int>) {
        viewModelScope.launch {
            _eliminarRelacionesMultiplesResultado.value = ProveedorPartidaUsuario.eliminarRelacionesPartidaUsuarioMultiples(partida, idsRelaciones)
        }
    }

    private val _modificandoRelacionPartidaUsuario = mutableStateOf(false)
    val modificandoRelacionPartidaUsuario = _modificandoRelacionPartidaUsuario

    fun modificarModificandoRelacionPartidaUsuario(modificando: Boolean) {
        _modificandoRelacionPartidaUsuario.value = modificando
    }

    private val _modificarRelacionResultado = mutableStateOf("")
    val modificarRelacionResultado = _modificarRelacionResultado

    fun modificarModificarRelacionResultado(modificarRelacionResultado: String) {
        _modificarRelacionResultado.value = modificarRelacionResultado
    }

    fun modificarRelacionPartidaUsuario(partida: Partida) {
        viewModelScope.launch {
            _modificarRelacionResultado.value = ProveedorPartidaUsuario.modificarRelacionPartidaUsuario(personaje.value!!, partida)
            }
    }

    private val _mostrarDialogoSeleccionPersonaje = mutableStateOf(false)
    val mostrarDialogoSeleccionPersonaje = _mostrarDialogoSeleccionPersonaje

    fun mostrarDialogoSeleccionPersonaje(mostrar: Boolean) {
        _mostrarDialogoSeleccionPersonaje.value = mostrar
    }
}