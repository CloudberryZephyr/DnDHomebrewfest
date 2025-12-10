package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import kotlin.random.Random

enum class StatMethod{
    STANDARD,
    ROLLFOURANDDROP,
    NEITHER
}

enum class Step{
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreationScreen(hbfVM : HBFViewModel, modifier : Modifier = Modifier) {
    val hbfUIState by hbfVM.uiState.collectAsState()
    val dndViewModel : DnDViewModel = viewModel()
    var step : Step by remember{ mutableStateOf<Step>(Step.ONE)}

    var finalClass by remember { mutableStateOf<String>("")}
    val finalStats by remember { mutableStateOf<MutableMap<String, Int>>(mutableMapOf())}

    // Step 1: Class
    val classes : MutableList<String> = mutableListOf()
    val abilityScores : MutableList<String> = mutableListOf()
    val races : MutableList<String> = mutableListOf()

    LaunchedEffect(Unit) {
        if(dndViewModel.classObjects.isEmpty()) {
            dndViewModel.getClasses()
        }
        if(dndViewModel.abilityScoreObjects.isEmpty()) {
            dndViewModel.getAbilityScores()
        }
        if(dndViewModel.raceObjects.isEmpty()) {
            dndViewModel.getRaces()
        }
    }
    for(c in dndViewModel.classObjects){
        classes.add(c.name)
    }
    for(s in dndViewModel.abilityScoreObjects){
        abilityScores.add(s.name)
    }
    for(r in dndViewModel.raceObjects){
        races.add(r.name)
    }

    if(step == Step.ONE){
        Column(
            modifier = modifier.fillMaxSize().alpha(if (step == Step.ONE) 1f else 0.5f)
                .pointerInput(step == Step.ONE) {
                    if (step != Step.ONE) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent()
                            }
                        }
                    }
                }
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select Your Class!")

            var selectedIndex by remember { mutableStateOf<Int?>(null) }

