package com.example.dndhomebrewfest.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndhomebrewfest.apis.DnDAPI
import com.example.dndhomebrewfest.viewmodels.Option.OptionString
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = OptionSerializer::class)
sealed interface Option {
    @Serializable(with = OptionStringSerializer::class)
    data class OptionString(val option : String) : Option

    @Serializable
    data class OptionObject(
        val option_type : String,
        val name : String? = null,
        val dc : DC? = null,
        val damage : List<Damage>? = null,
        val count : Int? = null,
        val item : ObjectReference? = null,
        val of : ObjectReference? = null,
        val items : List<Option>? = null,
        val choice : Choice? = null,
        val string : String? = null,
        val desc : String? = null,
        val alignments : List<ObjectReference>? = null,
        val ability_score: ObjectReference? = null,
        val action_name : String? = null,
        val minimum_score: Int? = null,
        val type : String? = null,
        val damage_type : ObjectReference? = null,
        val damage_dice : String? = null,
        val notes: String? = null,
        val bonus : Int? = null,
        val prerequisites : List<ProficiencyType>? = null
    ) : Option
}

@Serializable
data class ProficiencyType(
    val type : String,
    val proficiency: ObjectReference
)

// Custom serializer specifically for the OptionString case
object OptionStringSerializer : KSerializer<OptionString> {
    // Describes what the output looks like (a primitive string)
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Option.OptionString", PrimitiveKind.STRING)

    // How to write an OptionString object back to JSON
    override fun serialize(encoder: Encoder, value: OptionString) {
        encoder.encodeString(value.option)
    }

    // How to read a JSON string and create an OptionString object
    override fun deserialize(decoder: Decoder): OptionString {
        // We expect a primitive string from the JSON, and we use it to build our object
        return OptionString(decoder.decodeString())
    }
}

object OptionSerializer : JsonContentPolymorphicSerializer<Option>(Option::class) {
    override fun selectDeserializer(element: JsonElement) = when (element) {
        is JsonPrimitive -> OptionString.serializer()
        is JsonObject -> Option.OptionObject.serializer()
        else -> throw SerializationException("Cannot determine the type to deserialize: $element")
    }
}

@Serializable
data class OptionsSet (
    val option_set_type : String,
    val options : List<Option>? = null,
    val equipment_category : ObjectReference? = null,
    val resource_list_url : String? = null
)

@Serializable
data class Choice(
    val desc : String? = null,
    val choose : Int,
    val type : String,
    val from : OptionsSet
)

@Serializable
data class Equipments(
    val equipment : ObjectReference,
    val quantity : Int
)

@Serializable
data class Prerequisite(
    val type : String? = null,
    val spell : String? = null,
    val level : Int? = null,
    val feature : String? = null,
    val ability_score : ObjectReference? = null,
    val minimum_score : Int? = null
)

@Serializable
data class MultiClassing(
    val prerequisites : List<Prerequisite>? = null,
    val prerequisite_options : Choice? = null,
    val proficiencies: List<ObjectReference>,
    val proficiency_choices : List<Choice>? = null
)

@Serializable
data class Cost(
    val quantity: Int,
    val unit : String
)

@Serializable
data class ObjectReference(
    val index : String,
    val name : String,
    val url : String,
    val level : Int? = null,
    val type : String? = null
)

@Serializable
data class AbilityScore(
    val index: String,
    val name: String,
    val full_name: String,
    val desc: List<String>,
    val skills: List<ObjectReference>,
    val url: String,
    val updated_at: String
)

@Serializable
data class Alignment(
    val index: String,
    val name: String,
    val abbreviation: String,
    val desc: String,
    val url: String,
    val updated_at: String
)

@Serializable
data class BackgroundFeature(
    val name : String,
    val desc : List<String>
)

@Serializable
data class Background(
    val index: String,
    val name: String,
    val starting_proficiencies: List<ObjectReference>,
    val language_options: Choice,
    val starting_equipment: List<Equipments>,
    val starting_equipment_options: List<Choice>,
    val feature : BackgroundFeature,
    val personality_traits: Choice,
    val ideals: Choice,
    val bonds : Choice,
    val flaws : Choice,
    val url : String,
    val updated_at: String
)

