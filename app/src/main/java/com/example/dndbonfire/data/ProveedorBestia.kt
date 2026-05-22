package com.example.dndbonfire.data

import android.content.Context
import android.util.Log
import com.example.dndbonfire.modelo.Accion
import com.example.dndbonfire.modelo.AccionLegendaria
import com.example.dndbonfire.modelo.Bestia
import com.example.dndbonfire.modelo.Bestiario
import com.example.dndbonfire.modelo.ClaseArmadura
import com.example.dndbonfire.modelo.Competencia
import com.example.dndbonfire.modelo.Condicion
import com.example.dndbonfire.modelo.Dano
import com.example.dndbonfire.modelo.Dc
import com.example.dndbonfire.modelo.Descripcion
import com.example.dndbonfire.modelo.Forma
import com.example.dndbonfire.modelo.HabilidadEspecial
import com.example.dndbonfire.modelo.LanzamientoDeConjuros
import com.example.dndbonfire.modelo.Reaccion
import com.example.dndbonfire.modelo.ResumenAccion
import com.example.dndbonfire.modelo.ResumenBestia
import com.example.dndbonfire.modelo.ResumenCompetencia
import com.example.dndbonfire.modelo.ResumenConjuro
import com.example.dndbonfire.modelo.TipoDano
import com.example.dndbonfire.modelo.TipoDc
import com.example.dndbonfire.modelo.Uso
import com.example.dndbonfire.urlDnDApi
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL
import kotlin.String

class ProveedorBestia {
    companion object {
        private var appContext: Context? = null
        private var traductorInglesEspanol: Translator? = null
        private var _bestias: MutableList<ResumenBestia>? = null
        private var _bestia: Bestia? = null
        private var _bestiaTraducida: Bestia? = null
        private var _descripcion: Descripcion? = null
        private var _descripcionTraducida: Descripcion? = null

        fun inicializar(context: Context) {
            appContext = context.applicationContext
            inicializarTraductor()
        }

        private fun inicializarTraductor() {
            val opciones = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build()
            traductorInglesEspanol = Translation.getClient(opciones)
        }

        val bestias: MutableList<ResumenBestia>
            get() = _bestias ?: mutableListOf()

        suspend fun cargarBestias() {
            return withContext(Dispatchers.IO) {
                try {
                    val url = "$urlDnDApi/api/2014/monsters"
                    val json = URL(url).readText()
                    val respuesta = Json.decodeFromString<Bestiario>(json)
                    val lista = respuesta.results.toMutableList()
                    _bestias = traducirNombresBestias(lista)
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    _bestias = mutableListOf()
                }
            }
        }

        private suspend fun traducirNombresBestias(
            bestias: List<ResumenBestia>
        ): MutableList<ResumenBestia> {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                bestias.map { bestia ->
                    ResumenBestia(
                        index = bestia.index,
                        name = traductorInglesEspanol
                            ?.translate(bestia.name)
                            ?.await() ?: bestia.name,
                        url = bestia.url
                    )
                }
                    .sortedBy { it.name }
                    .toMutableList()
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                bestias.toMutableList()
            }
        }

