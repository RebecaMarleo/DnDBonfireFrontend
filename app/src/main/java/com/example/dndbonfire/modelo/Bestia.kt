package com.example.dndbonfire.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bestia(
    val index: String,
    val name: String,
    val size: String,
    val type: String,
    val alignment: String,
    val armor_class: List<ClaseArmadura>,
    val hit_points: Int,
    val hit_dice: String,
    val forms: List<Forma>? = null,
    val hit_points_roll: String,
    val speed: Velocidades,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val proficiencies: List<Competencia>,
    val damage_vulnerabilities: List<String>,
    val damage_resistances: List<String>,
    val damage_immunities: List<String>,
    val condition_immunities: List<Condicion>,
    val senses: Sentidos,
    val languages: String,
    val challenge_rating: Double,
    val proficiency_bonus: Int,
    val xp: Int,
    val special_abilities: List<HabilidadEspecial>,
    val actions: List<Accion>,
    val legendary_actions: List<AccionLegendaria>,
    val reactions: List<Reaccion>,
    val image: String,
    val url: String
)

@Serializable
data class ClaseArmadura(
    val type: String,
    val value: Int
)

@Serializable
data class Forma(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class Velocidades(
    val walk: String? = null,
    val fly: String? = null,
    val swim: String? = null,
    val burrow: String? = null,
    val climb: String? = null
)

@Serializable
data class Competencia(
    val value: Int,
    val proficiency: ResumenCompetencia
)

@Serializable
data class ResumenCompetencia(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class Condicion(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class Sentidos(
    val blindsight: String? = null,
    val darkvision: String? = null,
    val tremorsense: String? = null,
    val truesight: String? = null,
    val passive_perception: Int
)

@Serializable
data class HabilidadEspecial(
    val name: String,
    val desc: String,
    val spellcasting: LanzamientoDeConjuros? = null,
    val usage: Uso? = null,
    val dc: Dc? = null,
    val damage: List<Dano>? = null
)

@Serializable
data class LanzamientoDeConjuros(
    val level: Int,
    val ability: Habilidad,
    val dc: Int,
    val modifier: Int,
    val components_required: List<String>,
    val school: String,
    val slots: EspaciosDeConjuro,
    val spells: List<ResumenConjuro>
)

@Serializable
data class Habilidad(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class EspaciosDeConjuro(
    @SerialName("1") val nivel1: Int? = null,
    @SerialName("2") val nivel2: Int? = null,
    @SerialName("3") val nivel3: Int? = null,
    @SerialName("4") val nivel4: Int? = null,
    @SerialName("5") val nivel5: Int? = null,
    @SerialName("6") val nivel6: Int? = null
)

@Serializable
data class ResumenConjuro(
    val name: String,
    val level: Int,
    val url: String
)

@Serializable
data class Uso(
    val type: String,
    val times: Int? = null,
    val rest_types: List<String>
)

@Serializable
data class Dano(
    val damage_type: TipoDano? = null,
    val damage_dice: String? = null
)

@Serializable
data class Accion(
    val damage: List<Dano>? = null,
    val name: String,
    val multiattack_type: String? = null,
    val attack_bonus: Int? = null,
    val usage: UsoAccion? = null,
    val dc: Dc? = null,
    val desc: String,
    val actions: List<ResumenAccion>
)

@Serializable
data class TipoDano(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class UsoAccion(
    val type: String,
    val dice: String? = null,
    val min_value: Int? = null,
    val times: Int? = null
)

@Serializable
data class Dc(
    val dc_type: TipoDc,
    val dc_value: Int,
    val success_type: String
)

@Serializable
data class TipoDc(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class ResumenAccion(
    val action_name: String,
    val count: String,
    val type: String
)

@Serializable
data class AccionLegendaria(
    val name: String,
    val desc: String,
    val dc: Dc? = null,
    val damage: List<Dano>
)

@Serializable
data class Reaccion(
    val name: String,
    val desc: String
)