@Serializable
data class SpellInfo(
    val name : String,
    val desc : List<String>
)

@Serializable
data class ClassSpellcasting(
    val level :  Int,
    val spellcasting_ability : ObjectReference,
    val info : List<SpellInfo>
)

@Serializable
data class Class(
    val index: String,
    val name : String,
    val hit_die : Int,
    val proficiency_choices: List<Choice>,
    val proficiencies: List<ObjectReference>,
    val saving_throws: List<ObjectReference>,
    val starting_equipment: List<Equipments>,
    val starting_equipment_options: List<Choice>,
    val class_levels: String,
    val multi_classing: MultiClassing,
    val spells : String? = null,
    val spellcasting: ClassSpellcasting? = null,
    val subclasses: List<ObjectReference>,
    val url: String,
    val updated_at: String
)

@Serializable
data class Condition(
    val index: String,
    val name: String,
    val desc: List<String>,
    val url: String,
    val updated_at: String
)

@Serializable
data class DamageType(
    val index: String,
    val name: String,
    val desc: List<String>,
    val url: String,
    val updated_at: String
)

@Serializable
data class ArmorClass(
    val base : Int,
    val dex_bonus : Boolean,
    val max_bonus : Int? = null
)

@Serializable
data class Range(
    val normal : Int,
    val long : Int? = null
)

@Serializable
data class Content(
    val item : ObjectReference,
    val quantity: Int
)

@Serializable
data class Equipment(
    val special : List<String>,
    val index: String,
    val name: String,
    val equipment_category: ObjectReference,
    val tool_category: String? = null,
    val armor_category: String? = null,
    val armor_class: ArmorClass? = null,
    val str_minimum : Int? = null,
    val stealth_disadvantage : Boolean? = null,
    val gear_category : ObjectReference? = null,
    val vehicle_category : String? = null,
    val weapon_category : String? = null,
    val weapon_range : String? = null,
    val category_range : String? = null,
    val cost : Cost,
    val quantity: Int? = null,
    val speed: Speed? = null,
    val capacity : String? = null,
    val damage : Damage? = null,
    val range : Range? = null,
    val throw_range : Range? = null,
    val weight : Double? = null,
    val desc: List<String>,
    val image: String? = null,
    val url: String,
    val updated_at: String,
    val contents : List<Content>,
    val properties: List<ObjectReference>,
    val two_handed_damage : Damage? = null
)

@Serializable
data class EquipmentCategory(
    val index: String,
    val name: String,
    val equipment: List<ObjectReference>,
    val url : String,
    val updated_at: String
)

@Serializable
data class Feat(
    val index: String,
    val name: String,
    val prerequisites: List<Prerequisite>,
    val desc: List<String>,
    val url: String,
    val updated_at: String
)

@Serializable
data class FeatureStuff(
    val subfeature_options : Choice? = null,
    val expertise_options : Choice? = null,
    val enemy_type_options : Choice? = null,
    val terrain_type_options : Choice? = null,
    val invocations : List<ObjectReference>
)

@Serializable
data class Feature(
    val index: String,
    @SerialName("class") val req_class : ObjectReference,
    val subclass : ObjectReference? = null,
    val name: String,
    val level : Int,
    val prerequisites: List<Prerequisite>,
    val desc: List<String>,
    val parent : ObjectReference? = null,
    val feature_specific : FeatureStuff? = null,
    val reference : String? = null,
    val url: String,
    val updated_at: String
)

@Serializable
data class Language(
    val index: String,
    val name: String,
    val type : String,
    val typical_speakers: List<String>,
    val script: String? = null,
    val desc : String? = null,
    val url: String,
    val updated_at: String
)

@Serializable
data class Rarity(
    val name : String
)

@Serializable
data class MagicItem(
    val index : String,
    val name: String,
    val equipment_category : ObjectReference,
    val rarity : Rarity,
    val variants : List<ObjectReference>,
    val variant: Boolean,
    val desc: List<String>,
    val image: String? = null,
    val url: String,
    val updated_at: String
)

@Serializable
data class MagicSchool(
    val index: String,
    val name: String,
    val desc: String,
    val url: String,
    val updated_at: String
)

