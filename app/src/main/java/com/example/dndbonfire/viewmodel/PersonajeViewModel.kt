package com.example.dndbonfire.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndbonfire.data.ProveedorPersonaje
import com.example.dndbonfire.data.ProveedorUsuario
import com.example.dndbonfire.modelo.Clase
import com.example.dndbonfire.modelo.Descripcion
import com.example.dndbonfire.modelo.Idioma
import com.example.dndbonfire.modelo.Nivel
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.modelo.Raza
import com.example.dndbonfire.modelo.ResumenClase
import com.example.dndbonfire.modelo.ResumenConjuroDeClase
import com.example.dndbonfire.modelo.ResumenRaza
import com.example.dndbonfire.modelo.Subclase
import com.example.dndbonfire.modelo.Subraza
import kotlinx.coroutines.launch
import kotlin.math.ceil

class PersonajeViewModel: ViewModel() {
    var personajes = mutableStateListOf<Personaje>()
        private set

    private val _sinPersonajes = mutableStateOf(false)
    val sinPersonajes = _sinPersonajes

    var personajesInicial = mutableStateListOf<Personaje>()
        private set

    private val _personaje = mutableStateOf<Personaje?>(null)
    val personaje: State<Personaje?> = _personaje
    private val _personajeTraducido = mutableStateOf<Personaje?>(null)
    val personajeTraducido: State<Personaje?> = _personajeTraducido

    private val _nombre = mutableStateOf("")
    val nombre = _nombre

    fun modificarNombre(nombre: String) {
        _nombre.value = nombre
    }

    private val _imagen = mutableStateOf<String?>(null)
    val imagen = _imagen

    fun modificarImagen(imagen: String?) {
        _imagen.value = imagen
    }

    var razas = mutableStateListOf<ResumenRaza>()
        private set

    private val _menuRazaExpandido = mutableStateOf(false)
    val menuRazaExpandido = _menuRazaExpandido

    fun modificarMenuRazaExpandido(menuRazaExpandido: Boolean) {
        _menuRazaExpandido.value = menuRazaExpandido
    }

    private val _raza = mutableStateOf<Raza?>(null)
    val raza = _raza
    private val _razaTraducida = mutableStateOf<Raza?>(null)
    val razaTraducida = _razaTraducida

    fun cargarRaza(id: String) {
        viewModelScope.launch {
            _raza.value = ProveedorPersonaje.cargarRazaSeleccionada(id)
            _razaTraducida.value = ProveedorPersonaje.cargarRazaSeleccionadaTraducida(_raza.value)
            _subraza.value = ProveedorPersonaje.cargarSubrazaSeleccionada("")
            _subrazaTraducida.value = ProveedorPersonaje.cargarSubrazaSeleccionadaTraducida(_subraza.value)
            _idiomaSeleccionado.value = ProveedorPersonaje.cargarIdiomaSeleccionado("")
        }
    }

    private val _razaMod = mutableStateOf<Raza?>(null)
    val razaMod = _razaMod
    private val _razaTraducidaMod = mutableStateOf<Raza?>(null)
    val razaTraducidaMod = _razaTraducidaMod

    fun forzarRazaMod() {
        _razaMod.value = _raza.value
        _razaTraducidaMod.value = _razaTraducida.value
    }

    fun forzarRaza() {
        _raza.value = _razaMod.value
        _razaTraducida.value = _razaTraducidaMod.value
    }

    fun cargarRazaMod(id: String) {
        viewModelScope.launch {
            _razaMod.value = ProveedorPersonaje.cargarRazaSeleccionada(id)
            _razaTraducidaMod.value = ProveedorPersonaje.cargarRazaSeleccionadaTraducida(_razaMod.value)
            _subrazaMod.value = ProveedorPersonaje.cargarSubrazaSeleccionada("")
            _subrazaTraducidaMod.value = ProveedorPersonaje.cargarSubrazaSeleccionadaTraducida(_subrazaMod.value)
            _idiomaSeleccionado.value = ProveedorPersonaje.cargarIdiomaSeleccionado("")
        }
    }

    private val _menuSubrazaExpandido = mutableStateOf(false)
    val menuSubrazaExpandido = _menuSubrazaExpandido

    fun modificarMenuSubrazaExpandido(menuSubrazaExpandido: Boolean) {
        _menuSubrazaExpandido.value = menuSubrazaExpandido
    }

