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
import kotlinx.coroutines.flow.StateFlow

class RoomVM(
    val characterDao: CharacterDao,
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

    fun addCharacter(character: Character){
        viewModelScope.launch {
            characterDao.upsertCharacter(character)
        }
    }

    companion object{
        private var INSTANCE : RoomVM ? = null

        fun getInstance() : RoomVM {
            val vm = INSTANCE ?: synchronized(this){
                RoomVM(HomebreweryApp.Companion.getApp().container.characterDao).also {
                    INSTANCE = it
                }
            }
            return vm
        }
    }
}