        suspend fun cargarBestiaSeleccionada(idBestia: String): Bestia? {
            return withContext(Dispatchers.IO) {
                try {
                    if (idBestia.isEmpty()) {
                        _bestia = null
                    } else {
                        val url = "$urlDnDApi/api/2014/monsters/$idBestia"
                        val json = URL(url).readText()
                        val respuesta = Json { ignoreUnknownKeys = true }.decodeFromString<Bestia>(json)
                        _bestia = respuesta
                    }
                    _bestia
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        suspend fun cargarBestiaSeleccionadaTraducida(bestia: Bestia?): Bestia? {
            return withContext(Dispatchers.IO) {
                try {
                    if (bestia == null) {
                        _bestiaTraducida = null
                    } else {
                        val bestiaTraducida = traducirDatosBestia(bestia)
                        _bestiaTraducida = bestiaTraducida
                    }
                    _bestiaTraducida
                } catch (e: Exception) {
                    Log.e("ERROR", e.stackTraceToString())
                    null
                }
            }
        }

        private suspend fun traducirDatosBestia(
            bestia: Bestia
        ): Bestia? {
            traductorInglesEspanol?.downloadModelIfNeeded()?.await()

            return try {
                val claseDeArmadura = bestia.armor_class.map {
                    ClaseArmadura(
                        type = traductorInglesEspanol
                            ?.translate(it.type)
                            ?.await() ?: it.type,
                        value = it.value
                    )
                }

                val formas = bestia.forms?.let {
                    bestia.forms.map {
                        Forma(
                            index = it.index,
                            name = traductorInglesEspanol
                                ?.translate(it.name)
                                ?.await() ?: it.name,
                            url = it.url
                        )
                    }
                }

                val competencias = bestia.proficiencies.map {
                    Competencia(
                        value = it.value,
                        proficiency = ResumenCompetencia(
                            index = it.proficiency.index,
                            name = traductorInglesEspanol
                                ?.translate(it.proficiency.name)
                                ?.await() ?: it.proficiency.name,
                            url = it.proficiency.url
                        )
                    )
                }

                val vulnerabilidadesDeDano = bestia.damage_vulnerabilities.map {
                    traductorInglesEspanol
                        ?.translate(it)
                        ?.await() ?: it
                }

                val resistenciasDeDano = bestia.damage_resistances.map {
                    traductorInglesEspanol
                        ?.translate(it)
                        ?.await() ?: it
                }

                val inmunidadesDeDano = bestia.damage_immunities.map {
                    traductorInglesEspanol
                        ?.translate(it)
                        ?.await() ?: it
                }

                val inmunidadesACondiciones = bestia.condition_immunities.map {
                    Condicion(
                        index = it.index,
                        name = traductorInglesEspanol
                            ?.translate(it.name)
                            ?.await() ?: it.name,
                        url = it.url
                    )
                }

                val habilidadesEspeciales = bestia.special_abilities.map { hab ->
                    HabilidadEspecial(
                        name = traductorInglesEspanol
                            ?.translate(hab.name)
                            ?.await() ?: hab.name,
                        desc = traductorInglesEspanol
                            ?.translate(hab.desc)
                            ?.await() ?: hab.desc,
                        spellcasting = hab.spellcasting?.let { hech ->
                            LanzamientoDeConjuros(
                                level = hech.level,
                                ability = hech.ability,
                                dc = hech.dc,
                                modifier = hech.modifier,
                                components_required = hech.components_required,
                                school = traductorInglesEspanol
                                    ?.translate(hech.school)
                                    ?.await() ?: hech.school,
                                slots = hech.slots,
                                spells = hech.spells.map {
                                    ResumenConjuro(
                                        name = traductorInglesEspanol
                                            ?.translate(it.name)
                                            ?.await() ?: it.name,
                                        level = it.level,
                                        url = it.url
                                    )
                                }
                            )
                        },
                        usage = hab.usage?.let { uso ->
                            Uso(
                                type = uso.type,
                                times = uso.times,
                                rest_types = uso.rest_types.map {
                                    traductorInglesEspanol
                                        ?.translate(it)
                                        ?.await() ?: it
                                }
                            )
                        },
                        dc = hab.dc,
                        damage = hab.damage?.let { dmg ->
                            dmg.map {
                                Dano(
                                    damage_type = it.damage_type?.let { dmg ->
                                        TipoDano(
                                            dmg.index,
                                            traductorInglesEspanol
                                                ?.translate(dmg.name)
                                                ?.await() ?: dmg.name,
                                            dmg.url
                                        )
                                    },
                                    damage_dice = it.damage_dice
                                )
                            }
                        }
                    )
                }

                val acciones = bestia.actions.map { acc ->
                    Accion(
                        damage = acc.damage?.let { dmg ->
                            dmg.map {
                                Dano(
                                    damage_type = it.damage_type?.let { tipoDmg ->
                                        TipoDano(
                                            tipoDmg.index,
                                            traductorInglesEspanol
                                                ?.translate(tipoDmg.name)
                                                ?.await() ?: tipoDmg.name,
                                            tipoDmg.url
                                        )
                                    },
                                    damage_dice = it.damage_dice
                                )
                            }
                        },
                        name = traductorInglesEspanol
                            ?.translate(acc.name)
                            ?.await() ?: acc.name,
                        multiattack_type = acc.multiattack_type,
                        attack_bonus = acc.attack_bonus,
                        usage = acc.usage,
                        dc = acc.dc?.let {
                            Dc(
                                dc_type = TipoDc(
                                    index = it.dc_type.index,
                                    name = traductorInglesEspanol
                                        ?.translate(it.dc_type.name)
                                        ?.await() ?: it.dc_type.name,
                                    url = it.dc_type.url
                                ),
                                dc_value = it.dc_value,
                                success_type = it.success_type
                            )
                        },
                        desc = traductorInglesEspanol
                            ?.translate(acc.desc)
                            ?.await() ?: acc.desc,
                        actions = acc.actions.map {
                            ResumenAccion(
                                action_name = traductorInglesEspanol
                                    ?.translate(it.action_name)
                                    ?.await() ?: it.action_name,
                                count = it.count,
                                type = it.type
                            )
                        }
                    )
                }
                
                val accionesLegendarias = bestia.legendary_actions.map { acc ->
                    AccionLegendaria(
                        name = traductorInglesEspanol
                            ?.translate(acc.name)
                            ?.await() ?: acc.name,
                        desc = traductorInglesEspanol
                            ?.translate(acc.desc)
                            ?.await() ?: acc.desc,
                        dc = acc.dc?.let {
                            Dc(
                                dc_type = TipoDc(
                                    index = it.dc_type.index,
                                    name = traductorInglesEspanol
                                        ?.translate(it.dc_type.name)
                                        ?.await() ?: it.dc_type.name,
                                    url = it.dc_type.url
                                ),
                                dc_value = it.dc_value,
                                success_type = it.success_type
                            )
                        },
                        damage = acc.damage.map {
                            Dano(
                                damage_type = it.damage_type?.let { dmg ->
                                    TipoDano(
                                        dmg.index,
                                        traductorInglesEspanol
                                            ?.translate(dmg.name)
                                            ?.await() ?: dmg.name,
                                        dmg.url
                                    )
                                },
                                damage_dice = it.damage_dice
                            )
                        }
                    )
                }
                
                val reacciones = bestia.reactions.map {
                    Reaccion(
                        name = traductorInglesEspanol
                            ?.translate(it.name)
                            ?.await() ?: it.name,
                        desc = traductorInglesEspanol
                            ?.translate(it.desc)
                            ?.await() ?: it.desc
                    )
                }

                Bestia(
                    index = bestia.index,
                    name = traductorInglesEspanol
                        ?.translate(bestia.name)
                        ?.await() ?: bestia.name,
                    size = traductorInglesEspanol
                        ?.translate(bestia.size)
                        ?.await() ?: bestia.size,
                    type = traductorInglesEspanol
                        ?.translate(bestia.type)
                        ?.await() ?: bestia.type,
                    alignment = traductorInglesEspanol
                        ?.translate(bestia.alignment)
                        ?.await() ?: bestia.alignment,
                    armor_class = claseDeArmadura,
                    hit_points = bestia.hit_points,
                    hit_dice = bestia.hit_dice,
                    forms = formas,
                    hit_points_roll = bestia.hit_points_roll,
                    speed = bestia.speed,
                    strength = bestia.strength,
                    dexterity = bestia.dexterity,
                    constitution = bestia.constitution,
                    intelligence = bestia.intelligence,
                    wisdom = bestia.wisdom,
                    charisma = bestia.charisma,
                    proficiencies = competencias,
                    damage_vulnerabilities = vulnerabilidadesDeDano,
                    damage_resistances = resistenciasDeDano,
                    damage_immunities = inmunidadesDeDano,
                    condition_immunities = inmunidadesACondiciones,
                    senses = bestia.senses,
                    languages = traductorInglesEspanol
                        ?.translate(bestia.languages)
                        ?.await() ?: bestia.languages,
                    challenge_rating = bestia.challenge_rating,
                    proficiency_bonus = bestia.proficiency_bonus,
                    xp = bestia.xp,
                    special_abilities = habilidadesEspeciales,
                    actions = acciones,
                    legendary_actions = accionesLegendarias,
                    reactions = reacciones,
                    image = bestia.image,
                    url = bestia.url
                )
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                null
            }
        }

        suspend fun traducirDescripcionSeleccionada(descripcion: String?): String? {
            return descripcion?.let {
                traductorInglesEspanol
                    ?.translate(it)
                    ?.await() ?: it
            }
        }
    }
}