    private val _subraza = mutableStateOf<Subraza?>(null)
    val subraza = _subraza
    private val _subrazaTraducida = mutableStateOf<Subraza?>(null)
    val subrazaTraducida = _subrazaTraducida

    fun cargarSubraza(id: String) {
        viewModelScope.launch {
            _subraza.value = ProveedorPersonaje.cargarSubrazaSeleccionada(id)
            _subrazaTraducida.value = ProveedorPersonaje.cargarSubrazaSeleccionadaTraducida(_subraza.value)
        }
    }

    private val _subrazaMod = mutableStateOf<Subraza?>(null)
    val subrazaMod = _subrazaMod
    private val _subrazaTraducidaMod = mutableStateOf<Subraza?>(null)
    val subrazaTraducidaMod = _subrazaTraducidaMod

    fun forzarSubrazaMod() {
        _subrazaMod.value = _subraza.value
        _subrazaTraducidaMod.value = _subrazaTraducida.value
    }

    fun forzarSubraza() {
        _subraza.value = _subrazaMod.value
        _subrazaTraducida.value = _subrazaTraducidaMod.value
    }

    fun cargarSubrazaMod(id: String) {
        viewModelScope.launch {
            _subrazaMod.value = ProveedorPersonaje.cargarSubrazaSeleccionada(id)
            _subrazaTraducidaMod.value = ProveedorPersonaje.cargarSubrazaSeleccionadaTraducida(_subrazaMod.value)
        }
    }

    private val _hp = mutableIntStateOf(0)
    val hp = _hp

    fun modificarHp(hp: Int) {
        _hp.value = hp
    }

    private val _hpAuto = mutableStateOf(true)
    val hpAuto = _hpAuto

    fun modificarHpAuto(hpAuto: Boolean) {
        _hpAuto.value = hpAuto
    }

    private val _fuerza = mutableIntStateOf(0)
    val fuerza = _fuerza

    fun modificarFuerza(fuerza: Int) {
        _fuerza.value = fuerza
    }

    private val _destreza = mutableIntStateOf(0)
    val destreza = _destreza

    fun modificarDestreza(destreza: Int) {
        _destreza.value = destreza
    }

    private val _constitucion = mutableIntStateOf(0)
    val constitucion = _constitucion

    fun modificarConstitucion(constitucion: Int) {
        _constitucion.value = constitucion
    }

    private val _inteligencia = mutableIntStateOf(0)
    val inteligencia = _inteligencia

    fun modificarInteligencia(inteligencia: Int) {
        _inteligencia.value = inteligencia
    }

    private val _sabiduria = mutableIntStateOf(0)
    val sabiduria = _sabiduria

    fun modificarSabiduria(sabiduria: Int) {
        _sabiduria.value = sabiduria
    }

    private val _carisma = mutableIntStateOf(0)
    val carisma = _carisma

    fun modificarCarisma(carisma: Int) {
        _carisma.value = carisma
    }

    private val _edad = mutableIntStateOf(0)
    val edad = _edad

    fun modificarEdad(edad: Int) {
        _edad.value = edad
    }

    private val _alineamiento = mutableStateOf("Neutral verdadero")
    val alineamiento = _alineamiento

    fun modificarAlineamiento(alineamiento: String) {
        _alineamiento.value = alineamiento
    }

    private val _urlOriginal = mutableStateOf<Descripcion?>(null)
    val urlOriginal = _urlOriginal
    private val _urlTraducida = mutableStateOf<Descripcion?>(null)
    val urlTraducida = _urlTraducida

    fun cargarDescripcion(url: String) {
        viewModelScope.launch {
            _urlOriginal.value = ProveedorPersonaje.cargarDescripcionSeleccionada(url)
            _urlTraducida.value = ProveedorPersonaje.cargarDescripcionSeleccionadaTraducida(_urlOriginal.value)
        }
    }

    private val _infoSeleccionada = mutableStateOf("")
    val infoSeleccionada = _infoSeleccionada

    fun modificarInfoSeleccionada(infoSeleccionada: String) {
        _infoSeleccionada.value = infoSeleccionada
    }

    var idiomas = mutableStateListOf<Idioma>()
        private set

    private val _menuIdiomaExpandido = mutableStateOf(false)
    val menuIdiomaExpandido = _menuIdiomaExpandido

