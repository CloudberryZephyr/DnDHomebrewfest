package com.example.dndhomebrewfest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel

enum class StatMethod{
    STANDARD,
    ROLLFOURANDDROP,
    POINTBUY
}

@Composable
fun CharacterCreationScreen(hbfVM : HBFViewModel, modifier : Modifier = Modifier) {
    val hbfUIState by hbfVM.uiState.collectAsState()
    val dndViewModel : DnDViewModel = viewModel()

    // TODO: Step 1: Class

    // Step 2: Stats
    // TODO: Implement choosing between stat generation methods
    val statMethod = StatMethod.STANDARD

    if(statMethod == StatMethod.STANDARD){
        val stats : MutableList<Int> = mutableListOf(15, 14, 13, 12, 10, 8)
        val statNames : MutableList<String> = mutableListOf(
            "Strength",
            "Dexterity",
            "Constitution",
            "Intelligence",
            "Wisdom",
            "Charisma"
        )
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            
        }
    } else if (statMethod == StatMethod.ROLLFOURANDDROP){

    } else {

    }

    // TODO: Step 3: Race

    // TODO: Step 4: Background

    // TODO: Step 5: Equipment

    // TODO: Step 6: Misc.
}