package com.example.dndbonfire.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.data.ProveedorUsuario
import com.example.dndbonfire.modelo.Usuario
import kotlinx.coroutines.launch

class UsuarioViewModel: ViewModel() {
    private val _nombre = mutableStateOf("")
    val nombre = _nombre

    fun modificarNombre(nombre: String) {
        _nombre.value = nombre
    }

    private val _username = mutableStateOf("")
    val username = _username

    fun modificarUsername(username: String) {
        _username.value = username
    }

    private val _menuRangoExpandido = mutableStateOf(false)
    val menuRangoExpandido = _menuRangoExpandido

    fun modificarMenuRangoExpandido(menuRangoExpandido: Boolean) {
        _menuRangoExpandido.value = menuRangoExpandido
    }

    private val _rango = mutableIntStateOf(0)
    val rango = _rango

    fun modificarRango(rango: Int) {
        _rango.intValue = rango
    }

    private val _contrasena = mutableStateOf("")
    val contrasena = _contrasena

    fun modificarContrasena(contrasena: String) {
        _contrasena.value = contrasena
    }

    private val _contrasenaRe = mutableStateOf("")
    val contrasenaRe = _contrasenaRe

    fun modificarContrasenaRe(contrasenaRe: String) {
        _contrasenaRe.value = contrasenaRe
    }

    private val _imagen = mutableStateOf<String?>(null)
    val imagen = _imagen

    fun modificarImagen(imagen: String?) {
        _imagen.value = imagen
    }

    private val _mantenerseLogeado = mutableStateOf(false)
    val mantenerseLogeado = _mantenerseLogeado

    fun cambiarEstadoMantenerseLogeado(mantenerseLogeado: Boolean) {
        _mantenerseLogeado.value = mantenerseLogeado
        ProveedorUsuario.cambiarEstadoMantenerseLogeado(mantenerseLogeado)
    }

    private val _modoEdicion = mutableStateOf(false)
    val modoEdicion = _modoEdicion

    fun activarModoEdicion(activar: Boolean) {
        _modoEdicion.value = activar
    }

    private val _usuarioDM = mutableStateOf<Usuario?>(null)
    val usuarioDM = _usuarioDM

    fun cargarUsuarioDM(id: Int?) {
        viewModelScope.launch {
            ProveedorUsuario.cargarUsuarioDM(id)
            _usuarioDM.value = ProveedorUsuario.usuarioDM
        }
    }

    private val _mostrarDialogoCuenta = mutableStateOf(false)
    val mostrarDialogoCuenta = _mostrarDialogoCuenta

    fun mostrarDialogo(mostrar: Boolean) {
        _mostrarDialogoCuenta.value = mostrar
    }
}