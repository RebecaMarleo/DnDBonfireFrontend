package com.example.dndbonfire.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nivel(
    val level: Int,
    val ability_score_bonuses: Int,
    val prof_bonus: Int,
    val features: List<Rasgo>,
    val spellcasting: NivelesDeConjuros? = null,
    val class_specific: AtributosEspecificosDeClase,
    val index: String,
    @SerialName("class") val clase: ResumenClase,
    val url: String,
    val updated_at: String
) {
}

@Serializable
data class NivelesDeConjuros(
    val cantrips_known: Int? = null,
    val spells_known: Int? = null, // en clerigos y druidas es nivel + modificador de wisdom
    val spell_slots_level_1: Int? = null,
    val spell_slots_level_2: Int? = null,
    val spell_slots_level_3: Int? = null,
    val spell_slots_level_4: Int? = null,
    val spell_slots_level_5: Int? = null,
    val spell_slots_level_6: Int? = null,
    val spell_slots_level_7: Int? = null,
    val spell_slots_level_8: Int? = null,
    val spell_slots_level_9: Int? = null,
) {}

@Serializable
data class AtributosEspecificosDeClase(
    // barbarian
    val rage_count: Int? = null, // veces que puede usar rage por dia
    val rage_damage_bonus: Int? = null,
    val brutal_critical_dice: Int? = null, // dados de daño extra al hacer critico
    // bardo
    val bardic_inspiration_die: Int? = null,
    val song_of_rest_die: Int? = null,
    val magical_secrets_max_5: Int? = null, // numero de conjuros que no sean de bardo de nivel maximo 5 que puede aprender
    val magical_secrets_max_7: Int? = null,
    val magical_secrets_max_9: Int? = null,
    // clerigo
    val channel_divinity_charges: Int? = null,
    val destroy_undead_cr: Double? = null, // nivel maximo de los monstruos para hacer autokill con canalizar divino (fallar salvacion)
    // druida
    val wild_shape_max_cr: Double? = null,
    val wild_shape_swim: Boolean? = false,
    val wild_shape_fly: Boolean? = false,
    // luchador
    val action_surges: Int? = null, // accion subita
    val indomitable_uses: Int? = null, // permite volver a lanzar una tirada de salvacion si ha fallado
    val extra_attacks: Int? = null,
    // monje
    val martial_arts: ArtesMarciales? = null,
    val ki_points: Int? = null,
    val unarmored_movement: Int? = null,
    // paladin
    val aura_range: Int? = null,
    // explorador
    val favored_enemies: Int? = null, // cantidad de tipos de enemigo que puede trackear
    val favored_terrain: Int? = null, // cantidad de terrenos en los que tiene ventaja al viajar por ellos
    // picaro
    val sneak_attack: AtaqueSorpresa? = null,
    // hechicero
    val sorcery_points: Int? = null,
    val metamagic_known: Int? = null,
    val creating_spell_slots: List<CreacionDeHechizos>? = null,
    // brujo
    val invocations_known: Int? = null,
    val mystic_arcanum_level_6: Int? = null, // numero de hechizos de este nivel que pueden pillar (los brujos capean sus conjuros a lvl 5)
    val mystic_arcanum_level_7: Int? = null,
    val mystic_arcanum_level_8: Int? = null,
    val mystic_arcanum_level_9: Int? = null,
    // mago
    val arcane_recovery_levels: Int? = null, // cantidad de niveles de hechizo que puede recuperar un mago a base de descansos cortos a lo largo de un dia
) {}

@Serializable
data class ArtesMarciales(
    val dice_count: Int,
    val dice_value: Int
) {}

@Serializable
data class AtaqueSorpresa(
    val dice_count: Int,
    val dice_value: Int
) {}

@Serializable
data class CreacionDeHechizos(
    val spell_slot_level: Int,
    val sorcery_point_cost: Int
) {}