@Serializable
data class RuleSection(
    val index: String,
    val name: String,
    val desc: String,
    val url: String,
    val updated_at: String
)


@Serializable
data class MonsterAC(
    val type : String,
    val value : Int,
    val desc : String? = null,
    val armor : List<ObjectReference>? = null,
    val condition : ObjectReference? = null,
    val spell : ObjectReference? = null
)

@Serializable
data class Speed(
    val walk : String? = null,
    val swim : String? = null,
    val fly : String? = null,
    val burrow : String? = null,
    val climb : String? = null,
    val hover : Boolean? = null,
    val quantity: Double? = null,
    val unit: String? = null
)

@Serializable
data class ProficiencyReference(
    val value : Int,
    val proficiency : ObjectReference
)

@Serializable
data class Usage(
    val type: String,
    val rest_types: List<String>? = null,
    val times : Int? = null,
    val dice : String? = null,
    val min_value : Int? = null
)

@Serializable
data class MonsterSpell(
    val name: String,
    val level : Int,
    val url: String,
    val usage : Usage? = null,
    val notes : String? = null
)

@Serializable(with = DamageSerializer::class)
sealed interface Damage {
    @Serializable
    data class DamageObject(
        val damage_type: ObjectReference? = null,
        val damage_dice: String? = null,
        val damage_at_character_level: Map<String, String>? = null,
        val dc: DC? = null,
        val damage_at_slot_level: Map<String, String>? = null

    ) : Damage

    @Serializable
    data class DamageChoice(
        val desc : String? = null,
        val choose : Int,
        val type : String,
        val from : OptionsSet
    ) : Damage
}

object DamageSerializer : JsonContentPolymorphicSerializer<Damage>(Damage::class) {
    override fun selectDeserializer(element: JsonElement) = when (element) {
        is JsonObject -> if (element.contains("choose")) Damage.DamageChoice.serializer() else Damage.DamageObject.serializer()
        else -> throw SerializationException("Cannot determine the type to deserialize: $element")
    }
}

@Serializable
data class Spellcasting(
    val level: Int? = null,
    val ability : ObjectReference,
    val dc : Int? = null,
    val modifier : Int? = null,
    val components_required : List<String>,
    val school : String? = null,
    val slots : Map<String, Int>? = null,
    val spells : List<MonsterSpell>,
    val damage : List<Damage>? = null
)

@Serializable
data class Ability(
    val name : String,
    val desc : String,
    val damage : List<Damage>? = null,
    val area_of_effect: AOE? = null,
    val spellcasting : Spellcasting? = null,
    val dc : DC? = null,
    val usage : Usage? = null,
    val actions : ObjectReference? = null
)

@Serializable
data class Attack(
    val name : String,
    val dc : DC,
    val damage: List<Damage>? = null,
)

@Serializable
data class Action(
    val name: String? = null,
    val action_name : String? = null,
    val count : String? = null,
    val type : String? = null,
    val desc: String? = null,
    val attacks : List<Attack>? = null,
    val attack_bonus : Int? = null,
    val multiattack_type : String? = null,
    val damage : List<Damage>? = null,
    val actions: List<Action>? = null,
    val dc: DC? = null,
    val usage: Usage? = null,
    val options : Choice? = null,
    val action_options : Choice? = null
)

@Serializable
data class Monster(
    val index: String,
    val name: String,
    val desc: String? = null,
    val size : String,
    val type : String,
    val subtype : String? = null,
    val alignment : String,
    val armor_class : List<MonsterAC>,
    val hit_points : Int,
    val hit_dice : String,
    val hit_points_roll : String,
    val speed : Speed,
    val strength : Int,
    val dexterity : Int,
    val constitution : Int,
    val intelligence : Int,
    val wisdom : Int,
    val charisma : Int,
    val proficiencies: List<ProficiencyReference>,
    val damage_vulnerabilities : List<String>,
    val damage_resistances : List<String>,
    val damage_immunities : List<String>,
    val condition_immunities : List<ObjectReference>,
    val languages : String,
    val senses : Map<String, JsonPrimitive>,
    val challenge_rating : Double,
    val proficiency_bonus : Int,
    val xp : Int,
    val special_abilities : List<Ability>,
    val actions : List<Action>,
    val image: String,
    val url: String,
    val updated_at: String,
    val forms : List<ObjectReference>,
    val legendary_actions : List<Action>,
    val reactions : List< Action>
)

