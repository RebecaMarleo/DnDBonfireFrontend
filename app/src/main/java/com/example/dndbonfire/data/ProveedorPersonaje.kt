package com.example.dndbonfire.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.dndbonfire.data.ProveedorUsuario.Companion.comprobarToken
import com.example.dndbonfire.json
import com.example.dndbonfire.modelo.Clase
import com.example.dndbonfire.modelo.DatosEquipamientoInicial
import com.example.dndbonfire.modelo.Descripcion
import com.example.dndbonfire.modelo.Eleccion
import com.example.dndbonfire.modelo.EquipamientoInicial
import com.example.dndbonfire.modelo.Idioma
import com.example.dndbonfire.modelo.InformacionConjuros
import com.example.dndbonfire.modelo.Item
import com.example.dndbonfire.modelo.LanzamientoDeConjurosClase
import com.example.dndbonfire.modelo.ListaClases
import com.example.dndbonfire.modelo.ListaConjurosDeClase
import com.example.dndbonfire.modelo.ListaIdiomas
import com.example.dndbonfire.modelo.ListaRazas
import com.example.dndbonfire.modelo.Multiclase
import com.example.dndbonfire.modelo.Nivel
import com.example.dndbonfire.modelo.Opcion
import com.example.dndbonfire.modelo.Opciones
import com.example.dndbonfire.modelo.OpcionesEquipamientoInicial
import com.example.dndbonfire.modelo.Personaje
import com.example.dndbonfire.modelo.Rasgo
import com.example.dndbonfire.modelo.Raza
import com.example.dndbonfire.modelo.RespuestaError
import com.example.dndbonfire.modelo.ResumenClase
import com.example.dndbonfire.modelo.ResumenCompetencia
import com.example.dndbonfire.modelo.ResumenConjuroDeClase
import com.example.dndbonfire.modelo.ResumenRaza
import com.example.dndbonfire.modelo.ResumenSubclase
import com.example.dndbonfire.modelo.ResumenSubraza
import com.example.dndbonfire.modelo.Subclase
import com.example.dndbonfire.modelo.Subraza
import com.example.dndbonfire.modelo.Usuario
import com.example.dndbonfire.urlBonfireApi
import com.example.dndbonfire.urlDnDApi
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class ProveedorPersonaje {
    companion object{
        private var appContext: Context? = null
        private lateinit var pref: SharedPreferences
        private var _usuarioLogeado: Usuario? = null

        private var _personajes: MutableList<Personaje>? = null
        private var _sinPersonajes: Boolean = false
        private var _personaje: Personaje? = null
        private var _personajeTraducido: Personaje? = null
        private var _razas: MutableList<ResumenRaza>? = null
        private var _idiomas: MutableList<Idioma>? = null
        private var _idioma: Idioma? = null
        private var _clases: MutableList<ResumenClase>? = null
        private var _descripcion: Descripcion? = null
        private var _descripcionTraducida: Descripcion? = null
        private var _conjuros: MutableList<ResumenConjuroDeClase>? = null

        private var traductorInglesEspanol: Translator? = null

        fun inicializar(pref: SharedPreferences, context: Context) {
            appContext = context.applicationContext
            this.pref = pref
            _usuarioLogeado = DatosUsuario.cargarUsuarioLogeado(pref)
            inicializarTraductor()
        }

        val personajes: MutableList<Personaje>
            get() = _personajes ?: mutableListOf()
        val sinPersonajes: Boolean
            get() = _sinPersonajes

        val personaje: Personaje?
            get() = _personaje
        val personajeTraducido: Personaje?
            get() = _personajeTraducido

        private fun inicializarTraductor() {
            val opciones = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build()
            traductorInglesEspanol = Translation.getClient(opciones)
        }

        val razas: MutableList<ResumenRaza>
            get() = _razas ?: mutableListOf()

        suspend fun cargarRazas() {
            return withContext(Dispatchers.IO) {
                try {
                    val url = "$urlDnDApi/api/2014/races"
                    val json = URL(url).readText()
                    val respuesta = Json.decodeFromString<ListaRazas>(json)
                    val lista = respuesta.results.toMutableList()
                    _razas = traducirNombresRazas(lista)
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _razas = mutableListOf()
                }
            }
        }

        private suspend fun traducirNombresRazas(
            razas: List<ResumenRaza>
        ): MutableList<ResumenRaza> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                razas.map { raza ->
                    ResumenRaza(
                        index = raza.index,
                        name = traduccionesRazas[raza.index]!!,
                        url = raza.url
                    )
                }
                    .sortedBy { it.name }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                razas.toMutableList()
            }
        }

        val traduccionesRazas = mapOf(
            "dragonborn" to "Dracónido",
            "dwarf" to "Enano",
            "elf" to "Elfo",
            "gnome" to "Gnomo",
            "half-elf" to "Semielfo",
            "half-orc" to "Medio orco",
            "halfling" to "Mediano",
            "human" to "Humano",
            "tiefling" to "Tiefling",

            "high-elf" to "Alto elfo",
            "hill-dwarf" to "Enano de la colina",
            "lightfoot-halfling" to "Mediano piesligeros",
            "rock-gnome" to "Gnomo de la roca"
        )

        suspend fun cargarRazaSeleccionada(idRaza: String): Raza? {
            return withContext(Dispatchers.IO) {
                try {
                    if (idRaza.isEmpty()) {
                        null
                    } else {
                        val url = "$urlDnDApi/api/2014/races/$idRaza"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Raza>(json)
                        respuesta
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        suspend fun cargarRazaSeleccionadaTraducida(raza: Raza?): Raza? {
            return withContext(Dispatchers.IO) {
                try {
                    if (raza == null) {
                        null
                    } else {
                        val razaTraducida = traducirDatosRaza(raza)
                        razaTraducida
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        private suspend fun traducirDatosRaza(
            raza: Raza
        ): Raza? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                val idiomas = raza.languages.map {
                    Idioma(
                        index = it.index,
                        name = traduccionesIdiomas[it.index]!!,
                        url = it.url
                    )
                }

                val rasgos = raza.traits.map {
                    Rasgo(
                        index = it.index,
                        name = traductorInglesEspanol
                            ?.translate(it.name)
                            ?.await() ?: it.name,
                        url = it.url
                    )
                }

                val subrazas = raza.subraces.map {
                    ResumenSubraza(
                        index = it.index,
                        name = traduccionesRazas[it.index]!!,
                        url = it.url
                    )
                }

                Raza(
                    index = raza.index,
                    name = traduccionesRazas[raza.index]!!,
                    speed = raza.speed,
                    ability_bonuses = raza.ability_bonuses,
                    age = traductorInglesEspanol
                        ?.translate(raza.age)
                        ?.await() ?: raza.age,
                    alignment = traductorInglesEspanol
                        ?.translate(raza.alignment)
                        ?.await() ?: raza.alignment,
                    size = traductorInglesEspanol
                        ?.translate(raza.size)
                        ?.await() ?: raza.size,
                    size_description = traductorInglesEspanol
                        ?.translate(raza.size_description)
                        ?.await() ?: raza.size_description,
                    languages = idiomas,
                    language_desc = traductorInglesEspanol
                        ?.translate(raza.language_desc)
                        ?.await() ?: raza.language_desc,
                    traits = rasgos,
                    subraces = subrazas,
                    url = raza.url
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                null
            }
        }

        suspend fun cargarSubrazaSeleccionada(idSubraza: String): Subraza? {
            return withContext(Dispatchers.IO) {
                try {
                    if (idSubraza.isEmpty()) {
                        null
                    } else {
                        val url = "$urlDnDApi/api/2014/subraces/$idSubraza"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Subraza>(json)
                        respuesta
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        suspend fun cargarSubrazaSeleccionadaTraducida(subraza: Subraza?): Subraza? {
            return withContext(Dispatchers.IO) {
                try {
                    if (subraza == null) {
                        null
                    } else {
                        val subrazaTraducida = traducirDatosSubraza(subraza)
                        subrazaTraducida
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        private suspend fun traducirDatosSubraza(
            subraza: Subraza
        ): Subraza? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                val rasgosRaciales = subraza.racial_traits.map {
                    Rasgo(
                        index = it.index,
                        name = traductorInglesEspanol
                            ?.translate(it.name)
                            ?.await() ?: it.name,
                        url = it.url
                    )
                }

                Subraza(
                    index = subraza.index,
                    name = traductorInglesEspanol
                        ?.translate(subraza.name)
                        ?.await() ?: subraza.name,
                    race = ResumenRaza(
                        index = subraza.race.index,
                        name = traductorInglesEspanol
                            ?.translate(subraza.race.name)
                            ?.await() ?: subraza.race.name,
                        url = subraza.race.url
                    ),
                    desc = traductorInglesEspanol
                        ?.translate(subraza.desc)
                        ?.await() ?: subraza.desc,
                    ability_bonuses = subraza.ability_bonuses,
                    racial_traits = rasgosRaciales,
                    url = subraza.url
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                null
            }
        }

        val idiomas: MutableList<Idioma>
            get() = _idiomas ?: mutableListOf()

        suspend fun cargarIdiomas() {
            return withContext(Dispatchers.IO) {
                try {
                    val url = "$urlDnDApi/api/2014/languages"
                    val json = URL(url).readText()
                    val respuesta = Json.decodeFromString<ListaIdiomas>(json)
                    val lista = respuesta.results.toMutableList()
                    _idiomas = traducirNombresIdiomas(lista)
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _idiomas = mutableListOf()
                }
            }
        }

        private suspend fun traducirNombresIdiomas(
            idiomas: List<Idioma>
        ): MutableList<Idioma> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                idiomas.map { idioma ->
                    Idioma(
                        index = idioma.index,
                        name = traduccionesIdiomas[idioma.index]!!,
                        url = idioma.url
                    )
                }
                    .sortedBy { it.name }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                idiomas.toMutableList()
            }
        }

        private val traduccionesIdiomas = mapOf(
            "abyssal" to "Abisal",
            "celestial" to "Celestial",
            "common" to "Común",
            "deep-speech" to "Lengua Profunda",
            "draconic" to "Dracónico",
            "dwarvish" to "Enano",
            "elvish" to "Élfico",
            "giant" to "Gigante",
            "gnomish" to "Gnomo",
            "goblin" to "Goblin",
            "halfling" to "Mediano",
            "infernal" to "Infernal",
            "orc" to "Orco",
            "primordial" to "Primordial",
            "sylvan" to "Silvano",
            "undercommon" to "Subcomún"
        )

        suspend fun cargarIdiomaSeleccionado(idIdioma: String): Idioma? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return withContext(Dispatchers.IO) {
                try {
                    if (idIdioma.isEmpty()) {
                        _idioma = null
                    } else {
                        val url = "$urlDnDApi/api/2014/languages/$idIdioma"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Idioma>(json)
                        _idioma = Idioma(
                            index = respuesta.index,
                            name = traduccionesIdiomas[respuesta.index]!!,
                            url = respuesta.url
                        )
                    }
                    _idioma
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        val clases: MutableList<ResumenClase>
            get() = _clases ?: mutableListOf()

        suspend fun cargarClases() {
            return withContext(Dispatchers.IO) {
                try {
                    val url = "$urlDnDApi/api/2014/classes"
                    val json = URL(url).readText()
                    val respuesta = Json.decodeFromString<ListaClases>(json)
                    val lista = respuesta.results.toMutableList()
                    _clases = traducirNombresClases(lista)
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _clases = mutableListOf()
                }
            }
        }

        private suspend fun traducirNombresClases(
            clases: List<ResumenClase>
        ): MutableList<ResumenClase> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                clases.map { clase ->
                    ResumenClase(
                        index = clase.index,
                        name = traduccionesClases[clase.index]!!,
                        url = clase.url
                    )
                }
                    .sortedBy { it.name }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                clases.toMutableList()
            }
        }

        val traduccionesClases = mapOf(
            "barbarian" to "Bárbaro",
            "bard" to "Bardo",
            "cleric" to "Clérigo",
            "druid" to "Druida",
            "fighter" to "Luchador",
            "monk" to "Monje",
            "paladin" to "Paladín",
            "ranger" to "Explorador",
            "rogue" to "Pícaro",
            "sorcerer" to "Hechicero",
            "warlock" to "Brujo",
            "wizard" to "Mago",

            "berserker" to "Senda del Berserker",
            "champion" to "Campeón",
            "devotion" to "Juramento de Devoción",
            "draconic" to "Linaje del Dragón",
            "evocation" to "Escuela de la Evocación",
            "fiend" to "Ser infernal",
            "hunter" to "Cazador",
            "land" to "Círculo de la Tierra",
            "life" to "Dominio de la Vida",
            "lore" to "Colegio del Conocimiento",
            "open-hand" to "Camino de la Mano Abierta",
            "thief" to "Ladrón"
        )

        suspend fun cargarClaseSeleccionada(idClase: String): Clase? {
            return withContext(Dispatchers.IO) {
                try {
                    if (idClase.isEmpty()) {
                        null
                    } else {
                        val url = "$urlDnDApi/api/2014/classes/$idClase"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Clase>(json)
                        respuesta
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        suspend fun cargarClaseSeleccionadaTraducida(clase: Clase?): Clase? {
            return withContext(Dispatchers.IO) {
                try {
                    if (clase == null) {
                        null
                    } else {
                        val claseTraducida = traducirDatosClase(clase)
                        claseTraducida
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        private suspend fun traducirDatosClase(
            clase: Clase
        ): Clase? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                val eleccionDeCompetencias = clase.proficiency_choices.map {
                    Eleccion(
                        desc = traductorInglesEspanol
                            ?.translate(it.desc)
                            ?.await() ?: it.desc,
                        choose = it.choose,
                        type = it.type,
                        from = Opciones(
                            option_set_type = it.from.option_set_type,
                            options = it.from.options?.map { opcion ->
                                Opcion(
                                    option_type = opcion.option_type,
                                    item = opcion.item?.let { item ->
                                        Item(
                                            index = item.index,
                                            name = traductorInglesEspanol
                                                ?.translate(item.name)
                                                ?.await() ?: item.name,
                                            url = item.url
                                        )
                                    },
                                    choice = opcion.choice?.let {
                                        Eleccion(
                                            desc = traductorInglesEspanol
                                                ?.translate(opcion.choice.desc)
                                                ?.await() ?: opcion.choice.desc,
                                            type = opcion.choice.type,
                                            choose = opcion.choice.choose,
                                            from = Opciones(
                                                option_set_type = opcion.choice.from.option_set_type,
                                                options = opcion.choice.from.options?.map { opcion ->
                                                    Opcion(
                                                        option_type = opcion.option_type,
                                                        item = opcion.item?.let { item ->
                                                            Item(
                                                                index = item.index,
                                                                name = traductorInglesEspanol
                                                                    ?.translate(item.name)
                                                                    ?.await() ?: item.name,
                                                                url = item.url
                                                            )
                                                        }
                                                    )
                                                }
                                            )
                                        )
                                    }
                                )
                            }
                        )
                    )
                }

                val competencias = clase.proficiencies.map {
                    ResumenCompetencia(
                        index = it.index,
                        name = traductorInglesEspanol
                            ?.translate(it.name)
                            ?.await() ?: it.name,
                        url = it.url
                    )
                }

                val equipamientoInicial = clase.starting_equipment.map {
                    EquipamientoInicial(
                        equipment = DatosEquipamientoInicial(
                            index = it.equipment.index,
                            name = traductorInglesEspanol
                                ?.translate(it.equipment.name)
                                ?.await() ?: it.equipment.name,
                            url = it.equipment.url
                        ),
                        quantity = it.quantity
                    )
                }

                val opcionesEquipamientoInicial = clase.starting_equipment_options.map {
                    OpcionesEquipamientoInicial(
                        desc = traductorInglesEspanol
                            ?.translate(it.desc)
                            ?.await() ?: it.desc,
                        choose = it.choose,
                        type = it.type,
                        from = Opciones(
                            option_set_type = it.from.option_set_type,
                            options = it.from.options?.map { opcion ->
                                Opcion(
                                    option_type = opcion.option_type,
                                    count = opcion.count,
                                    of = opcion.of?.let { item ->
                                        Item(
                                            index = item.index,
                                            name = traductorInglesEspanol
                                                ?.translate(item.name)
                                                ?.await() ?: item.name,
                                            url = item.name
                                        )
                                    }
                                )
                            },
                            equipment_category = it.from.equipment_category
                        )
                    )
                }

                val multiclase = Multiclase(
                    prerequisites = clase.multi_classing.prerequisites,
                    proficiencies = clase.multi_classing.proficiencies.map {
                        ResumenCompetencia(
                            index = it.index,
                            name = traductorInglesEspanol
                                ?.translate(it.name)
                                ?.await() ?: it.name,
                            url = it.url
                        )
                    }
                )

                val subclases = clase.subclasses.map {
                    ResumenSubclase(
                        index = it.index,
                        name = traduccionesClases[it.index]!!,
                        url = it.url
                    )
                }

                val lanzamientoDeConjuros = clase.spellcasting?.let { lanzamiento ->
                    LanzamientoDeConjurosClase(
                        level = lanzamiento.level,
                        spellcasting_ability = lanzamiento.spellcasting_ability,
                        info = lanzamiento.info.map {
                            InformacionConjuros(
                                name = traductorInglesEspanol
                                    ?.translate(it.name)
                                    ?.await() ?: it.name,
                                desc = it.desc.map { desc ->
                                    traductorInglesEspanol
                                        ?.translate(desc)
                                        ?.await() ?: desc
                                }
                            )
                        }
                    )
                }

                Clase(
                    index = clase.index,
                    name = traduccionesClases[clase.index]!!,
                    hit_die = clase.hit_die,
                    proficiency_choices = eleccionDeCompetencias,
                    proficiencies = competencias,
                    saving_throws = clase.saving_throws,
                    starting_equipment = equipamientoInicial,
                    starting_equipment_options = opcionesEquipamientoInicial,
                    class_levels = clase.class_levels,
                    multi_classing = multiclase,
                    subclasses = subclases,
                    spellcasting = lanzamientoDeConjuros,
                    spells = clase.spells,
                    url = clase.url
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                null
            }
        }

        suspend fun cargarSubclaseSeleccionada(idSubclase: String): Subclase? {
            return withContext(Dispatchers.IO) {
                try {
                    if (idSubclase.isEmpty()) {
                        null
                    } else {
                        val url = "$urlDnDApi/api/2014/subclasses/$idSubclase"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Subclase>(json)
                        respuesta
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        suspend fun cargarSubclaseSeleccionadaTraducida(subclase: Subclase?): Subclase? {
            return withContext(Dispatchers.IO) {
                try {
                    if (subclase == null) {
                        null
                    } else {
                        val subclaseTraducida = traducirDatosSubclase(subclase)
                        subclaseTraducida
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        private suspend fun traducirDatosSubclase(
            subclase: Subclase
        ): Subclase? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                val clasePrincipal = ResumenClase(
                    index = subclase.clase.index,
                    name = traduccionesClases[subclase.clase.index]!!,
                    url = subclase.clase.url
                )

                val descripcion = subclase.desc.map {
                    traductorInglesEspanol
                        ?.translate(it)
                        ?.await() ?: it
                }

                Subclase(
                    index = subclase.index,
                    clase = clasePrincipal,
                    name = traduccionesClases[subclase.index]!!,
                    subclass_flavor = traductorInglesEspanol
                        ?.translate(subclase.subclass_flavor)
                        ?.await() ?: subclase.subclass_flavor,
                    desc = descripcion,
                    subclass_levels = subclase.subclass_levels,
                    url = subclase.url
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                null
            }
        }

        suspend fun cargarDescripcionSeleccionada(url: String): Descripcion? {
            return withContext(Dispatchers.IO) {
                try {
                    if (url.isEmpty()) {
                        _descripcion = null
                    } else {
                        val url = "$urlDnDApi$url"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Descripcion>(json)
                        _descripcion = respuesta
                    }
                    _descripcion
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        suspend fun cargarDescripcionSeleccionadaTraducida(descripcion: Descripcion?): Descripcion? {
            return withContext(Dispatchers.IO) {
                try {
                    if (descripcion == null) {
                        _descripcionTraducida = null
                    } else {
                        val descripcionTraducida = traducirDescripcion(descripcion)
                        _descripcionTraducida = descripcionTraducida
                    }
                    _descripcionTraducida
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        private suspend fun traducirDescripcion(
            descripcion: Descripcion
        ): Descripcion? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                Descripcion(
                    index = descripcion.index,
                    desc = descripcion.desc.map {
                        traductorInglesEspanol
                            ?.translate(it)
                            ?.await() ?: it
                    },
                    url = descripcion.url
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                null
            }
        }

        suspend fun cargarInfoNiveles(clase: String): MutableList<Nivel> {
            return withContext(Dispatchers.IO) {
                try {
                    if (clase.isEmpty()) {
                        mutableListOf()
                    } else {
                        val url = "$urlDnDApi/api/2014/classes/$clase/levels"
                        val json = URL(url).readText()
                        val respuesta = Json.decodeFromString<List<Nivel>>(json)
                        val lista = respuesta.toMutableList()
                        val infoNiveles = traducirInfoNiveles(lista)

                        infoNiveles.toMutableList()
                    }
                } catch (e: Exception) {
                    Log.e("ERROR2", e.stackTraceToString())
                    mutableListOf()
                }
            }
        }

        private suspend fun traducirInfoNiveles(
            infoNiveles: List<Nivel>
        ): MutableList<Nivel> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                infoNiveles.map { infoNivel ->
                    val clase = ResumenClase(
                        index = infoNivel.clase.index,
                        name = traduccionesClases[infoNivel.clase.index]!!,
                        url = infoNivel.clase.url
                    )

                    Nivel(
                        level = infoNivel.level,
                        ability_score_bonuses = infoNivel.ability_score_bonuses,
                        prof_bonus = infoNivel.prof_bonus,
                        features = infoNivel.features.map {
                            Rasgo(
                                index = it.index,
                                name = traductorInglesEspanol
                                    ?.translate(it.name)
                                    ?.await() ?: it.name,
                                url = it.url
                            )
                        },
                        spellcasting = infoNivel.spellcasting,
                        class_specific = infoNivel.class_specific,
                        index = infoNivel.index,
                        clase = clase,
                        url = infoNivel.url,
                        updated_at = infoNivel.updated_at
                    )
                }
                    .sortedBy { it.level }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                infoNiveles.toMutableList()
            }
        }

        val conjuros: MutableList<ResumenConjuroDeClase>
            get() = _conjuros ?: mutableListOf()

        suspend fun cargarConjuros(clase: String): MutableList<ResumenConjuroDeClase> {
            _conjuros?.clear()

            return withContext(Dispatchers.IO) {
                try {
                    if (clase.isEmpty()) {
                        _conjuros = mutableListOf()
                        mutableListOf()
                    } else {
                        val url = "$urlDnDApi/api/2014/classes/$clase/spells"
                        val json = URL(url).readText()
                        val respuesta = Json.decodeFromString<ListaConjurosDeClase>(json)
                        val lista = respuesta.results.toMutableList()
                        _conjuros = traducirNombresConjuros(lista)
                        _conjuros!!.toMutableList()
                    }
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _conjuros = mutableListOf()
                    mutableListOf()
                }
            }
        }

        private suspend fun traducirNombresConjuros(
            conjuros: List<ResumenConjuroDeClase>
        ): MutableList<ResumenConjuroDeClase> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                conjuros.map { conjuro ->
                    ResumenConjuroDeClase(
                        index = conjuro.index,
                        name = traductorInglesEspanol
                                ?.translate(conjuro.name)
                                ?.await() ?: conjuro.name,
                        level = conjuro.level,
                        url = conjuro.url
                    )
                }
                    .sortedBy { it.name }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                conjuros.toMutableList()
            }
        }

        private suspend fun traducirDatosPersonajes(
            personajes: List<Personaje>
        ): MutableList<Personaje> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                personajes.map {
                    traducirDatosPersonaje(it)
                }
                    .sortedBy {
                        if (it.nombre == "Sin nombre") "ZZZZZZ"
                        else it.nombre
                    }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                personajes.toMutableList()
            }
        }

        private suspend fun traducirDatosPersonaje(
            personaje: Personaje
        ): Personaje {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                Personaje(
                    id = personaje.id,
                    nombre = personaje.nombre ?: "Sin nombre",
                    raza = traduccionesRazas[personaje.raza] ?: "Sin raza",
                    subraza = traduccionesRazas[personaje.subraza] ?: "Sin subraza",
                    hp = personaje.hp,
                    hpAuto = personaje.hpAuto,
                    fuerza = personaje.fuerza,
                    destreza = personaje.destreza,
                    constitucion = personaje.constitucion,
                    inteligencia = personaje.inteligencia,
                    sabiduria = personaje.sabiduria,
                    carisma = personaje.carisma,
                    edad = personaje.edad,
                    idiomaExtra = personaje.idiomaExtra,
                    alineamiento = personaje.alineamiento,
                    clase = traduccionesClases[personaje.clase] ?: "Sin clase",
                    subclase = traduccionesClases[personaje.subclase] ?: "Sin subclase",
                    nivel = personaje.nivel,
                    competenciasSeleccionadas = personaje.competenciasSeleccionadas,
                    trucosConocidos = personaje.trucosConocidos,
                    conjurosConocidos = personaje.conjurosConocidos,
                    imagen = personaje.imagen,
                    idUsuario = personaje.idUsuario
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                personaje
            }
        }

        suspend fun crearPersonaje(nombre: String, raza: String?, subraza: String?, hp: Int, hpAuto: Boolean, fuerza: Int, destreza: Int, constitucion: Int, inteligencia: Int, sabiduria: Int, carisma: Int, edad: Int, idiomaSeleccionado: String?, alineamiento: String, clase: String?, subclase: String?, nivel: Int, competenciasSeleccionadas: List<List<String>>, trucosConocidos: List<String>, conjurosConocidos: List<String>, imagen: String?): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/personaje/crear")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val nombreAjuste = if (nombre != "") "\"$nombre\"" else null
                    val razaAjuste = if (raza != null) "\"$raza\"" else null
                    val subrazaAjuste = if (subraza != null) "\"$subraza\"" else null
                    val idiomaExtraAjuste = if (idiomaSeleccionado != null) "\"$idiomaSeleccionado\"" else null
                    val alineamientoAjuste = if (alineamiento != "") "\"$alineamiento\"" else null
                    val claseAjuste = if (clase != null) "\"$clase\"" else null
                    val subclaseAjuste = if (subclase != null) "\"$subclase\"" else null
                    val competenciasAjuste = competenciasSeleccionadas.map { lista ->
                        lista.map { "\"$it\"" }
                    }
                    val trucosAjuste = trucosConocidos.map { "\"$it\"" }
                    val conjurosAjuste = conjurosConocidos.map { "\"$it\"" }
                    val imagenAjuste = if (imagen != null) "\"$imagen\"" else null
//                    val tokenAjuste = if (_usuarioLogeado?.token != "") "\"${_usuarioLogeado?.token}\"" else null
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "id": 0,
                        "nombre": $nombreAjuste,
                        "raza": $razaAjuste,
                        "subraza": $subrazaAjuste,
                        "hp": $hp,
                        "hpAuto": $hpAuto,
                        "fuerza": $fuerza,
                        "destreza": $destreza,
                        "constitucion": $constitucion,
                        "inteligencia": $inteligencia,
                        "sabiduria": $sabiduria,
                        "carisma": $carisma,
                        "edad": $edad,
                        "idiomaExtra": $idiomaExtraAjuste,
                        "alineamiento": $alineamientoAjuste,
                        "clase": $claseAjuste,
                        "subclase": $subclaseAjuste,
                        "nivel": $nivel,
                        "competenciasSeleccionadas": $competenciasAjuste,
                        "trucosConocidos": $trucosAjuste,
                        "conjurosConocidos": $conjurosAjuste,
                        "imagen": $imagenAjuste,
                        "idUsuario": ${_usuarioLogeado?.id}
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

        suspend fun eliminarPersonaje(id: Int): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/personaje/eliminar/$id")

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

        suspend fun eliminarPersonajesMultiples(idsPersonajes: List<Int>): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val query = idsPersonajes.joinToString("&") { "id=$it" }
                    val url = URL("$urlBonfireApi/api/personaje/eliminarMultiples?$query")

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

        suspend fun modificarPersonaje(id: Int, nombre: String, raza: String?, subraza: String?, hp: Int, hpAuto: Boolean, fuerza: Int, destreza: Int, constitucion: Int, inteligencia: Int, sabiduria: Int, carisma: Int, edad: Int, idiomaSeleccionado: String?, alineamiento: String, clase: String?, subclase: String?, nivel: Int, competenciasSeleccionadas: List<List<String>>, trucosConocidos: List<String>, conjurosConocidos: List<String>, imagen: String?): String {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/personaje/modificar/$id")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "PUT"
                    connection.setDoOutput(true)
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val nombreAjuste = if (nombre != "") "\"$nombre\"" else null
                    val razaAjuste = if (raza != null) "\"$raza\"" else null
                    val subrazaAjuste = if (subraza != null) "\"$subraza\"" else null
                    val idiomaExtraAjuste = if (idiomaSeleccionado != null) "\"$idiomaSeleccionado\"" else null
                    val alineamientoAjuste = if (alineamiento != "") "\"$alineamiento\"" else null
                    val claseAjuste = if (clase != null) "\"$clase\"" else null
                    val subclaseAjuste = if (subclase != null) "\"$subclase\"" else null
                    val competenciasAjuste = competenciasSeleccionadas.map { lista ->
                        lista.map { "\"$it\"" }
                    }
                    val trucosAjuste = trucosConocidos.map { "\"$it\"" }
                    val conjurosAjuste = conjurosConocidos.map { "\"$it\"" }
                    val imagenAjuste = if (imagen != null) "\"$imagen\"" else null
//                    val tokenAjuste = if (_usuarioLogeado?.token != "") "\"${_usuarioLogeado?.token}\"" else null
                    // el id se sobreescribe después por un número incremental
                    val body = """{
                        "id": 0,
                        "nombre": $nombreAjuste,
                        "raza": $razaAjuste,
                        "subraza": $subrazaAjuste,
                        "hp": $hp,
                        "hpAuto": $hpAuto,
                        "fuerza": $fuerza,
                        "destreza": $destreza,
                        "constitucion": $constitucion,
                        "inteligencia": $inteligencia,
                        "sabiduria": $sabiduria,
                        "carisma": $carisma,
                        "edad": $edad,
                        "idiomaExtra": $idiomaExtraAjuste,
                        "alineamiento": $alineamientoAjuste,
                        "clase": $claseAjuste,
                        "subclase": $subclaseAjuste,
                        "nivel": $nivel,
                        "competenciasSeleccionadas": $competenciasAjuste,
                        "trucosConocidos": $trucosAjuste,
                        "conjurosConocidos": $conjurosAjuste,
                        "imagen": $imagenAjuste,
                        "idUsuario": ${_usuarioLogeado?.id}
                        }""".trimIndent()
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        _personaje = json.decodeFromString<Personaje>(responseJson)
                        _personajeTraducido = traducirDatosPersonaje(_personaje!!)
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

        suspend fun cargarPersonajes(usuario: Usuario) {
            return withContext(Dispatchers.IO) {
                try {
                    _usuarioLogeado = comprobarToken()

                    val url = URL("$urlBonfireApi/api/personaje/cargar/porUsuario/${usuario.id}")

                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 2000
                    connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                    val responseCode = connection.responseCode

                    if (responseCode == 200) {
                        val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                        val lista = Json.decodeFromString<MutableList<Personaje>>(responseJson)
                        _personajes = traducirDatosPersonajes(lista)
                        _sinPersonajes = lista.isEmpty()
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

        suspend fun cargarPersonaje(id: Int?) {
            return withContext(Dispatchers.IO) {
                try {
                    if (id == null) {
                        _personaje = null
                    } else {
                        _usuarioLogeado = comprobarToken()

                        val url = URL("$urlBonfireApi/api/personaje/cargar/$id")

                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 2000
                        connection.setRequestProperty("Authorization", _usuarioLogeado?.token)

                        val responseCode = connection.responseCode

                        if (responseCode == 200) {
                            val responseJson = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                            _personaje = Json.decodeFromString<Personaje>(responseJson)
                            _personajeTraducido = traducirDatosPersonaje(_personaje!!)
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