package com.example.dndbonfire.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorTokenDispositivo
import com.example.dndbonfire.data.ProveedorUsuario
import com.example.dndbonfire.modelo.Usuario
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class TemaApp {
    SISTEMA,
    CLARO,
    OSCURO
}

class MainViewModel: ViewModel() {
    private val _temaApp = mutableStateOf(TemaApp.SISTEMA)
    val temaApp = _temaApp

    fun cambiarTema(tema: TemaApp) {
        _temaApp.value = tema
    }

    private val _menuLateralDesplegado = MutableStateFlow(false)
    val menuLateralDesplegado = _menuLateralDesplegado.asStateFlow()

    fun desplegarMenu(desplegar: Boolean) {
        _menuLateralDesplegado.value = desplegar
    }

    private val _usuarioLogeado = mutableStateOf<Usuario?>(null)
    val usuarioLogeado = _usuarioLogeado

    fun cargarUsuarioLogeado() {
        if (ProveedorUsuario.usuarioLogeado != null) {
            _usuarioLogeado.value = ProveedorUsuario.usuarioLogeado
        }
    }

    private val _loginResultado = mutableStateOf("")
    val loginResultado = _loginResultado

    fun modificarLoginResultado(loginResultado: String) {
        _loginResultado.value = loginResultado
    }

    fun logearUsuario(username: String, contrasena: String) {
        viewModelScope.launch {
            _loginResultado.value = ProveedorUsuario.logearUsuario(username, contrasena)
            _usuarioLogeado.value = ProveedorUsuario.usuarioLogeado
        }
    }

    private val _crearCuentaResultado = mutableStateOf("")
    val crearCuentaResultado = _crearCuentaResultado

    fun modificarCrearCuentaResultado(crearCuentaResultado: String) {
        _crearCuentaResultado.value = crearCuentaResultado
    }

    fun crearUsuario(nombre: String, username: String, contrasena: String) {
        viewModelScope.launch {
            _crearCuentaResultado.value = ProveedorUsuario.crearUsuario(nombre, username, contrasena)
            ProveedorUsuario.logearUsuario(username, contrasena)
            _usuarioLogeado.value = ProveedorUsuario.usuarioLogeado
        }
    }

    private val _modificarUsuarioResultado = mutableStateOf("")
    val modificarUsuarioResultado = _modificarUsuarioResultado

    fun modificarModificarUsuarioResultado(modificarUsuarioResultado: String) {
        _modificarUsuarioResultado.value = modificarUsuarioResultado
    }

    fun modificarUsuario(nombre: String, username: String, contrasena: String, rango: Int, imagen: String?) {
        viewModelScope.launch {
            _modificarUsuarioResultado.value = ProveedorUsuario.modificarUsuario(nombre, username, contrasena, rango, imagen)
            _usuarioLogeado.value = ProveedorUsuario.usuarioLogeado
        }
    }

    private val _eliminarUsuarioResultado = mutableStateOf("")
    val eliminarUsuarioResultado = _eliminarUsuarioResultado

    fun modificarEliminarUsuarioResultado(eliminarUsuarioResultado: String) {
        _eliminarUsuarioResultado.value = eliminarUsuarioResultado
    }

    fun eliminarUsuario(id: Int) {
        viewModelScope.launch {
            _eliminarUsuarioResultado.value = ProveedorUsuario.eliminarUsuario(id)
            _usuarioLogeado.value = null
        }
    }

    private val _mostrarDialogoConfirmacionBorrado = mutableStateOf(false)
    val mostrarDialogoConfirmacionBorrado = _mostrarDialogoConfirmacionBorrado

    fun mostrarDialogoConfirmacionBorrado(mostrar: Boolean) {
        _mostrarDialogoConfirmacionBorrado.value = mostrar
    }

    private val _cerrarSesionResultado = mutableStateOf("")
    val cerrarSesionResultado = _cerrarSesionResultado

    fun modificarCerrarSesionResultado(cerrarSesionResultado: String) {
        _cerrarSesionResultado.value = cerrarSesionResultado
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            val token = FirebaseMessaging.getInstance().token.await()
            modificarCerrarSesionResultado(ProveedorTokenDispositivo.activarTokens(token, usuarioLogeado.value!!, false))
        }
    }
}