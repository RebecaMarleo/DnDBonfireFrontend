package com.example.dndbonfire.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Clase(
    val index: String,
    val name: String,
    val hit_die: Int,
    val proficiency_choices: List<Eleccion>,
    val proficiencies: List<ResumenCompetencia>,
    val saving_throws: List<TiradaSalvacion>,
    val starting_equipment: List<EquipamientoInicial>,
    val starting_equipment_options: List<OpcionesEquipamientoInicial>,
    val class_levels: String,
    val multi_classing: Multiclase,
    val subclasses: List<ResumenSubclase>,
    val spellcasting: LanzamientoDeConjurosClase? = null,
    val spells: String? = null,
    val url: String
)

@Serializable
data class Eleccion(
    val desc: String,
    val choose: Int,
    val type: String,
    val from: Opciones,
)

@Serializable
data class Opciones(
    val option_set_type: String,
    val options: List<Opcion>? = null,
    val equipment_category: Item? = null
)

@Serializable
data class Opcion(
    val option_type: String,
    val item: Item? = null,
    val count: Int? = null,
    val of: Item? = null,
    val choice: Eleccion? = null,
    val equipment_category: DatosEquipamientoInicial? = null
)

@Serializable
data class Item(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class TiradaSalvacion(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class EquipamientoInicial(
    val equipment: DatosEquipamientoInicial,
    val quantity: Int
)

@Serializable
data class DatosEquipamientoInicial(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class OpcionesEquipamientoInicial(
    val desc: String,
    val choose: Int,
    val type: String,
    val from: Opciones
)

@Serializable
data class Multiclase(
    val prerequisites: List<Prerrequisito>? = null,
    val proficiencies: List<ResumenCompetencia>
)

@Serializable
data class Prerrequisito(
    val ability_score: PuntajeHabilidad,
    val minimum_score: Int
)

@Serializable
data class ResumenSubclase(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class LanzamientoDeConjurosClase(
    val level: Int,
    val spellcasting_ability: Habilidad? = null,
    val info: List<InformacionConjuros>
)

@Serializable
data class InformacionConjuros(
    val name: String,
    val desc: List<String>
)