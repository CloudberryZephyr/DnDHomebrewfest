package com.example.dndhomebrewfest.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndhomebrewfest.HomebreweryApp
import com.example.dndhomebrewfest.data.CharacterDao
import com.example.dndhomebrewfest.data.DataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.dndhomebrewfest.data.Character
import com.example.dndhomebrewfest.data.Homebrew
import com.example.dndhomebrewfest.data.HomebrewDao
import kotlinx.coroutines.flow.StateFlow


data class CharacterState(
    var id: String = "",
    var name: String = "",
    var characterClass: String = "",
    var img: String = "",
    var stats: String = "",
    var race: String = "",
    var saves: String = "",
    var skills: String = "",
    var equipment: String = "",
    var features: String = "",
    var spells: String = ""
){
    fun toCharacter() : Character {
        return Character(
            character_id = id.toIntOrNull() ?: 1,
            name = name,
            character_class = characterClass,
            char_img_uri = img,
            character_stats_json = stats,
            character_race = race,
            saving_throws_json = saves,
            skills_json = skills,
            equipment_json = equipment,
            features_json = features,
            spells_json = spells
        )
    }
}

class RoomVM(
    val characterDao: CharacterDao,
    val homebrewDao: HomebrewDao
) : ViewModel() {
    init {
        // For testing only
        importDataToCharacterTable()
    }

    private fun importDataToCharacterTable(){
        viewModelScope.launch {
            DataSource.listOfCharacters.forEach{ character ->
                characterDao.upsertCharacter(character)
                Log.i("MYTAG", "Upserted $character")
            }
        }
    }

    val characters = characterDao.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val homebrews = homebrewDao.getAllHomebrews()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun updateCharacterImage(charID : Int, imageURI : String) {
        viewModelScope.launch {
            characterDao.updateImageUri(charID, imageURI)
        }
    }

    fun getCharacter(charID : Int) : StateFlow<Character>? {
        var character: StateFlow<Character>? = null
        viewModelScope.launch {
            character = characterDao.getCharacter(charID).stateIn(viewModelScope, SharingStarted.WhileSubscribed(),
                Character())
        }
        return character
    }

    fun getHomebrew(brewID : Int) : StateFlow<Homebrew>? {
        var homebrew: StateFlow<Homebrew>? = null
        viewModelScope.launch{
            homebrew = homebrewDao.getHomebrew(brewID).stateIn(viewModelScope, SharingStarted.WhileSubscribed(),
                Homebrew())
        }
        return homebrew
    }

    fun addCharacter(character: Character){
        val id = character.character_id
        if(id != null) {
            viewModelScope.launch {
                characterDao.upsertCharacter(character)
            }
        }
    }

    fun addHomebrew(homebrew: Homebrew){
        val id = homebrew.homebrewId
        if(id != null) {
            viewModelScope.launch{
                homebrewDao.upsertHomebrew(homebrew)
            }
        }
    }

    companion object{
        private var INSTANCE : RoomVM ? = null

        fun getInstance() : RoomVM {
            val vm = INSTANCE ?: synchronized(this){
                RoomVM(HomebreweryApp.Companion.getApp().container.characterDao,
                    HomebreweryApp.Companion.getApp().container.homebrewDao).also {
                    INSTANCE = it
                }
            }
            return vm
        }
    }
}