            Button(
                onClick = {
                    selectedIndex?.let { finalClass = classes[it] }
                    step = Step.TWO
                    Log.d("MYTAG", "button clicked")
                },
                enabled = finalClass != ""
            ){
                Text("Next!")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize()
            ) {
                items(classes.size) { index ->
                    val charClass = classes[index]
                    val isSelected = selectedIndex == index

                    Card(
                        onClick = {
                            selectedIndex = if (selectedIndex == index) null else index
                            Log.i("MYTAG", "selected $charClass")
                            finalClass = charClass
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = charClass)
                        }
                    }
                }
            }
        }
    }

    // Step 2: Stats
    var statMethod : StatMethod by remember {mutableStateOf(StatMethod.NEITHER)}

    if(step == Step.TWO) {
        val stats : MutableList<Int> by remember{mutableStateOf<MutableList<Int>>(mutableListOf())}

        if(statMethod == StatMethod.NEITHER) {
            Column(
                modifier = modifier.fillMaxSize()
                    .alpha(if (statMethod == StatMethod.NEITHER) 1f else 0.5f)
                    .pointerInput(statMethod == StatMethod.NEITHER) {
                        if (statMethod != StatMethod.NEITHER) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Select the method you wish to use to generate stats. Standard Array gives you" +
                            "a fixed list of stats, while rolling randomly generates stat values."
                )
                Button(
                    onClick = { statMethod = StatMethod.STANDARD }
                ) {
                    Text("Standard Array")
                }
                Button(
                    onClick = { statMethod = StatMethod.ROLLFOURANDDROP }
                ) {
                    Text("Roll for Stats")
                }
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize().alpha(if (step == Step.TWO) 1f else 0.5f)
                    .pointerInput(step == Step.TWO) {
                        if (step != Step.TWO) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                    }
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LaunchedEffect(Unit) {
                    if (statMethod == StatMethod.STANDARD) {
                        stats.add(15)
                        stats.add(14)
                        stats.add(13)
                        stats.add(12)
                        stats.add(10)
                        stats.add(8)
                    } else if (statMethod == StatMethod.ROLLFOURANDDROP) {
                        for (i in 0..5) {
                            val statRNs: MutableList<Int> = mutableListOf()
                            var lowest = 6
                            for (j in 0..3) {
                                val thisRn: Int = Random.nextInt(1, 7)
                                statRNs.add(thisRn)
                                lowest = if (thisRn < lowest) thisRn else lowest
                            }
                            statRNs.remove(lowest)
                            var sum: Int = 0
                            for (j in statRNs) {
                                sum += j
                            }
                            stats.add(sum)
                        }
                    }
                }
                var expandedStr : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedStr,
                    onExpandedChange = {
                        expandedStr = it
                    }
                ) {
                    var chosenStr : String? by remember {mutableStateOf(null)}
                    TextField(
                        value = if(chosenStr == null) "Select your Strength score" else "Strength: $chosenStr",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedStr,
                        onDismissRequest = { expandedStr = false }
                    ) {
                        stats.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    finalStats["Strength"] = option
                                    if(chosenStr != null){
                                        stats.add(chosenStr!!.toInt())
                                    }
                                    chosenStr = option.toString()
                                    expandedStr = false
                                    stats.remove(option)
                                }
                            )
                        }
                    }
                }

                var expandedDex : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedDex,
                    onExpandedChange = {
                        expandedDex = it
                    }
                ) {
                    var chosenDex: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenDex == null) "Select your Dexterity score" else "Dexterity: $chosenDex",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDex,
                        onDismissRequest = { expandedDex = false }
                    ) {
                        stats.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    finalStats["Dexterity"] = option
                                    if (chosenDex != null) {
                                        stats.add(chosenDex!!.toInt())
                                    }
                                    chosenDex = option.toString()
                                    expandedDex = false
                                    stats.remove(option)
                                }
                            )
                        }
                    }
                }

                var expandedCon : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedCon,
                    onExpandedChange = {
                        expandedCon = it
                    }
                ) {
                    var chosenCon: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenCon == null) "Select your Constitution score" else "Constitution: $chosenCon",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCon,
                        onDismissRequest = { expandedCon = false }
                    ) {
                        stats.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    finalStats["Dexterity"] = option
                                    if (chosenCon != null) {
                                        stats.add(chosenCon!!.toInt())
                                    }
                                    chosenCon = option.toString()
                                    expandedCon = false
                                    stats.remove(option)
                                }
                            )
                        }
                    }
                }

                var expandedInt : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedInt,
                    onExpandedChange = {
                        expandedInt = it
                    }
                ) {
                    var chosenInt: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenInt == null) "Select your Intelligence score" else "Intelligence: $chosenInt",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedInt,
                        onDismissRequest = { expandedInt = false }
                    ) {
                        stats.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    finalStats["Intelligence"] = option
                                    if (chosenInt != null) {
                                        stats.add(chosenInt!!.toInt())
                                    }
                                    chosenInt = option.toString()
                                    expandedInt = false
                                    stats.remove(option)
                                }
                            )
                        }
                    }
                }

                var expandedWis : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedWis,
                    onExpandedChange = {
                        expandedWis = it
                    }
                ) {
                    var chosenWis: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenWis == null) "Select your Wisdom score" else "Wisdom: $chosenWis",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedWis,
                        onDismissRequest = { expandedWis = false }
                    ) {
                        stats.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    finalStats["Wisdom"] = option
                                    if (chosenWis != null) {
                                        stats.add(chosenWis!!.toInt())
                                    }
                                    chosenWis = option.toString()
                                    expandedWis = false
                                    stats.remove(option)
                                }
                            )
                        }
                    }
                }

                var expandedCha : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedCha,
                    onExpandedChange = {
                        expandedCha = it
                    }
                ) {
                    var chosenCha: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenCha == null) "Select your Charisma score" else "Charisma: $chosenCha",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCha,
                        onDismissRequest = { expandedCha = false }
                    ) {
                        stats.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    finalStats["Charisma"] = option
                                    if (chosenCha != null) {
                                        stats.add(chosenCha!!.toInt())
                                    }
                                    chosenCha = option.toString()
                                    expandedCha = false
                                    stats.remove(option)
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        step = Step.THREE
                        Log.d("MYTAG", "final stats: $finalStats")
                    },
                    enabled = stats.isEmpty()
                ){
                    Text("Next!")
                }
            }
        }


    }
    // TODO: Step 3: Race

    // TODO: Step 4: Background

    // TODO: Step 5: Equipment

    // TODO: Step 6: Misc.
}