    fun modificarMenuIdiomaExpandido(menuIdiomaExpandido: Boolean) {
        _menuIdiomaExpandido.value = menuIdiomaExpandido
    }

    private val _idiomaSeleccionado = mutableStateOf<Idioma?>(null)
    val idiomaSeleccionado = _idiomaSeleccionado

    fun cargarIdioma(id: String) {
        viewModelScope.launch {
            _idiomaSeleccionado.value = ProveedorPersonaje.cargarIdiomaSeleccionado(id)
        }
    }

    var clases = mutableStateListOf<ResumenClase>()
        private set

    private val _menuClaseExpandido = mutableStateOf(false)
    val menuClaseExpandido = _menuClaseExpandido

    fun modificarMenuClaseExpandido(menuClaseExpandido: Boolean) {
        _menuClaseExpandido.value = menuClaseExpandido
    }

    private val _clase = mutableStateOf<Clase?>(null)
    val clase = _clase
    private val _claseTraducida = mutableStateOf<Clase?>(null)
    val claseTraducida = _claseTraducida

    fun cargarClase(id: String) {
        viewModelScope.launch {
            _clase.value = ProveedorPersonaje.cargarClaseSeleccionada(id)

            infoNiveles.clear()
            conjuros.clear()

            // para que se muestre el hp actualizado lo mas rapido posible
            if (hpAuto.value && clase.value != null) {
                var hpAutomatico = 0
                if (nivel.intValue > 0) {
                    hpAutomatico = (ceil(((clase.value!!.hit_die+1).toDouble()/2) + ceil((constitucion.intValue - 10).toDouble()/2)) * (nivel.intValue - 1) + clase.value!!.hit_die + ceil((constitucion.intValue - 10).toDouble()/2)).toInt()
                }
                modificarHp(hpAutomatico)
            }

            _claseTraducida.value = ProveedorPersonaje.cargarClaseSeleccionadaTraducida(_clase.value)
            _subclase.value = ProveedorPersonaje.cargarSubclaseSeleccionada("")
            _subclaseTraducida.value = ProveedorPersonaje.cargarSubclaseSeleccionadaTraducida(_subclase.value)

            cargarInfoNiveles(id)
            cargarConjuros(id)
        }
    }

    private val _claseMod = mutableStateOf<Clase?>(null)
    val claseMod = _claseMod
    private val _claseTraducidaMod = mutableStateOf<Clase?>(null)
    val claseTraducidaMod = _claseTraducidaMod

    fun forzarClaseMod() {
        _claseMod.value = _clase.value
        _claseTraducidaMod.value = _claseTraducida.value
    }

    fun forzarClase() {
        _clase.value = _claseMod.value
        _claseTraducida.value = _claseTraducidaMod.value
    }

    fun cargarClaseMod(id: String) {
        viewModelScope.launch {
            _claseMod.value = ProveedorPersonaje.cargarClaseSeleccionada(id)

            infoNivelesMod.clear()
            conjurosMod.clear()

            // para que se muestre el hp actualizado lo mas rapido posible
            if (hpAuto.value && clase.value != null) {
                var hpAutomatico = 0
                if (nivel.intValue > 0) {
                    hpAutomatico = (ceil(((claseMod.value!!.hit_die+1).toDouble()/2) + ceil((constitucion.intValue - 10).toDouble()/2)) * (nivel.intValue - 1) + claseMod.value!!.hit_die + ceil((constitucion.value - 10).toDouble()/2)).toInt()
                }
                modificarHp(hpAutomatico)
            }

            _claseTraducidaMod.value = ProveedorPersonaje.cargarClaseSeleccionadaTraducida(_claseMod.value)
            _subclaseMod.value = ProveedorPersonaje.cargarSubclaseSeleccionada("")
            _subclaseTraducidaMod.value = ProveedorPersonaje.cargarSubclaseSeleccionadaTraducida(_subclaseMod.value)

            cargarInfoNivelesMod(id)
            cargarConjurosMod(id)
        }
    }

    private val _menuSubclaseExpandido = mutableStateOf(false)
    val menuSubclaseExpandido = _menuSubclaseExpandido

    fun modificarMenuSubclaseExpandido(menuSubclaseExpandido: Boolean) {
        _menuSubclaseExpandido.value = menuSubclaseExpandido
    }

    private val _subclase = mutableStateOf<Subclase?>(null)
    val subclase = _subclase
    private val _subclaseTraducida = mutableStateOf<Subclase?>(null)
    val subclaseTraducida = _subclaseTraducida

