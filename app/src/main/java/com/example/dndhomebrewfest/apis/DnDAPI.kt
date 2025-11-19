package com.example.dndhomebrewfest.apis

import com.example.dndhomebrewfest.viewmodels.AbilityScore
import com.example.dndhomebrewfest.viewmodels.Alignment
import com.example.dndhomebrewfest.viewmodels.Background
import com.example.dndhomebrewfest.viewmodels.Category
import com.example.dndhomebrewfest.viewmodels.Class
import com.example.dndhomebrewfest.viewmodels.Condition
import com.example.dndhomebrewfest.viewmodels.DamageType
import com.example.dndhomebrewfest.viewmodels.Equipment
import com.example.dndhomebrewfest.viewmodels.EquipmentCategory
import com.example.dndhomebrewfest.viewmodels.Feat
import com.example.dndhomebrewfest.viewmodels.Feature
import com.example.dndhomebrewfest.viewmodels.Language
import com.example.dndhomebrewfest.viewmodels.MagicItem
import com.example.dndhomebrewfest.viewmodels.MagicSchool
import com.example.dndhomebrewfest.viewmodels.Monster
import com.example.dndhomebrewfest.viewmodels.Proficiency
import com.example.dndhomebrewfest.viewmodels.Race
import com.example.dndhomebrewfest.viewmodels.Rule
import com.example.dndhomebrewfest.viewmodels.RuleSection
import com.example.dndhomebrewfest.viewmodels.Skill
import com.example.dndhomebrewfest.viewmodels.Spell
import com.example.dndhomebrewfest.viewmodels.Subclass
import com.example.dndhomebrewfest.viewmodels.Subrace
import com.example.dndhomebrewfest.viewmodels.Trait
import com.example.dndhomebrewfest.viewmodels.WeaponProperty
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface DndAPIService {

    @GET("/api/2014/{category}")
    suspend fun getCategory(@Path("category") category : String) : Category

    @GET("/api/2014/ability-scores/{score}")
    suspend fun getAbilityScore(@Path("score") score : String) : AbilityScore

    @GET("/api/2014/alignments/{alignment}")
    suspend fun getAlignment(@Path("alignment") alignment : String) : Alignment

    @GET("/api/2014/backgrounds/{background}")
    suspend fun getBackground(@Path("background") background : String) : Background

    @GET("/api/2014/classes/{class}")
    suspend fun getClass(@Path("class") classUrl : String) : Class

    @GET("/api/2014/conditions/{condition}")
    suspend fun getCondition(@Path("condition") condition : String) : Condition

    @GET("/api/2014/damage-types/{dmg}")
    suspend fun getDamageType(@Path("dmg") dmg : String) : DamageType

    @GET("/api/2014/equipment/{equipment}")
    suspend fun getEquipment(@Path("equipment") equipment : String) : Equipment

    @GET("/api/2014/equipment-categories/{equipCat}")
    suspend fun getEquipmentCategory(@Path("equipCat") equipCat : String) : EquipmentCategory

    @GET("/api/2014/feats/{feat}")
    suspend fun getFeat(@Path("feat") feat : String) : Feat

    @GET("/api/2014/features/{feature}")
    suspend fun getFeature(@Path("feature") feature : String) : Feature

    @GET("/api/2014/languages/{language}")
    suspend fun getLanguage(@Path("language") language : String) : Language

    @GET("/api/2014/magic-items/{magic-item}")
    suspend fun getMagicItem(@Path("magic-item") magicItem : String) : MagicItem

    @GET("/api/2014/magic-schools/{magic-school}")
    suspend fun getMagicSchool(@Path("magic-school") magicSchool : String) : MagicSchool

    @GET("/api/2014/monsters/{monster}")
    suspend fun getMonster(@Path("monster") monster : String) : Monster

    @GET("/api/2014/proficiencies/{proficiency}")
    suspend fun getProficiency(@Path("proficiency") proficiency : String) : Proficiency

    @GET("/api/2014/races/{race}")
    suspend fun getRace(@Path("race") race : String) : Race

    @GET("/api/2014/rule-sections/{rule-section}")
    suspend fun getRuleSection(@Path("rule-section") ruleSection : String) : RuleSection

    @GET("/api/2014/rules/{rule}")
    suspend fun getRule(@Path("rule") rule : String) : Rule

    @GET("/api/2014/skills/{skill}")
    suspend fun getSkill(@Path("skill") skill : String) : Skill

    @GET("/api/2014/spells/{spell}")
    suspend fun getSpell(@Path("spell") spell : String) : Spell

    @GET("/api/2014/subclasses/{subclass}")
    suspend fun getSubclass(@Path("subclass") subclass : String) : Subclass

    @GET("/api/2014/subraces/{subrace}")
    suspend fun getSubrace(@Path("subrace") subrace : String) : Subrace

    @GET("/api/2014/traits/{trait}")
    suspend fun getTrait(@Path("trait") trait : String) : Trait

    @GET("/api/2014/weapon-properties/{weapon-property}")
    suspend fun getWeaponProperty(@Path("weapon-property") weaponProperty : String) : WeaponProperty

}

object DnDAPI {

    private val base_url = "https://www.dnd5eapi.co/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json{ignoreUnknownKeys = false}.asConverterFactory("application/json".toMediaType()))
        .baseUrl(base_url)
        .build()

    val retrofitService : DndAPIService by lazy{
        retrofit.create(DndAPIService::class.java)
    }

}