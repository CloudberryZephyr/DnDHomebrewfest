package com.example.dndhomebrewfest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndhomebrewfest.HomebreweryApp
import com.example.dndhomebrewfest.data.CharacterDao
import com.example.dndhomebrewfest.data.DataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
            }
        }
    }

    val characters = characterDao.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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