    fun cargarSubclase(id: String) {
        viewModelScope.launch {
            _subclase.value = ProveedorPersonaje.cargarSubclaseSeleccionada(id)
            _subclaseTraducida.value = ProveedorPersonaje.cargarSubclaseSeleccionadaTraducida(_subclase.value)
        }
    }

    private val _subclaseMod = mutableStateOf<Subclase?>(null)
    val subclaseMod = _subclaseMod
    private val _subclaseTraducidaMod = mutableStateOf<Subclase?>(null)
    val subclaseTraducidaMod = _subclaseTraducidaMod

    fun forzarSubclaseMod() {
        _subclaseMod.value = _subclase.value
        _subclaseTraducidaMod.value = _subclaseTraducida.value
    }

    fun forzarSubclase() {
        _subclase.value = _subclaseMod.value
        _subclaseTraducida.value = _subclaseTraducidaMod.value
    }

    fun cargarSubclaseMod(id: String) {
        viewModelScope.launch {
            _subclaseMod.value = ProveedorPersonaje.cargarSubclaseSeleccionada(id)
            _subclaseTraducidaMod.value = ProveedorPersonaje.cargarSubclaseSeleccionadaTraducida(_subclaseMod.value)
        }
    }

    var competenciasSeleccionadas = mutableStateListOf(
            mutableStateListOf<String>(),
            mutableStateListOf<String>()
        )
        private set

    fun forzarCompetenciasSeleccionadas(competencias: List<List<String>>) {
        competenciasSeleccionadas.clear()
        competenciasSeleccionadas.addAll(
            competencias.map { competencia ->
                competencia.toMutableStateList()
            }
        )
    }

    fun modificarCompetenciasSeleccionadas(accion: String, lista: Int?, index: String?) {
        if (index != null && lista != null) {
            if (accion == "añadir") {
                competenciasSeleccionadas[lista].add(index)
            } else {
                competenciasSeleccionadas[lista].remove(index)
            }
        } else {
            for (i in 0..1) {
                competenciasSeleccionadas[i].clear()
            }
        }
    }

    private val _nivel = mutableIntStateOf(0)
    val nivel = _nivel

    fun modificarNivel(nivel: Int) {
        _nivel.value = nivel
    }

    var infoNiveles = mutableStateListOf<Nivel>()
        private set

    fun cargarInfoNiveles(clase: String) {
        viewModelScope.launch {
            infoNiveles.clear()
            infoNiveles.addAll(ProveedorPersonaje.cargarInfoNiveles(clase))
        }
    }

    var infoNivelesMod = mutableStateListOf<Nivel>()
        private set

    fun forzarInfoNivelesMod() {
        infoNivelesMod = infoNiveles
    }

    fun forzarInfoNiveles() {
        infoNiveles = infoNivelesMod
    }

    fun cargarInfoNivelesMod(clase: String) {
        viewModelScope.launch {
            infoNivelesMod.clear()
            infoNivelesMod.addAll(ProveedorPersonaje.cargarInfoNiveles(clase))
        }
    }

    var conjuros = mutableStateListOf<ResumenConjuroDeClase>()
        private set

    fun cargarConjuros(clase: String) {
        viewModelScope.launch {
            conjuros.clear()
            conjuros.addAll(ProveedorPersonaje.cargarConjuros(clase))
        }
    }

    var conjurosMod = mutableStateListOf<ResumenConjuroDeClase>()
        private set

    fun forzarConjurosMod() {
        conjurosMod = conjuros
    }

    fun forzarConjuros() {
        conjuros = conjurosMod
    }

    fun cargarConjurosMod(clase: String) {
        viewModelScope.launch {
            conjurosMod.clear()
            conjurosMod.addAll(ProveedorPersonaje.cargarConjuros(clase))
        }
    }

    var trucosConocidos = mutableStateListOf<String>()
        private set

    fun forzarTrucosConocidos(trucos: List<String>) {
        trucosConocidos.clear()
        trucosConocidos.addAll(trucos.toMutableStateList())
    }

    fun modificarTrucosConocidos(accion: String, index: String?) {
        if (index != null) {
            if (accion == "añadir") {
                trucosConocidos.add(index)
            } else {
                trucosConocidos.remove(index)
            }
        } else {
            trucosConocidos.clear()
        }
    }

