package com.example.dndhomebrewfest.viewmodels

import android.media.Image
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndhomebrewfest.apis.DnDAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Option(
    val option_type : String,
    val count : Int?,
    val item : ObjectReference?,
    val of : ObjectReference?,
    val choice : Choice?,
    val string : String?,
    val desc : String?,
    val alignments : List<ObjectReference>?
)

@Serializable
data class OptionSet (
    val option_set_type : String,
    val options : List<Option>?,
    val equipment_category : ObjectReference?,
    val resource_list_url : String?
)

@Serializable
data class Choice(
    val desc : String?,
    val choose : Int,
    val type : String,
    val from : OptionSet
)

@Serializable
data class Equipments(
    val equipment : ObjectReference,
    val quantity : Int
)

@Serializable
data class Prerequisite(
    val ability_score : ObjectReference,
    val minimum_score : Int
)

@Serializable
data class MultiClassing(
    val prerequisites : List<Prerequisite>,
    val proficiencies: List<ObjectReference>
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
    val url : String
)

@Serializable
data class AbilityScore(
    val index: String,
    val name: String,
    val full_name: String,
    val desc: String,
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
data class Equipment(
    val special : List<ObjectReference>,
    val index: String,
    val name: String,
    val equipment_category: ObjectReference,
    val gear_category : ObjectReference,
    val cost : Cost,
    val weight : Int,
    val desc: List<String>,
    val url: String,
    val updated_at: String,
    val contents : List<ObjectReference>,
    val properties: List<ObjectReference>
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
data class Feature(
    val index: String,
    @SerialName("class") val req_class : ObjectReference,
    val name: String,
    val level : Int,
    val prerequisites: List<Prerequisite>,
    val desc: List<String>,
    val url: String,
    val updated_at: String
)

@Serializable
data class Language(
    val index: String,
    val name: String,
    val type : String,
    val typical_speakers: List<String>,
    val script: String,
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
data class ArmorClass(
    val type : String,
    val value : Int,
    val spell : ObjectReference?
)

@Serializable
data class Speed(
    val walk : String
)

@Serializable
data class ProficiencyReference(
    val value : Int,
    val proficiency : ObjectReference
)

@Serializable
data class Senses (
    val passive_perception : Int
)

@Serializable
data class Usage(
    val type: String,
    val rest_types: List<String>
)

@Serializable
data class MonsterSpell(
    val name: String,
    val level : Int,
    val url: String,
    val usage : Usage?,
    val notes : String
)

@Serializable
data class Damage(
    val damage_type : ObjectReference,
    val damage_dice : String?,
    val damage_at_character_level : Map<String,String>?,
    val damage_at_slot_level : Map<String,String>?

)

@Serializable
data class Spellcasting(
    val level: Int,
    val ability : ObjectReference,
    val dc : Int,
    val modifier : Int,
    val components_required : List<String>,
    val slots : Map<String, Int>,
    val spells : List<MonsterSpell>,
    val damage : List<Damage>
)

@Serializable
data class Ability(
    val name : String,
    val desc : String,
    val damage : List<String>?,
    val spellcasting : Spellcasting?
)

@Serializable
data class Action(
    val name: String,
    val desc: String,
    val attack_bonus : Int,
    val damage : List<Damage>,
    val actions: List<Action>
)

@Serializable
data class Monster(
    val index: String,
    val name: String,
    val desc: String,
    val size : String,
    val type : String,
    val subtype : String,
    val alignment : String,
    val armor_class : List<ArmorClass>,
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
    val condition_immunities : List<String>,
    val languages : String,
    val senses : Senses,
    val challenge_rating : Int,
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
    val alignment : String,
    val age : String,
    val size : String,
    val size_description : String,
    val languages : List<ObjectReference>,
    val langauge_desc : String,
    val traits : List<ObjectReference>,
    val subraces : List<ObjectReference>,
    val url : String,
    val updated_at: String
)

@Serializable
data class Rule(
    val index: String,
    val name: String,
    val desc: List<String>,
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
    val dc_success : String
)

@Serializable
data class Spell(
    val index: String,
    val name: String,
    val desc: List<String>,
    val higher_level : List<String>,
    val range : String,
    val components: List<String>,
    val material : String,
    val ritual : Boolean,
    val duration : String,
    val concentration : Boolean,
    val casting_time : String,
    val level : Int,
    val dc : DC?,
    val attack_type : String?,
    val damage : Damage,
    val heal_at_slot_level : Map<String, String>?,
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
data class Subclass(
    val index: String,
    @SerialName("class") val req_class : ObjectReference,
    val name: String,
    val subclass_flavor : String,
    val desc : List<String>,
    val subclass_levels : String,
    val url : String,
    val updated_at: String,
    val spells : List<ObjectReference>
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
data class Trait(
    val index : String,
    val races : List<ObjectReference>,
    val subraces : List<ObjectReference>,
    val name : String,
    val desc : List<String>,
    val proficiencies: List<ObjectReference>,
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
//
//sealed interface ObjectUIState {
//    object Loading : ObjectUIState
//    object Error : ObjectUIState
//    class Success(val objectInfo : Object) : ObjectUIState
//}

class DnDViewModel : ViewModel() {

    val categoryList = listOf(
        "ability-scores",
        "alignments",
        "backgrounds",
        "classes",
        "conditions",
        "damage-types",
        "equipment",
        "equipment-categories",
        "feats",
        "features",
        "languages",
        "magic-items",
        "magic-schools",
        "monsters",
        "proficiencies",
        "races",
        "rule-sections",
        "rules",
        "skills",
        "spells",
        "subclasses",
        "subraces",
        "traits",
        "weapon-properties"
    )

    val abilityScoreObjects : List<AbilityScore> = mutableListOf()
    val alignmentObjects : List<Alignment> = mutableListOf()
    val backgroundObjects : List<Background> = mutableListOf()
    val classObjects : List<Class> = mutableListOf()
    val conditionObjects : List<Condition> = mutableListOf()
    val damageTypeObjects : List<DamageType> = mutableListOf()
    val equipmentObjects : List<Equipment> = mutableListOf()
    val equipmentCategoryObjects : List<EquipmentCategory> = mutableListOf()
    val featObjects : List<Feat> = mutableListOf()
    val featureObjects : List<Feature> = mutableListOf()
    val languageObjects : List<Language> = mutableListOf()
    val magicItemObjects : List<MagicItem> = mutableListOf()
    val magicSchoolObjects : List<MagicSchool> = mutableListOf()
    val monsterObjects : List<Monster> = mutableListOf()
    val proficiencyObjects : List<Proficiency> = mutableListOf()
    val raceObjects : List<Race> = mutableListOf()
    val ruleSectionObejcts : List<RuleSection> = mutableListOf()
    val ruleObjects : List<Rule> = mutableListOf()
    val skillObjects : List<Skill> = mutableListOf()
    val spellObjects : List<Spell> = mutableListOf()
    val subclassObjects : List<Subclass> = mutableListOf()
    val subraceObjects : List<Subrace> = mutableListOf()
    val traitObjects : List<Trait> = mutableListOf()
    val weaponPropertyOpjects : List<WeaponProperty> = mutableListOf()

//    var dndUIState : DnDUIState by mutableStateOf(DnDUIState.Loading)
//    var dndUIStates : MutableMap<String, DnDUIState> = mutableMapOf()
//
//    var objectUIState : ObjectUIState by mutableStateOf(ObjectUIState.Loading)

    init {

    }

//    fun getCategories() {
//        for (category : String in categoryList) {
//            viewModelScope.launch {
//                try {
//                    val catInfo = DnDAPI.retrofitService.getCategory(category)
//                    dndUIState = DnDUIState.Success(catInfo)
//                    dndUIStates[category] = dndUIState
//                } catch (e: Throwable) {
//                    Log.e("MyTag", "error is ${e.message}")
//                    dndUIState = DnDUIState.Error
//                }
//            }
//        }
//    }




}