@Serializable
data class Proficiency(
    val index: String,
    val type : String,
    val name: String,
    val classes : List<ObjectReference>,
    val races : List<ObjectReference>,
    val url: String,
    val reference: ObjectReference,
    val updated_at: String
)

@Serializable
data class AbilityBonus(
    val ability_score: ObjectReference,
    val bonus : Int
)


@Serializable
data class Race(
    val index: String,
    val name: String,
    val speed : Int,
    val ability_bonuses : List<AbilityBonus>,
    val ability_bonus_options : Choice? = null,
    val alignment : String,
    val age : String,
    val size : String,
    val size_description : String,
    val languages : List<ObjectReference>,
    val language_options: Choice? = null,
    val language_desc : String,
    val traits : List<ObjectReference>,
    val subraces : List<ObjectReference>,
    val url : String,
    val updated_at: String
)

@Serializable
data class Rule(
    val index: String,
    val name: String,
    val desc: String,
    val subsections : List<ObjectReference>,
    val url: String,
    val updated_at: String
)

@Serializable
data class Skill(
    val index: String,
    val name: String,
    val desc: List<String>,
    val ability_score: ObjectReference,
    val url: String,
    val updated_at: String
)

@Serializable
data class DC(
    val dc_type : ObjectReference,
    val desc : String? = null,
    val dc_value : Int? = null,
    val success_type : String? = null,
    val dc_success : String? = null
)

@Serializable
data class AOE(
    val type : String,
    val size : Int
)

@Serializable
data class Spell(
    val index: String,
    val name: String,
    val desc: List<String>,
    val higher_level : List<String>,
    val range : String,
    val components: List<String>,
    val material : String? = null,
    val ritual : Boolean,
    val duration : String,
    val concentration : Boolean,
    val casting_time : String,
    val level : Int,
    val dc : DC? = null,
    val attack_type : String? = null,
    val damage : Damage? = null,
    val area_of_effect : AOE? = null,
    val heal_at_slot_level : Map<String, String>? = null,
    val school : ObjectReference,
    val classes : List<ObjectReference>,
    val subclasses: List<ObjectReference>,
    val url: String,
    val updated_at: String
)

@Serializable
data class SubclassLevel(
    val level : Int,
    val features : List<ObjectReference>,
    @SerialName("class") val req_class : ObjectReference,
    val subclass : ObjectReference,
    val url: String,
    val index: String,
    val updated_at: String
)

@Serializable
data class SubclassSpell(
    val prerequisites: List<ObjectReference>,
    val spell : ObjectReference
)

@Serializable
data class Subclass(
    val index: String,
    @SerialName("class") val req_class : ObjectReference,
    val name: String,
    val subclass_flavor : String,
    val desc : List<String>,
    val subclass_levels : String,
    val url : String,
    val updated_at: String,
    val spells : List<SubclassSpell>
)

@Serializable
data class Subrace(
    val index: String,
    val name: String,
    val race : ObjectReference,
    val desc : String,
    val ability_bonuses: List<AbilityBonus>,
    val racial_traits : List<ObjectReference>,
    val url: String,
    val updated_at: String
)

@Serializable
data class TraitOption(
    val subtrait_options : Choice? = null,
    val damage_type : ObjectReference? = null,
    val breath_weapon : Ability? = null,
    val spell_options : Choice? = null
)

@Serializable
data class Trait(
    val index : String,
    val races : List<ObjectReference>,
    val subraces : List<ObjectReference>,
    val name : String,
    val desc : List<String>,
    val proficiencies: List<ObjectReference>,
    val proficiency_choices : Choice? = null,
    val trait_specific : TraitOption? = null,
    val language_options: Choice? = null,
    val parent: ObjectReference? = null,
    val url: String,
    val updated_at: String
)

@Serializable
data class WeaponProperty(
    val index: String,
    val name: String,
    val desc: List<String>,
    val url: String,
    val updated_at: String
)

