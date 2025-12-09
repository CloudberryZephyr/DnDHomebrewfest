package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import kotlin.random.Random

enum class StatMethod{
    STANDARD,
    ROLLFOURANDDROP,
    POINTBUY
}

enum class Step{
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX
}

@Composable
fun CharacterCreationScreen(hbfVM : HBFViewModel, modifier : Modifier = Modifier) {
    val hbfUIState by hbfVM.uiState.collectAsState()
    val dndViewModel : DnDViewModel = viewModel()
    val step : Step = Step.ONE

    // TODO: Step 1: Class
    if(step == Step.ONE){
        Column(modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(dndViewModel.classObjects.toString())
        }
    }

    // Step 2: Stats
    // TODO: Implement choosing between stat generation methods
    val statMethod = StatMethod.ROLLFOURANDDROP

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
        val stats : MutableList<Int> = mutableListOf()
        for(i in 0..5){
            val statRNs : MutableList<Int> = mutableListOf()
            var lowest  = 6
            for(j in 0..3) {
                val thisRn : Int = Random.nextInt(1, 7)
                statRNs.add(thisRn)
                lowest = if(thisRn < lowest) thisRn else lowest
            }
            statRNs.remove(lowest)
            var sum : Int = 0
            for(j in statRNs){
                sum += j
            }
            stats.add(sum)
        }
    } else {

    }

    // TODO: Step 3: Race

    // TODO: Step 4: Background

    // TODO: Step 5: Equipment

    // TODO: Step 6: Misc.
}