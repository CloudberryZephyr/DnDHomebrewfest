package com.example.dndhomebrewfest.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndhomebrewfest.apis.DnDAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Object(
    val index : String,
    val name : String,
    val url : String
)

@Serializable
data class DnDCategory (
    val count : Int,
    val results : List<Object>
)

sealed interface DnDUIState {
    object Loading : DnDUIState
    object Error : DnDUIState
    class Success(val dndCategory : DnDCategory) : DnDUIState
}

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
    var dndUIState : DnDUIState by mutableStateOf(DnDUIState.Loading)
    var dndUIStates : MutableMap<String, DnDUIState> = mutableMapOf()

    init {
        getCategories()
    }

    fun getCategories() {
        for (category : String in categoryList) {
            viewModelScope.launch {
                try {
                    val catInfo = DnDAPI.retrofitService.getCategory(category)
                    dndUIState = DnDUIState.Success(catInfo)
                    dndUIStates[category] = dndUIState
                } catch (e: Throwable) {
                    Log.e("MyTag", "error is ${e.message}")
                    dndUIState = DnDUIState.Error
                }
            }
        }
    }
}