    var conjurosConocidos = mutableStateListOf<String>()
        private set

    fun forzarConjurosConocidos(conjuros: List<String>) {
        conjurosConocidos.clear()
        conjurosConocidos.addAll(conjuros.toMutableStateList())
    }

    fun modificarConjurosConocidos(accion: String, index: String?) {
        if (index != null) {
            if (accion == "añadir") {
                conjurosConocidos.add(index)
            } else {
                conjurosConocidos.remove(index)
            }
        } else {
            conjurosConocidos.clear()
        }
    }

    private val _personajeABorrar = mutableStateOf<Personaje?>(null)
    val personajeABorrar = _personajeABorrar

    fun modificarPersonajeABorrar(personaje: Personaje?) {
        _personajeABorrar.value = personaje
    }

    var idsPersonajesABorrar = mutableStateListOf<Int>()
        private set

    fun modificarPersonajesABorrar(accion: String, id: Int?) {
        if (id != null) {
            if (accion == "añadir") {
                idsPersonajesABorrar.add(id)
            } else {
                idsPersonajesABorrar.remove(id)
            }
        } else {
            idsPersonajesABorrar.clear()
        }
    }

    init {
        cargarPersonajes()
        cargarRazas()
        cargarIdiomas()
        cargarClases()
    }

    fun cargarPersonajes() {
        personajes.clear()
        personajes.addAll(ProveedorPersonaje.personajes)
        _sinPersonajes.value = ProveedorPersonaje.sinPersonajes

        var ultimaLetra = " "
        var sinNombre = false
        personajes.forEach { personaje ->
            personaje.nombre?.startsWith(ultimaLetra)?.let {
                if (!it && personaje.nombre != "Sin nombre") {
                    ultimaLetra = personaje.nombre.first().toString()
                    personajesInicial.add(personaje)
                } else if (personaje.nombre == "Sin nombre" && !sinNombre) {
                    ultimaLetra = " "
                    sinNombre = true
                    personajesInicial.add(personaje)
                }
            }
        }
    }

    fun recargarPersonajes() {
        viewModelScope.launch {
            try {
                personajes.clear()
                ProveedorPersonaje.cargarPersonajes(ProveedorUsuario.usuarioLogeado!!)
                cargarPersonajes()
            } catch (e: Exception) {}
        }
    }

    fun vaciarPersonajes() {
        viewModelScope.launch {
            personajes.clear()
            _sinPersonajes.value = false
        }
    }

    fun modificarSinPersonajes(sinPersonajes: Boolean) {
        _sinPersonajes.value = sinPersonajes
    }

    fun filtrarPersonajes(textoBusqueda: String) {
        personajes.clear()
        personajesInicial.clear()

        // si la cadena recibida está vacía devuelve la lista completa
        if (textoBusqueda.isEmpty()) {
            cargarPersonajes()
        } else {
            ProveedorPersonaje.personajes.forEach { personaje ->
                if ((personaje.nombre!!.lowercase().contains(textoBusqueda.lowercase()) && personaje.nombre != "Sin nombre")
                    || (personaje.raza!!.lowercase().contains(textoBusqueda.lowercase()) && personaje.raza != "Sin raza")
                    || (personaje.subraza!!.lowercase().contains(textoBusqueda.lowercase()) && personaje.subraza != "Sin subraza")
                    || (personaje.clase!!.lowercase().contains(textoBusqueda.lowercase()) && personaje.clase != "Sin clase")
                    || (personaje.subclase!!.lowercase().contains(textoBusqueda.lowercase()) && personaje.subclase != "Sin subclase")
                ) {
                    personajes.add(personaje)
                }
            }

            var ultimaLetra = " "
            personajes.forEach { personaje ->
                if (!personaje.nombre!!.startsWith(ultimaLetra)) {
                    ultimaLetra = personaje.nombre.first().toString()
                    personajesInicial.add(personaje)
                }
            }
        }
    }

    fun cargarPersonaje(id: Int?) {
        viewModelScope.launch {
            ProveedorPersonaje.cargarPersonaje(id)
            _personaje.value = ProveedorPersonaje.personaje
            _personajeTraducido.value = ProveedorPersonaje.personajeTraducido
        }
    }

    fun cargarRazas() {
        razas.addAll(ProveedorPersonaje.razas)
    }

