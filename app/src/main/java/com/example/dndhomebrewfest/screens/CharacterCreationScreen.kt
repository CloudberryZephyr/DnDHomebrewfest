package com.example.dndhomebrewfest.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.TypeConverter
import com.example.dndhomebrewfest.HBFUiState
import com.example.dndhomebrewfest.data.Character
import com.example.dndhomebrewfest.viewmodels.Background
import com.example.dndhomebrewfest.viewmodels.Class
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import com.example.dndhomebrewfest.viewmodels.Race
import com.example.dndhomebrewfest.viewmodels.RoomVM
import com.example.dndhomebrewfest.viewmodels.Subrace
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
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
    SIX,
    SEVEN,
    FINISH
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreationScreen(
    hbfVM: HBFViewModel,
    finish: () -> Unit,
    modifier: Modifier = Modifier,
    imageLoader: () -> Unit
) {
    var nextId by remember{mutableStateOf<Int>(0)}
    val hbfUIState by hbfVM.uiState.collectAsState()
    val dndViewModel : DnDViewModel = viewModel()
    var step : Step by remember{ mutableStateOf<Step>(Step.ONE)}

    var finalClass by remember { mutableStateOf<String>("")}
    var classInfo : Class? by remember {mutableStateOf<Class?>(null)}
    val finalStats by remember { mutableStateOf<MutableMap<String, Int>>(mutableMapOf())}
    var finalRace by remember {mutableStateOf<String>("")}
    var raceInfo : Race? = null
    var finalSubrace by remember {mutableStateOf<String>("")}
    var subraceInfo : Subrace? = null
    var finalBg by remember{mutableStateOf<String>("")}
    var bgInfo : Background? = null
    var finalSkills by remember{mutableStateOf<MutableList<String>>(mutableListOf())}

    val roomVM = RoomVM.getInstance()

    val characterList by roomVM.characters.collectAsState()
    for(chara in characterList){
        if(chara.character_id >= nextId){
            nextId = chara.character_id + 1
        }
    }

    // Step 1: Class
    val classes : MutableList<String> = mutableListOf()
    val abilityScores : MutableList<String> = mutableListOf()
    val races : MutableList<String> = mutableListOf()
    val bgs : MutableList<String> = mutableListOf()

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
        if(dndViewModel.subraceObjects.isEmpty()) {
            dndViewModel.getSubraces()
        }
        if(dndViewModel.backgroundObjects.isEmpty()) {
            dndViewModel.getBackgrounds()
        }
        if(dndViewModel.featureObjects.isEmpty()){
            dndViewModel.getFeatures()
        }
        if(dndViewModel.traitObjects.isEmpty()){
            dndViewModel.getTraits()
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
    for(bg in dndViewModel.backgroundObjects){
        bgs.add(bg.name)
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
            Text("Select Your Class!", color = Color.White)

            var selectedIndex by remember { mutableStateOf<Int?>(null) }

            Button(
                onClick = {
                    selectedIndex?.let { finalClass = classes[it] }
                    step = Step.TWO
                    Log.d("MYTAG", "button clicked")
                    dndViewModel.getClassLevels(finalClass.lowercase())
                },
                enabled = finalClass != ""
            ){
                Text("Next!")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(4.dp)

            ) {
                items(classes.size) { index ->
                    val charClass = classes[index]
                    val isSelected = selectedIndex == index

                    Card(
                        modifier = modifier.height(50.dp).requiredWidth(181.dp),
                        onClick = {
                            selectedIndex = if (selectedIndex == index) null else index
                            Log.i("MYTAG", "selected $charClass")
                            finalClass = charClass
                            for(c in dndViewModel.classObjects){
                                if(c.name == classes[index]){
                                    classInfo = c
                                }
                            }
                            Log.i("MYTAG", "Selected $finalClass. Json: $classInfo")
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
                            " a fixed list of stats, while rolling randomly generates stat values.",
                    textAlign = TextAlign.Center, color = Color.White)
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
                        value = if(chosenStr == null) "Strength" else "Strength: $chosenStr",
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

                Spacer(modifier = modifier.padding(10.dp))

                var expandedDex : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedDex,
                    onExpandedChange = {
                        expandedDex = it
                    }
                ) {
                    var chosenDex: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenDex == null) "Dexterity" else "Dexterity: $chosenDex",
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

                Spacer(modifier = modifier.padding(10.dp))

                var expandedCon : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedCon,
                    onExpandedChange = {
                        expandedCon = it
                    }
                ) {
                    var chosenCon: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenCon == null) "Constitution" else "Constitution: $chosenCon",
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
                                    finalStats["Constitution"] = option
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

                Spacer(modifier = modifier.padding(10.dp))

                var expandedInt : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedInt,
                    onExpandedChange = {
                        expandedInt = it
                    }
                ) {
                    var chosenInt: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenInt == null) "Intelligence" else "Intelligence: $chosenInt",
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

                Spacer(modifier = modifier.padding(10.dp))

                var expandedWis : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedWis,
                    onExpandedChange = {
                        expandedWis = it
                    }
                ) {
                    var chosenWis: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenWis == null) "Wisdom" else "Wisdom: $chosenWis",
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

                Spacer(modifier = modifier.padding(10.dp))

                var expandedCha : Boolean by remember {mutableStateOf(false)}
                ExposedDropdownMenuBox(
                    expanded = expandedCha,
                    onExpandedChange = {
                        expandedCha = it
                    }
                ) {
                    var chosenCha: String? by remember { mutableStateOf(null) }
                    TextField(
                        value = if (chosenCha == null) "Charisma" else "Charisma: $chosenCha",
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

                Spacer(modifier = modifier.padding(10.dp))

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

    // Step 3: Race
    if(step == Step.THREE){
        Column(
            modifier = modifier.fillMaxSize().alpha(if (step == Step.THREE) 1f else 0.5f)
                .pointerInput(step == Step.THREE) {
                    if (step != Step.THREE) {
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
            Text("Select Your Race!", color = Color.White)

            var selectedIndex by remember { mutableStateOf<Int?>(null) }

            Button(
                onClick = {
                    selectedIndex?.let { finalRace = races[it] }
                    step = Step.FOUR
                    Log.d("MYTAG", "race chosen")
                },
                enabled = finalRace != ""
            ){
                Text("Next!")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(4.dp)

            ) {
                items(races.size) { index ->
                    val charRace = races[index]
                    val isSelected = selectedIndex == index

                    Card(
                        modifier = modifier.height(50.dp).requiredWidth(181.dp),
                        onClick = {
                            selectedIndex = if (selectedIndex == index) null else index
                            for(r in dndViewModel.raceObjects){
                                if(r.name == races[index]){
                                    raceInfo = r
                                }
                            }
                            finalRace = charRace
                            Log.i("MYTAG", "Selected $finalRace. Json: $raceInfo")
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = charRace)
                        }
                    }
                }
            }
        }
    }

    // Step 4: Background
    if(step == Step.FOUR){
        Column(
            modifier = modifier.fillMaxSize().alpha(if (step == Step.FOUR) 1f else 0.5f)
                .pointerInput(step == Step.FOUR) {
                    if (step != Step.FOUR) {
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
            Text("Select Your Background!", color = Color.White)

            var selectedIndex by remember { mutableStateOf<Int?>(null) }

            Button(
                onClick = {
                    selectedIndex?.let { finalBg = bgs[it] }
                    step = Step.FINISH
                    Log.d("MYTAG", "background chosen")
                },
                enabled = finalBg != ""
            ){
                Text("Next!")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(4.dp)

            ) {
                items(bgs.size) { index ->
                    val charBg = bgs[index]
                    val isSelected = selectedIndex == index

                    Card(
                        modifier = modifier.height(50.dp).requiredWidth(181.dp),
                        onClick = {
                            selectedIndex = if (selectedIndex == index) null else index
                            finalBg = charBg
                            Log.i("MYTAG", "Selected $finalBg.")
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = charBg)
                        }
                    }
                }
            }
        }
    }

    // TODO: Step 5: Equipment
//    if(step == Step.FIVE){
//        Column(
//            modifier = modifier.fillMaxSize().alpha(if (step == Step.FIVE) 1f else 0.5f)
//                .pointerInput(step == Step.FIVE) {
//                    if (step != Step.FIVE) {
//                        awaitPointerEventScope {
//                            while (true) {
//                                awaitPointerEvent()
//                            }
//                        }
//                    }
//                }
//            ,
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Select Your Equipment!")
//
//            var selectedIndex by remember { mutableStateOf<Int?>(null) }
//
//            for(c in dndViewModel.classObjects){
//                if(finalClass == c.name){
//                    classInfo = c
//                }
//            }
//
//            Button(
//                onClick = {
//                    selectedIndex?.let {  }
//                    step = Step.FIVE
//                    Log.d("MYTAG", "equipment chosen")
//                },
//                enabled = false
//            ){
//                Text("Next!")
//            }
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(1),
//                modifier = modifier.fillMaxSize(.3f)
//            ) {
//                items(bgs.size) { index ->
//                    val charBg = bgs[index]
//                    val isSelected = selectedIndex == index
//
//                    Card(
//                        onClick = {
//                            selectedIndex = if (selectedIndex == index) null else index
//                            finalBg = charBg
//                            Log.i("MYTAG", "Selected $finalBg.")
//                        }
//                    ) {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(text = charBg)
//                        }
//                    }
//                }
//            }
//        }
//    }

    // TODO: Step 6: Skills


    // TODO: Step 7: Spells (depends)

    // TODO: Populate database with character information
    if(step == Step.FINISH){

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            var name by remember {mutableStateOf<String>("")}

            Box(modifier.size(300.dp)) {
                CharacterImage(nextId, hbfUIState, LocalContext.current, imageLoader)
            }

            TextField(
                value = name,
                onValueChange = {name = it},
                label = {Text("Enter your character's name")},
                singleLine = true
            )
            Button(
                onClick = {
                    if(classInfo == null){
                        for(c in dndViewModel.classObjects){
                            if(c.name == finalClass){
                                classInfo = c
                            }
                        }
                    }

                    if(raceInfo == null){
                        for(r in dndViewModel.raceObjects){
                            if(r.name == finalRace){
                                raceInfo = r
                            }
                        }
                    }

                    if(bgInfo == null){
                        for(bg in dndViewModel.backgroundObjects){
                            if(bg.name == finalBg){
                                bgInfo = bg
                            }
                        }
                    }

                    for(asi in raceInfo?.ability_bonuses!!){
                        Log.i("MYTAG", "$asi, ${raceInfo.ability_bonuses}")
                        if(asi.ability_score.name == "STR"){
                            finalStats["Strength"] = (finalStats["Strength"] ?: 0) + asi.bonus
                        }
                        if(asi.ability_score.name == "DEX"){
                            finalStats["Dexterity"] = (finalStats["Dexterity"] ?: 0) + asi.bonus
                        }
                        if(asi.ability_score.name == "CON"){
                            finalStats["Constitution"] = (finalStats["Constitution"] ?: 0) + asi.bonus
                        }
                        if(asi.ability_score.name == "INT"){
                            finalStats["Intelligence"] = (finalStats["Intelligence"] ?: 0) + asi.bonus
                        }
                        if(asi.ability_score.name == "WIS"){
                            finalStats["Wisdom"] = (finalStats["Wisdom"] ?: 0) + asi.bonus
                        }
                        if(asi.ability_score.name == "CHA"){
                            finalStats["Charisma"] = (finalStats["Charisma"] ?: 0) + asi.bonus
                        }
                    }
                    if(!raceInfo.subraces.isEmpty()){
                        for(sr in dndViewModel.subraceObjects){
                            if(sr.name == raceInfo.subraces.first().name){
                                finalSubrace = sr.name
                                subraceInfo = sr
                            }
                        }
                    }

                    val savingThrows : MutableList<String> = mutableListOf()
                    for(save in classInfo?.saving_throws!!){
                        savingThrows.add(save.name)
                    }

                    val features : MutableMap<String, String> = mutableMapOf()

                    for(l in dndViewModel.classLevelObjects){
                        if(l.level == 1){
                            for(f in l.features){
                                val featureName = f.name
                                for(feature in dndViewModel.featureObjects){
                                    if(featureName == feature.name){
                                        features[featureName] = feature.desc[0]
                                    }
                                }
                            }
                        }
                    }
                    for(rf in raceInfo.traits){
                        val traitName = rf.name
                        for(trait in dndViewModel.traitObjects){
                            if(trait.name == traitName){
                                features[traitName] = trait.desc[0]
                            }
                        }
                    }

                    val equipment : MutableList<String> = mutableListOf()

                    if(bgInfo != null){
                        for(s in bgInfo.starting_proficiencies){
                            finalSkills.add(s.name)
                        }
                        for(e in bgInfo.starting_equipment){
                            equipment.add(e.equipment.name + ": " + e.quantity)
                        }
                    }

                    if(subraceInfo != null) {
                        for (asi in subraceInfo.ability_bonuses) {
                            if (asi.ability_score.name == "STR") {
                                finalStats["Strength"] = (finalStats["Strength"] ?: 0) + asi.bonus
                            }
                            if (asi.ability_score.name == "DEX") {
                                finalStats["Dexterity"] = (finalStats["Dexterity"] ?: 0) + asi.bonus
                            }
                            if (asi.ability_score.name == "CON") {
                                finalStats["Constitution"] = (finalStats["Constitution"] ?: 0) + asi.bonus
                            }
                            if (asi.ability_score.name == "INT") {
                                finalStats["Intelligence"] = (finalStats["Intelligence"] ?: 0) + asi.bonus
                            }
                            if (asi.ability_score.name == "WIS") {
                                finalStats["Wisdom"] = (finalStats["Wisdom"] ?: 0) + asi.bonus
                            }
                            if (asi.ability_score.name == "CHA") {
                                finalStats["Charisma"] = (finalStats["Charisma"] ?: 0) + asi.bonus
                            }
                        }
                        for(srf in subraceInfo.racial_traits){
                            val traitName = srf.name
                            for(trait in dndViewModel.traitObjects){
                                if(trait.name == traitName){
                                    features[traitName] = trait.desc[0]
                                }
                            }
                        }
                    }

                    classInfo?.let {
                        for(e in it.starting_equipment){
                            equipment.add(e.equipment.name + ": " + e.quantity)
                        }
                    }

                    val uri = hbfUIState.current_char_img_name

                    val charToAdd = Character(
                        character_id = nextId,
                        name = name,
                        character_class = finalClass,
                        char_img_uri =  uri,
                        character_stats_json = fromStringIntMap(finalStats),
                        character_race = finalRace,
                        saving_throws_json = fromStringList(savingThrows),
                        skills_json = fromStringList(finalSkills),
                        equipment_json = fromStringList(equipment),
                        features_json = fromStringStringMap(features),
                        spells_json = fromStringList(listOf()) // NEED TO ADD THIS
                    )
                    roomVM.addCharacter(charToAdd)

                    Log.i("MYTAG", charToAdd.toString())

                    finish()
                }
            ){
                Text("Create your Character!")
            }
        }
    }
}

@TypeConverter
fun fromStringList(value: List<String>): String {
    return Json.encodeToString(value)
}

@TypeConverter
fun fromStringStringMap(value: Map<String, String>) : String {
    return Json.encodeToString(value)
}

@TypeConverter
fun fromStringIntMap(value: Map<String, Int>) : String {
    return Json.encodeToString(value)
}

@Composable
fun CharacterImage(charID : Int, hbfUiState: HBFUiState, context: Context, imageLoader: () -> Unit, modifier : Modifier = Modifier) {
    val imageName = hbfUiState.current_char_img_name

    Box() {
        Column() {
            Button(
                onClick = {
                    imageLoader()
                }
            ) {
                Text("Add Art")
            }

            if ((imageName == null || imageName == "")) {
            } else {

                val directory = context.filesDir
                val file = File(directory, imageName)
                val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                val bitmapPainter = BitmapPainter(bitmap.asImageBitmap())

                Image(
                    painter = bitmapPainter,
                    contentDescription = null
                )
            }
        }
    }
}