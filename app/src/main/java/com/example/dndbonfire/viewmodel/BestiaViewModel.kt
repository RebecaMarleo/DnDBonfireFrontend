package com.example.dndbonfire.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorBestia
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.modelo.Accion
import com.example.dndbonfire.modelo.AccionLegendaria
import com.example.dndbonfire.modelo.Bestia
import com.example.dndbonfire.modelo.Descripcion
import com.example.dndbonfire.modelo.HabilidadEspecial
import com.example.dndbonfire.modelo.Reaccion
import com.example.dndbonfire.modelo.ResumenBestia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.String

class BestiaViewModel: ViewModel() {
    var bestias = mutableStateListOf<ResumenBestia>()
        private set

    var bestiasInicial = mutableStateListOf<ResumenBestia>()
        private set

    private val _bestiaSeleccionada = mutableStateOf<Bestia?>(null)
    val bestiaSeleccionada: State<Bestia?> = _bestiaSeleccionada

    private val _bestiaSeleccionadaTraducida = mutableStateOf<Bestia?>(null)
    val bestiaSeleccionadaTraducida: State<Bestia?> = _bestiaSeleccionadaTraducida

    private val _mostrarDialogo = mutableStateOf(false)
    val mostrarDialogo = _mostrarDialogo

    private val _habilidad = mutableStateOf<HabilidadEspecial?>(null)
    val habilidad = _habilidad

    private val _accion = mutableStateOf<Accion?>(null)
    val accion = _accion

    private val _accionLegendaria = mutableStateOf<AccionLegendaria?>(null)
    val accionLegendaria = _accionLegendaria

    private val _reaccion = mutableStateOf<Reaccion?>(null)
    val reaccion = _reaccion

    private val _descOriginal = mutableStateOf<String?>(null)
    val descOriginal = _descOriginal
    private val _descTraducida = mutableStateOf<String?>(null)
    val descTraducida = _descTraducida

    fun cargarDescripcion(desc: String?) {
        viewModelScope.launch {
            _descOriginal.value = desc
            _descTraducida.value = ProveedorBestia.traducirDescripcionSeleccionada(desc)
        }
    }

    init {
        cargarBestias()
    }

    private fun cargarBestias() {
        bestias.addAll(ProveedorBestia.bestias)

        var ultimaLetra = " "
        bestias.forEach { bestia ->
            if (!bestia.name.startsWith(ultimaLetra)) {
                ultimaLetra = bestia.name.first().toString()
                bestiasInicial.add(bestia)
            }
        }
    }

    fun filtrarBestias(textoBusqueda: String) {
        bestias.clear()
        bestiasInicial.clear()

        // si la cadena recibida es vacía devuelve la lista completa
        if (textoBusqueda.isEmpty()) {
            cargarBestias()
        } else {
            ProveedorBestia.bestias.forEach { bestia ->
                if (bestia.name.lowercase().contains(textoBusqueda.lowercase())) {
                    bestias.add(bestia)
                }
            }

            var ultimaLetra = " "
            bestias.forEach { bestia ->
                if (!bestia.name.startsWith(ultimaLetra)) {
                    ultimaLetra = bestia.name.first().toString()
                    bestiasInicial.add(bestia)
                }
            }
        }
    }

    fun cargarBestia(id: String?) {
        viewModelScope.launch {
            if (id != null) {
                _bestiaSeleccionada.value = ProveedorBestia.cargarBestiaSeleccionada(id)
                _bestiaSeleccionadaTraducida.value = ProveedorBestia.cargarBestiaSeleccionadaTraducida(_bestiaSeleccionada.value)
            } else {
                _bestiaSeleccionada.value = null
                _bestiaSeleccionadaTraducida.value = null
            }
        }
    }

    fun mostrarDialogo(mostrar: Boolean) {
        _mostrarDialogo.value = mostrar
    }

    fun seleccionarHabilidad(habilidad: HabilidadEspecial?) {
        _habilidad.value = habilidad
    }

    fun seleccionarAccion(accion: Accion?) {
        _accion.value = accion
    }

    fun seleccionarAccionLegendaria(accionLegendaria: AccionLegendaria?) {
        _accionLegendaria.value = accionLegendaria
    }

    fun seleccionarReaccion(reaccion: Reaccion?) {
        _reaccion.value = reaccion
    }
}