    fun cargarIdiomas() {
        idiomas.addAll(ProveedorPersonaje.idiomas)
    }

    fun cargarClases() {
        clases.addAll(ProveedorPersonaje.clases)
    }

    private val _crearPersonajeResultado = mutableStateOf("")
    val crearPersonajeResultado = _crearPersonajeResultado

    fun modificarCrearPersonajeResultado(crearPersonajeResultado: String) {
        _crearPersonajeResultado.value = crearPersonajeResultado
    }

    fun crearPersonaje(nombre: String, raza: String?, subraza: String?, hp: Int, hpAuto: Boolean, fuerza: Int, destreza: Int, constitucion: Int, inteligencia: Int, sabiduria: Int, carisma: Int, edad: Int, idiomaSeleccionado: String?, alineamiento: String, clase: String?, subclase: String?, nivel: Int, competenciasSeleccionadas: List<List<String>>, trucosConocidos: List<String>, conjurosConocidos: List<String>, imagen: String?) {
        viewModelScope.launch {
            _crearPersonajeResultado.value = ProveedorPersonaje.crearPersonaje(nombre, raza, subraza, hp, hpAuto, fuerza, destreza, constitucion, inteligencia, sabiduria, carisma, edad, idiomaSeleccionado, alineamiento, clase, subclase, nivel, competenciasSeleccionadas, trucosConocidos, conjurosConocidos, imagen)
        }
    }

    private val _eliminarPersonajeResultado = mutableStateOf("")
    val eliminarPersonajeResultado = _eliminarPersonajeResultado

    fun modificarEliminarPersonajeResultado(eliminarPersonajeResultado: String) {
        _eliminarPersonajeResultado.value = eliminarPersonajeResultado
    }

    fun eliminarPersonaje(id: Int) {
        viewModelScope.launch {
            _eliminarPersonajeResultado.value = ProveedorPersonaje.eliminarPersonaje(id)
        }
    }

    private val _eliminarPersonajesMultiplesResultado = mutableStateOf("")
    val eliminarPersonajesMultiplesResultado = _eliminarPersonajesMultiplesResultado

    fun modificarEliminarPersonajesMultiplesResultado(eliminarPersonajesMultiplesResultado: String) {
        _eliminarPersonajesMultiplesResultado.value = eliminarPersonajesMultiplesResultado
    }

    fun eliminarPersonajesMultiples(idsPersonajes: List<Int>) {
        viewModelScope.launch {
            _eliminarPersonajesMultiplesResultado.value = ProveedorPersonaje.eliminarPersonajesMultiples(idsPersonajes)
        }
    }

    private val _modificarPersonajeResultado = mutableStateOf("")
    val modificarPersonajeResultado = _modificarPersonajeResultado

    fun modificarModificarPersonajeResultado(modificarPersonajeResultado: String) {
        _modificarPersonajeResultado.value = modificarPersonajeResultado
    }

    fun modificarPersonaje(id: Int, nombre: String, raza: String?, subraza: String?, hp: Int, hpAuto: Boolean, fuerza: Int, destreza: Int, constitucion: Int, inteligencia: Int, sabiduria: Int, carisma: Int, edad: Int, idiomaSeleccionado: String?, alineamiento: String, clase: String?, subclase: String?, nivel: Int, competenciasSeleccionadas: List<List<String>>, trucosConocidos: List<String>, conjurosConocidos: List<String>, imagen: String?) {
        viewModelScope.launch {
            _modificarPersonajeResultado.value = ProveedorPersonaje.modificarPersonaje(id, nombre, raza, subraza, hp, hpAuto, fuerza, destreza, constitucion, inteligencia, sabiduria, carisma, edad, idiomaSeleccionado, alineamiento, clase, subclase, nivel, competenciasSeleccionadas, trucosConocidos, conjurosConocidos, imagen)
            _personaje.value = ProveedorPersonaje.personaje
            _personajeTraducido.value = ProveedorPersonaje.personajeTraducido
        }
    }

    private val _modoEdicion = mutableStateOf(false)
    val modoEdicion = _modoEdicion

    fun activarModoEdicion(activar: Boolean) {
        _modoEdicion.value = activar
    }

    private val _mostrarDialogo = mutableStateOf(false)
    val mostrarDialogo = _mostrarDialogo

    fun mostrarDialogo(mostrar: Boolean) {
        _mostrarDialogo.value = mostrar
    }
}