@Serializable
data class Category(
    val count: Int,
    val results : List<ObjectReference>
)

class DnDViewModel : ViewModel() {

    var abilityScoreObjects by mutableStateOf<List<AbilityScore>>(emptyList())
    var alignmentObjects by mutableStateOf<List<Alignment>>(emptyList())
    var backgroundObjects by mutableStateOf<List<Background>>(emptyList())
    var classObjects by mutableStateOf<List<Class>>(emptyList())
    var conditionObjects by mutableStateOf<List<Condition>>(emptyList())
    var damageTypeObjects by mutableStateOf<List<DamageType>>(emptyList())
    var equipmentObjects by mutableStateOf<List<Equipment>>(emptyList())
    var equipmentCategoryObjects by mutableStateOf<List<EquipmentCategory>>(emptyList())
    var featObjects by mutableStateOf<List<Feat>>(emptyList())
    var featureObjects by mutableStateOf<List<Feature>>(emptyList())
    var languageObjects by mutableStateOf<List<Language>>(emptyList())
    var magicItemObjects by mutableStateOf<List<MagicItem>>(emptyList())
    var magicSchoolObjects by mutableStateOf<List<MagicSchool>>(emptyList())
    var monsterObjects by mutableStateOf<List<Monster>>(emptyList())
    var proficiencyObjects by mutableStateOf<List<Proficiency>>(emptyList())
    var raceObjects by mutableStateOf<List<Race>>(emptyList())
    var ruleSectionObjects by mutableStateOf<List<RuleSection>>(emptyList())
    var ruleObjects by mutableStateOf<List<Rule>>(emptyList())
    var skillObjects by mutableStateOf<List<Skill>>(emptyList())
    var spellObjects by mutableStateOf<List<Spell>>(emptyList())
    var subclassObjects by mutableStateOf<List<Subclass>>(emptyList())
    var subraceObjects by mutableStateOf<List<Subrace>>(emptyList())
    var traitObjects by mutableStateOf<List<Trait>>(emptyList())
    var weaponPropertyObjects by mutableStateOf<List<WeaponProperty>>(emptyList())

    fun getAbilityScores() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("ability-scores")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getAbilityScore(result.index)
                        val temp = abilityScoreObjects.toMutableList()
                        temp.add(info)
                        abilityScoreObjects = temp.toList()
                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with Ability Score ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with Ability Score: ${e.message}")
            }
        }
    }

    fun getAlignments() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("alignments")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getAlignment(result.index)
                        val temp = alignmentObjects.toMutableList()
                        temp.add(info)
                        alignmentObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with Alignment ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with Alignment: ${e.message}")
            }
        }
    }

    fun getBackgrounds() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("backgrounds")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getBackground(result.index)
                        val temp = backgroundObjects.toMutableList()
                        temp.add(info)
                        backgroundObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with Background ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with Background: ${e.message}")
            }
        }
    }

    fun getClasses() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("classes")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getClass(result.index)
                        val temp = classObjects.toMutableList()
                        temp.add(info)
                        classObjects = temp.toList()
                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with Class ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with Class: ${e.message}")
            }
        }


    }

    fun getConditions() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("conditions")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getCondition(result.index)
                        val temp = conditionObjects.toMutableList()
                        temp.add(info)
                        conditionObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with Condition ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with Condition: ${e.message}")
            }
        }
    }

    fun getDamageTypes() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("damage-types")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getDamageType(result.index)
                        val temp = damageTypeObjects.toMutableList()
                        temp.add(info)
                        damageTypeObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with damage-types ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with damage-types: ${e.message}")
            }
        }
    }

    fun getEquipment() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("equipment")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getEquipment(result.index)
                        val temp = equipmentObjects.toMutableList()
                        temp.add(info)
                        equipmentObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with equipment ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with equipment: ${e.message}")
            }
        }
    }

    fun getEquipmentCategories() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("equipment-categories")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getEquipmentCategory(result.index)
                        val temp = equipmentCategoryObjects.toMutableList()
                        temp.add(info)
                        equipmentCategoryObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with equipment-categories ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with equipment-categories: ${e.message}")
            }
        }
    }

    fun getFeats() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("feats")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getFeat(result.index)
                        val temp = featObjects.toMutableList()
                        temp.add(info)
                        featObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with feats ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with feats: ${e.message}")
            }
        }
    }

    fun getFeatures() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("features")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getFeature(result.index)
                        val temp = featureObjects.toMutableList()
                        temp.add(info)
                        featureObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with features ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with features: ${e.message}")
            }
        }
    }

    fun getLanguages() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("languages")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getLanguage(result.index)
                        val temp = languageObjects.toMutableList()
                        temp.add(info)
                        languageObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with languages ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with languages: ${e.message}")
            }
        }
    }

    fun getMagicItems() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("magic-items")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getMagicItem(result.index)
                        val temp = magicItemObjects.toMutableList()
                        temp.add(info)
                        magicItemObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with magic-items ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with magic-items: ${e.message}")
            }
        }
    }

    fun getMagicSchools() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("magic-schools")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getMagicSchool(result.index)
                        val temp = magicSchoolObjects.toMutableList()
                        temp.add(info)
                        magicSchoolObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with magic-schools ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with magic-schools: ${e.message}")
            }
        }
    }

    fun getMonsters() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("monsters")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getMonster(result.index)
                        val temp = monsterObjects.toMutableList()
                        temp.add(info)
                        monsterObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with monsters ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with monsters: ${e.message}")
            }
        }
    }

    fun getProficiencies() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("proficiencies")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getProficiency(result.index)
                        val temp = proficiencyObjects.toMutableList()
                        temp.add(info)
                        proficiencyObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with proficiencies ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with proficiencies: ${e.message}")
            }
        }
    }

    fun getRaces() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("races")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getRace(result.index)
                        val temp = raceObjects.toMutableList()
                        temp.add(info)
                        raceObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with races ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with races: ${e.message}")
            }
        }
    }

    fun getRuleSections() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("rule-sections")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getRuleSection(result.index)
                        val temp = ruleSectionObjects.toMutableList()
                        temp.add(info)
                        ruleSectionObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with rule-sections ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with rule-sections: ${e.message}")
            }
        }
    }

    fun getRules() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("rules")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getRule(result.index)
                        val temp = ruleObjects.toMutableList()
                        temp.add(info)
                        ruleObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with rules ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with rules: ${e.message}")
            }
        }
    }

    fun getSkills() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("skills")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getSkill(result.index)
                        val temp = skillObjects.toMutableList()
                        temp.add(info)
                        skillObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with skills ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with skills: ${e.message}")
            }
        }
    }

    fun getSpells() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("spells")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getSpell(result.index)
                        val temp = spellObjects.toMutableList()
                        temp.add(info)
                        spellObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with spells ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with spells: ${e.message}")
            }
        }
    }

    fun getSubclasses() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("subclasses")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getSubclass(result.index)
                        val temp = subclassObjects.toMutableList()
                        temp.add(info)
                        subclassObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with subclasses ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with subclasses: ${e.message}")
            }
        }
    }

    fun getSubraces() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("subraces")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getSubrace(result.index)
                        val temp = subraceObjects.toMutableList()
                        temp.add(info)
                        subraceObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with subraces ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with subraces: ${e.message}")
            }
        }
    }

    fun getTraits() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("traits")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getTrait(result.index)
                        val temp = traitObjects.toMutableList()
                        temp.add(info)
                        traitObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with traits ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with traits: ${e.message}")
            }
        }
    }

    fun getWeaponProperties() {
        viewModelScope.launch {
            try {
                val catInfo = DnDAPI.retrofitService.getCategory("weapon-properties")
                for (result in catInfo.results) {
                    try {
                        val info = DnDAPI.retrofitService.getWeaponProperty(result.index)
                        val temp = weaponPropertyObjects.toMutableList()
                        temp.add(info)
                        weaponPropertyObjects = temp.toList()
//                        Log.i("MyTAG", "Added ${result.url}")
                    } catch (e: Throwable) {
                        Log.e("MyTAG", "error with weapon-properties ${result.name}: ${e.message}")
                    }
                }

            } catch (e: Throwable) {
                Log.e("MyTAG", "error with weapon-properties: ${e.message}")
            }
        }
    }
}