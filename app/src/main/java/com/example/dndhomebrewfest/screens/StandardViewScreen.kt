package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dndhomebrewfest.R
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.lifecycle
import com.example.dndhomebrewfest.HBFUiState
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.*
import com.example.dndhomebrewfest.viewmodels.AlignmentDnD
import com.example.dndhomebrewfest.viewmodels.Background
import com.example.dndhomebrewfest.viewmodels.BackgroundFeature
import com.example.dndhomebrewfest.viewmodels.Choice
import com.example.dndhomebrewfest.viewmodels.Class
import com.example.dndhomebrewfest.viewmodels.ClassLevel
import com.example.dndhomebrewfest.viewmodels.Condition
import com.example.dndhomebrewfest.viewmodels.Damage
import com.example.dndhomebrewfest.viewmodels.DamageType
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.Equipment
import com.example.dndhomebrewfest.viewmodels.EquipmentCategory
import com.example.dndhomebrewfest.viewmodels.Equipments
import com.example.dndhomebrewfest.viewmodels.Feat
import com.example.dndhomebrewfest.viewmodels.Feature
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import com.example.dndhomebrewfest.viewmodels.ObjectReference
import com.example.dndhomebrewfest.viewmodels.Option
import com.example.dndhomebrewfest.viewmodels.OptionsSet
import java.util.stream.IntStream.range

@Composable
fun StandardViewScreen(hbfVM : HBFViewModel, modifier : Modifier = Modifier) {

    val hbfUIState by hbfVM.uiState.collectAsState()
    val dndViewModel : DnDViewModel = viewModel()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = hbfUIState.current_filter,
                onValueChange = hbfVM::onCurrentFilterChanged,
                shape = shapes.large,
                label = { Text("Search") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        modifier = modifier.size(30.dp)
                    )
                }
            )
        }

        when (hbfUIState.current_type) {
            "Ability Scores" -> SearchAbilityScores(hbfVM, dndViewModel)
            "Alignments" -> SearchAlignments(hbfVM, dndViewModel)
            "Backgrounds" -> SearchBackgrounds(hbfVM,dndViewModel)
            "Classes" -> SearchClasses( hbfVM, dndViewModel)
            "Conditions" -> SearchConditions(hbfVM, dndViewModel)
            "Damage Types" -> SearchDamageTypes(hbfVM, dndViewModel)
            "Equipment" -> SearchEquipment(hbfVM, dndViewModel)
            "Feats" -> SearchFeats(hbfVM, dndViewModel)
            "Languages" -> SearchLanguages(hbfVM, dndViewModel)
            "Magic Items" -> SearchMagicItems(hbfVM, dndViewModel)
            "Magic Schools" -> SearchMagicSchool(hbfVM, dndViewModel)
            "Monsters" -> SearchMonsters(hbfVM, dndViewModel)
            "Races" -> SearchRaces(hbfVM, dndViewModel)
            "Spells" -> SearchSpells(hbfVM, dndViewModel)
            "Subclasses" -> SearchSubclasses(hbfVM, dndViewModel)
            "Traits" -> SearchTraits(hbfVM, dndViewModel)
            else -> SearchWeaponProperties(hbfVM, dndViewModel)
        }

    }

}

@Composable
fun SearchAbilityScores(hbfVM: HBFViewModel, dndViewModel : DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowAbilityScore((hbfUiState.showThisObject) as AbilityScore, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.abilityScoreObjects.isEmpty()) {
            dndViewModel.getAbilityScores()
        }
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.abilityScoreObjects) { item ->

            if (item.full_name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(item.full_name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAbilityScore(abilityScore: AbilityScore, hbfVM : HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = abilityScore.full_name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                for (s in abilityScore.desc) {
                    Text(text = s, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = modifier.height(7.dp))
                }

                Spacer(modifier = modifier.height(3.dp))

                Text(text = "Related Skills:")
                for (skill in abilityScore.skills) {
                    Text(text = skill.name)
                }
            }
        }

    }
}

@Composable
fun SearchAlignments(hbfVM : HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.alignmentDnDObjects.isEmpty()) {
            dndViewModel.getAlignments()
        }
    }

    if(hbfUiState.showThisObject != null) {
        ShowAlignment((hbfUiState.showThisObject) as AlignmentDnD, hbfVM)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.alignmentDnDObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAlignment(alignment: AlignmentDnD, hbfVM : HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = alignment.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text(text = alignment.desc, style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
            }
        }

    }
}

@Composable
fun SearchBackgrounds(hbfVM : HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowBackground((hbfUiState.showThisObject) as Background, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.backgroundObjects.isEmpty()) {
            dndViewModel.getBackgrounds()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.backgroundObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowBackground(background: Background, hbfVM : HBFViewModel, modifier: Modifier = Modifier) {
    var expandFeature by remember { mutableStateOf(false) }
    var expandTraits by remember { mutableStateOf(false) }
    var expandIdeals by remember { mutableStateOf(false) }
    var expandBonds by remember { mutableStateOf(false) }
    var expandFlaws by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = background.name,
                    style = typography.titleLarge,
                    textDecoration = TextDecoration.Underline
                )

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Starting Proficiencies:")
                for (skill in background.starting_proficiencies) {
                    Text(text = skill.name)
                }

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Starting Equipment:")
                for (e in background.starting_equipment) {
                    Text(text = "${e.equipment.name} x${e.quantity}")
                }

                Spacer(modifier = modifier.height(10.dp))

                for (startingOption in background.starting_equipment_options) {
                    Text("Choose ${startingOption.choose} ${startingOption.from.equipment_category?.name}")
                }

                Spacer(modifier = modifier.height(10.dp))

                TextButton(
                    onClick = {
                        expandFeature = !expandFeature
                    },
                ) {
                    Row() {
                        Text(text = "Feature: ${background.feature.name}")
                        Icon(
                            painter = painterResource(if (expandFeature) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandFeature) {
                    for (p in background.feature.desc) {
                        Text(
                            text = p, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding( horizontal = 25.dp)
                        )
                    }
                }

                Spacer(modifier = modifier.height(10.dp))


                TextButton(
                    onClick = {
                        expandTraits = !expandTraits
                    },
                ) {
                    Row() {
                        Text(text = "Choose ${background.personality_traits.choose} traits:")
                        Icon(
                            painter = painterResource(if (expandTraits) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandTraits) {
                    for (option in background.personality_traits.from.options!!) {
                        Text(
                            text = (option as Option.OptionObject).string!!,
                            style = typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = modifier.height(7.dp))
                    }
                }

                Spacer(modifier = modifier.height(10.dp))


                TextButton(
                    onClick = {
                        expandIdeals = !expandIdeals
                    },
                ) {
                    Row() {
                        Text(text = "Choose ${background.ideals.choose} ideal:")
                        Icon(
                            painter = painterResource(if (expandIdeals) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandIdeals) {
                    for (option in background.ideals.from.options!!) {
                        Text(
                            text = (option as Option.OptionObject).desc!!,
                            style = typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        var alignments = ""
                        for (i in range(0, option.alignments!!.size - 1)) {
                            alignments = alignments + option.alignments[i].name + ", "
                        }
                        alignments += option.alignments[option.alignments.size - 1].name

                        Text(
                            text = alignments, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic
                        )

                        Spacer(modifier = modifier.height(7.dp))
                    }
                }

                Spacer(modifier = modifier.height(3.dp))

                TextButton(
                    onClick = {
                        expandBonds = !expandBonds
                    },
                ) {
                    Row() {
                        Text(text = "Choose ${background.bonds.choose} bond:")
                        Icon(
                            painter = painterResource(if (expandBonds) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }
                if (expandBonds) {
                    for (option in background.bonds.from.options!!) {
                        Text(
                            text = (option as Option.OptionObject).string!!,
                            style = typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = modifier.height(7.dp))
                    }
                }

                Spacer(modifier = modifier.height(3.dp))

                TextButton(
                    onClick = {
                        expandFlaws = !expandFlaws
                    },
                ) {
                    Row() {
                        Text(text = "Choose ${background.flaws.choose} flaw:")
                        Icon(
                            painter = painterResource(if (expandFlaws) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandFlaws) {
                    for (option in background.flaws.from.options!!) {
                        Text(
                            text = (option as Option.OptionObject).string!!,
                            style = typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = modifier.height(7.dp))
                    }
                }

            }
        }

    }
}

@Composable
fun SearchClasses(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowClasses((hbfUiState.showThisObject) as Class, hbfVM, dndViewModel)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.classObjects.isEmpty()) {
            dndViewModel.getClasses()
        }
        if(dndViewModel.featureObjects.isEmpty()) {
            dndViewModel.getFeatures()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.classObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ShowClasses(classObj: Class, hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    var expandProficiencies by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        dndViewModel.getClassLevels(classObj.index)
    }
    
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = classObj.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                var savingThrows = ""
                for (i in range(0, classObj.saving_throws.size - 1)) {
                    savingThrows = savingThrows + classObj.saving_throws[i].name + ", "
                }
                savingThrows += classObj.saving_throws[classObj.saving_throws.size - 1].name

                Text(text = "Saving Throws: $savingThrows")

                Text(text = "Hit dice: d${classObj.hit_die}")

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Choose Proficiencies", style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                for (prof in classObj.proficiency_choices) {
                    prof.desc?.let {
                        Text(
                            text = it, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(modifier = modifier.height(10.dp))

                TextButton(
                    onClick = {
                        expandProficiencies = !expandProficiencies
                    },
                ) {
                    Row() {
                        Text(text = "Proficiencies:")
                        Icon(
                            painter = painterResource(if (expandProficiencies) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandProficiencies) {
                    for (p in classObj.proficiencies) {
                        Text(
                            text = p.name, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding( horizontal = 25.dp)
                        )
                    }
                }

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Starting Equipment:")
                for (e in classObj.starting_equipment) {
                    Text(text = "${e.equipment.name} x${e.quantity}")
                }

                Spacer(modifier = modifier.height(10.dp))

                Text("Starting Equipment:")
                for (startingOption in classObj.starting_equipment_options) {
                    Text("Choose ${startingOption.choose} ${startingOption.desc}", style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                }
                // skipping info on multiclassing for now

                // class leveling
                for (level in dndViewModel.classLevelObjects) {
                    ShowClassLevel(level, dndViewModel)
                }

                Spacer(modifier.height(10.dp))

                // spell casting
                if (classObj.spellcasting != null) {
                    var expandSpellCasting by remember {mutableStateOf(false)}
                    TextButton(
                        onClick = {
                            expandSpellCasting = !expandSpellCasting
                        },
                    ) {
                        Row() {
                            Text("Spellcasting", textDecoration = TextDecoration.Underline, style = typography.bodyLarge)
                            Icon(
                                painter = painterResource(if (expandSpellCasting) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                contentDescription = null,
                                modifier = modifier.size(20.dp)
                            )
                        }
                    }

                    if (expandSpellCasting) {
                        // spell slot table

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Level", style = typography.bodyMedium)
                                for (level in dndViewModel.classLevelObjects) {
                                    Text("${level.level}", style = typography.bodyMedium)
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text( "Cantrips", style = typography.bodyMedium)
                                for (level in dndViewModel.classLevelObjects) {
                                    Text("${level.spellcasting?.cantrips_known ?: "-"}", style = typography.bodyMedium)
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Spells", style = typography.bodyMedium)
                                for (level in dndViewModel.classLevelObjects){
                                    Text("${level.spellcasting?.spells_known ?: "-"}", style = typography.bodyMedium)
                                }
                            }
                            for (i in range(1, 10)){
                                Column() {
                                    Text("$i", style = typography.bodyMedium)
                                    for (level in dndViewModel.classLevelObjects) {
                                        when (i) {
                                            1 -> Text("${level.spellcasting?.spell_slots_level_1 ?: "-"}", style = typography.bodyMedium)
                                            2 -> Text ("${level.spellcasting?.spell_slots_level_2 ?: "-"}", style = typography.bodyMedium)
                                            3 -> Text("${level.spellcasting?.spell_slots_level_3 ?: "-"}", style = typography.bodyMedium)
                                            4 -> Text("${level.spellcasting?.spell_slots_level_4 ?: "-"}", style = typography.bodyMedium)
                                            5 -> Text ("${level.spellcasting?.spell_slots_level_5 ?: "-"}", style = typography.bodyMedium)
                                            6 -> Text("${level.spellcasting?.spell_slots_level_6 ?: "-"}", style = typography.bodyMedium)
                                            7 -> Text("${level.spellcasting?.spell_slots_level_7 ?: "-"}", style = typography.bodyMedium)
                                            8 -> Text ("${level.spellcasting?.spell_slots_level_8 ?: "-"}", style = typography.bodyMedium)
                                            else -> Text("${level.spellcasting?.spell_slots_level_9 ?: "-"}", style = typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        }




                        Text("Spellcasting ability: ${classObj.spellcasting.spellcasting_ability.name}",
                            style = typography.bodyMedium)
                        Spacer(modifier.height(5.dp))
                        for (info in classObj.spellcasting.info) {
                            var expand by remember { mutableStateOf(false) }

                            TextButton(
                                onClick = {
                                    expand = !expand
                                },
                            ) {
                                Row() {
                                    Text(text = info.name)
                                    Icon(
                                        painter = painterResource(if (expand) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                        contentDescription = null,
                                        modifier = modifier.size(20.dp)
                                    )
                                }
                            }

                            if (expand) {
                                for (p in info.desc) {
                                    Text(
                                        text = p, style = typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = modifier.padding(horizontal = 25.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowClassLevel(level : ClassLevel, dndViewModel: DnDViewModel, modifier : Modifier = Modifier) {
    var expand by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        if(dndViewModel.featureObjects.isEmpty()) {
            dndViewModel.getFeatures()
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 15.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {
                expand = !expand
            },
        ) {
            Row() {
                Text("Level ${level.level}", style = typography.bodyLarge,
                    textDecoration = TextDecoration.Underline)
                Icon(
                    painter = painterResource(if (expand) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                    contentDescription = null,
                    modifier = modifier.size(20.dp)
                )
            }
        }

        if (expand) {
            if (level.ability_score_bonuses != 0) {
                Text("+${level.ability_score_bonuses} ability score bonus")
            }
            Text("Proficiency Bonus: +${level.prof_bonus}")

            when (level.classRef.index) {
                "barbarian" -> {
                    Text("Rage Count: ${level.class_specific.rage_count}")
                    Text("Rage Damage Bonus: +${level.class_specific.rage_damage_bonus}")
                    if (level.class_specific.brutal_critical_dice != 0)  Text("Brutal Critical Dice: ${level.class_specific.brutal_critical_dice}")
                }
                "bard" -> {
                    Text("Bardic Inspiration: d${level.class_specific.bardic_inspiration_die}")
                    if(level.class_specific.song_of_rest_die != 0) Text("Song of Rest: d${level.class_specific.song_of_rest_die}")
                    if(level.class_specific.magical_secrets_max_5 != 0) Text("Magical Secret Slots below 5: ${level.class_specific.magical_secrets_max_5}")
                    if(level.class_specific.magical_secrets_max_7 != 0) Text("Magical Secret Slots below 7: ${level.class_specific.magical_secrets_max_7}")
                    if(level.class_specific.magical_secrets_max_9 != 0) Text("Magical Secret Slots below 9: ${level.class_specific.magical_secrets_max_9}")
                }
                "cleric" -> {
                    if(level.class_specific.channel_divinity_charges != 0) Text("Channel Divinity Charges: ${level.class_specific.channel_divinity_charges}")
                    if(level.class_specific.destroy_undead_cr != 0.0) Text ("Destroy Undead CR ${level.class_specific.destroy_undead_cr} or Below")
                }
                "druid" -> {
                    if (level.class_specific.wild_shape_max_cr != 0.0) {
                        Text("Wild Shape Max CR ${level.class_specific.wild_shape_max_cr}")
                        if(level.class_specific.wild_shape_swim == true) {
                            if (level.class_specific.wild_shape_fly == true) {
                                Text("Swimming and Flying Speed Allowed", textAlign = TextAlign.Center)
                            } else {
                                Text("Swimming Speed Allowed")
                            }
                        }
                    }
                }
                "fighter" -> {
                    if (level.class_specific.action_surges != 0) Text("Action Surges: ${level.class_specific.action_surges}")
                    if (level.class_specific.indomitable_uses != 0) Text("Indomitable Uses: ${level.class_specific.indomitable_uses}")
                    if (level.class_specific.extra_attacks != 0) Text("Extra Attacks: ${level.class_specific.extra_attacks}")
                }
                "monk" -> {
                    Text("Martial Arts Dice: ${level.class_specific.martial_arts?.dice_count}d${level.class_specific.martial_arts?.dice_value}")
                    if (level.class_specific.ki_points != 0) Text("Ki Points: ${level.class_specific.ki_points}")
                    if (level.class_specific.unarmored_movement != 0) Text("Unarmored Movement: +${level.class_specific.unarmored_movement} feet")
                }
                "paladin" -> {
                    if (level.class_specific.aura_range != 0) Text("Aura Range: ${level.class_specific.aura_range} feet")
                }
                "ranger" -> {
                    if (level.class_specific.favored_enemies != 0) Text("Favored Enemies: ${level.class_specific.favored_enemies}")
                    if (level.class_specific.favored_terrain != 0) Text("Favored Terrains: ${level.class_specific.favored_terrain}")
                }
                "rogue" -> {
                    Text("Sneak Attack: ${level.class_specific.sneak_attack?.dice_count}d${level.class_specific.sneak_attack?.dice_value}")
                }
                "sorcerer" -> {
                    if (level.class_specific.sorcery_points != 0) Text("Sorcery Points: ${level.class_specific.sorcery_points}")
                    if (level.class_specific.metamagic_known != 0) Text("Metamagics Known: ${level.class_specific.metamagic_known}")
                    if (level.class_specific.creating_spell_slots?.isEmpty() == false) {
                        Text ("Spell Slot Creation Table:")
                        for (slot in level.class_specific.creating_spell_slots) {
                            Text("${slot.sorcery_point_cost} Sorcery Points for Level ${slot.spell_slot_level} Spell Slot", style = typography.bodyMedium)
                        }
                    }
                }
                "warlock" -> {
                    if (level.class_specific.invocations_known != 0) Text("Invocations: ${level.class_specific.invocations_known}")
                    if (level.class_specific.mystic_arcanum_level_6 != 0) {
                        val arcana : String = "6" +
                                if (level.class_specific.mystic_arcanum_level_7 != 0) ", 7" else "" +
                                if (level.class_specific.mystic_arcanum_level_8 != 0) ", 8" else "" +
                                if (level.class_specific.mystic_arcanum_level_9 != 0) ", 9" else ""
                        Text ("Level $arcana Mystic Arcanum Unlocked", textAlign = TextAlign.Center)
                    }
                }
                "wizard" -> {
                    Text("Arcane Recovery Levels: ${level.class_specific.arcane_recovery_levels}")
                }
            }

            // get the indexes of the features specific to this level
            val featureList : MutableList<Feature> = mutableListOf()
            for (classfeature in level.features) {
                for (feature in dndViewModel.featureObjects) {
                    if (classfeature.index == feature.index) {
                        featureList.add(feature)
                    }
                }
            }

            for (feature in featureList) {

                var expandFeature by remember { mutableStateOf(false) }

                TextButton(
                    onClick = {
                        expandFeature = !expandFeature
                    },
                ) {
                    Row() {
                        Text(
                            feature.name, style = typography.bodyLarge, color = Color.Black, textAlign = TextAlign.Center
                        )
                        Icon(
                            painter = painterResource(if (expandFeature) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandFeature) {
                    for (desc in feature.desc) {
                        Text(desc, style = typography.bodyMedium, textAlign = TextAlign.Center)
                        Spacer(modifier.height(3.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchConditions(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowCondition((hbfUiState.showThisObject) as Condition, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.conditionObjects.isEmpty()) {
            dndViewModel.getConditions()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.conditionObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowCondition(condition : Condition, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = condition.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                for (desc in condition.desc) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}

@Composable
fun SearchDamageTypes(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowDamageType((hbfUiState.showThisObject) as DamageType, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.damageTypeObjects.isEmpty()) {
            dndViewModel.getDamageTypes()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.damageTypeObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowDamageType(dmgType: DamageType, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = dmgType.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                for (desc in dmgType.desc) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}

@Composable
fun ShowEquipment(equipment: Equipment, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = equipment.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("${equipment.cost.quantity}${equipment.cost.unit}", style = typography.bodyMedium)
                    if (equipment.quantity != null && equipment.quantity != 0) Text("x${equipment.quantity}", style = typography.bodyMedium)
                    if (equipment.weight != null) Text("${equipment.weight}lbs", style = typography.bodyMedium)
                }

                if (equipment.speed != null) {
                    if (equipment.speed.quantity != null && equipment.speed.unit != null) {
                        Text("Speed: ${equipment.speed.quantity} ${equipment.speed.unit}", style = typography.bodyMedium)
                    }
                }

                Spacer(modifier = modifier.height(10.dp))

                if(equipment.armor_class != null) {
                    var string = "${equipment.armor_class.base}"
                    if (equipment.armor_class.dex_bonus) {
                        string = string + " + DEX"
                    }
                    if (equipment.armor_class.max_bonus != null) {
                        string = string + ", Max Bonus ${equipment.armor_class.max_bonus}"
                    }
                    Text("AC: ${string}", style = typography.bodyMedium)
                }

                if(equipment.str_minimum != null && equipment.str_minimum != 0) {
                    Text("Min STR score to wield: ${equipment.str_minimum}",
                        style = typography.bodyMedium)
                }

                if (equipment.weapon_range != null) {
                    Text("Weapon Range: ${equipment.weapon_range}",
                        style = typography.bodyMedium)
                }

                if (equipment.range != null) {
                    Text("Range: ${equipment.range.normal}${if (equipment.range.long != null) " normal, ${equipment.range.long} long" else ""}",
                        style = typography.bodyMedium)
                }

                if (equipment.throw_range != null) {
                    Text("Throw Range: ${equipment.throw_range}${if (equipment.throw_range.long != null) " normal, ${equipment.throw_range.long} long" else ""}",
                        style = typography.bodyMedium)
                }

                if (equipment.capacity != null) {
                    Text("Capacity: ${equipment.capacity}",
                        style = typography.bodyMedium)
                }

                if (equipment.damage != null) {
                    Text("Damage: ${(equipment.damage as Damage.DamageObject).damage_dice} ${equipment.damage.damage_type}",
                        style = typography.bodyMedium)
                }

                if (equipment.two_handed_damage != null) {
                    Text("Two Handed Damage: ${(equipment.two_handed_damage as Damage.DamageObject).damage_dice} ${(equipment.two_handed_damage.damage_type?.name)}",
                        style = typography.bodyMedium)
                }

                if (equipment.weapon_range != null) {
                    Text("Weapon Range: ${equipment.weapon_range}",
                        style = typography.bodyMedium)
                }

                for (desc in equipment.desc) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                for (thing in equipment.special) {
                    Text(
                        text = thing, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                for (thing in equipment.contents) {
                    Text(
                        text = "${thing.item.name} x${thing.quantity}", style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                for (prop in equipment.properties) {
                    Text(
                        text = prop.name, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

    }
}

@Composable
fun SearchEquipment(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowEquipment((hbfUiState.showThisObject) as Equipment, hbfVM)
    }

    LaunchedEffect(Unit) {
        if(dndViewModel.equipmentObjects.isEmpty()) {
            dndViewModel.getEquipment()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.equipmentObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter) || item.equipment_category.name.contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(65.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.image != null) {
//                            Log.d("MyTAG", "IMG: ${item.image}")
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data("https://www.dnd5eapi.co${item.image}")
                                    .crossfade(true)
                                    .listener(
                                        onError = { request, result ->
                                            // The request failed. 'result.throwable' contains the error.
                                            Log.e("ImageLoader", "Loading image failed for URL: ${request.data}", result.throwable)
                                        }
                                    )
                                    .build(),
                                error = painterResource(R.drawable.ic_broken_image),
                                placeholder = painterResource(R.drawable.loading_img),
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )
                        }
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchFeats(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowFeat((hbfUiState.showThisObject) as Feat, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.featObjects.isEmpty()) {
            dndViewModel.getFeats()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.featObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowFeat(feat: Feat, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = feat.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                for (desc in feat.desc) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                if (!feat.prerequisites.isEmpty()) {
                    var expand by remember { mutableStateOf(false)}

                    TextButton(
                        onClick = {
                            expand = !expand
                        },
                    ) {
                        Row() {
                            Text(
                                "Prerequisites", style = typography.bodyLarge, color = Color.Black, textAlign = TextAlign.Center
                            )
                            Icon(
                                painter = painterResource(if (expand) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                contentDescription = null,
                                modifier = modifier.size(20.dp)
                            )
                        }
                    }

                    if (expand) {
                        for (prereq in feat.prerequisites) {
                            if (prereq.type != null) {
                                Text(
                                    text = prereq.type, style = typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (prereq.spell != null) {
                                Text(
                                    text = prereq.spell, style = typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (prereq.level != null) {
                                Text(
                                    text = "Level ${prereq.level}", style = typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (prereq.feature != null) {
                                Text(
                                    text = prereq.feature, style = typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (prereq.ability_score != null && prereq.minimum_score != null) {
                                Text(
                                    text = "${prereq.ability_score.name} score minimum: ${prereq.minimum_score}", style = typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }


                }


            }
        }

    }
}

@Composable
fun SearchLanguages(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowLanguage((hbfUiState.showThisObject) as Language, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.languageObjects.isEmpty()) {
            dndViewModel.getLanguages()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.languageObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowLanguage(lang: Language, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = lang.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                if (lang.script != null) {
                    Text(
                        text = lang.script, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                var speakers = ""

                for (desc in lang.typical_speakers) {
                    speakers = speakers + desc + " "
                }

                Text(
                    text = "Speakers: ${speakers}", style = typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                if (lang.desc != null) Text(lang.desc, style = typography.bodyMedium, textAlign = TextAlign.Center)
            }
        }

    }
}

@Composable
fun SearchMagicItems(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowMagicItems((hbfUiState.showThisObject) as MagicItem, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.magicItemObjects.isEmpty()) {
            dndViewModel.getMagicItems()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.magicItemObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(65.dp).requiredWidth(250.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.image != null) {
//                            Log.d("MyTAG", "IMG: ${item.image}")
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data("https://www.dnd5eapi.co${item.image}")
                                    .crossfade(true)
                                    .listener(
                                        onError = { request, result ->
                                            // The request failed. 'result.throwable' contains the error.
                                            Log.e("ImageLoader", "Loading image failed for URL: ${request.data}", result.throwable)
                                        }
                                    )
                                    .build(),
                                error = painterResource(R.drawable.ic_broken_image),
                                placeholder = painterResource(R.drawable.loading_img),
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )
                        }
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowMagicItems(magicItem : MagicItem, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = magicItem.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text(magicItem.equipment_category.name,  style = typography.bodyMedium)
                Text(magicItem.rarity.name, style = typography.bodyMedium)

                for (desc in magicItem.desc) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                if (!magicItem.variants.isEmpty()) {
                    var variants = magicItem.variants[0].name

                    for (variant in magicItem.variants.subList(1,magicItem.variants.size-1)) {
                        variants = variants + ", " + variant.name
                    }

                    Text("Variants: ${variants}", style = typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
        }

    }
}

@Composable
fun SearchMagicSchool(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowMagicSchool((hbfUiState.showThisObject) as MagicSchool, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.magicSchoolObjects.isEmpty()) {
            dndViewModel.getMagicSchools()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.magicSchoolObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowMagicSchool(school: MagicSchool, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = school.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text(
                    text = school.desc, style = typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun SearchMonsters(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowMonster((hbfUiState.showThisObject) as Monster, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.monsterObjects.isEmpty()) {
            dndViewModel.getMonsters()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.monsterObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(65.dp).requiredWidth(250.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.image != null) {
//                            Log.d("MyTAG", "IMG: ${item.image}")
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data("https://www.dnd5eapi.co${item.image}")
                                    .crossfade(true)
                                    .listener(
                                        onError = { request, result ->
                                            // The request failed. 'result.throwable' contains the error.
                                            Log.e("ImageLoader", "Loading image failed for URL: ${request.data}", result.throwable)
                                        }
                                    )
                                    .build(),
                                error = painterResource(R.drawable.ic_broken_image),
                                placeholder = painterResource(R.drawable.loading_img),
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )
                        }
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowMonster(monster: Monster, hbfVM: HBFViewModel, modifier : Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = monster.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text("${monster.size} ${monster.type}${if(monster.subtype != null) " " + monster.subtype else ""} CR ${monster.challenge_rating}",
                    style = typography.bodyMedium)

                Text(monster.alignment, style = typography.bodyMedium)

                Text("${monster.hit_points}hp (${monster.hit_points_roll}) AC ${monster.armor_class[0].value}", style = typography.bodyMedium)

                Text("Speed: ${if(monster.speed.walk != null) "walk ${monster.speed.walk}" else ""}" +
                        (if(monster.speed.swim != null) "swim ${monster.speed.swim}" else "") +
                        (if(monster.speed.fly != null) "fly ${monster.speed.fly}" else "") +
                        (if(monster.speed.burrow != null) "burrow ${monster.speed.burrow}" else "") +
                        (if(monster.speed.climb != null) "climb ${monster.speed.climb}" else "") +
                        if(monster.speed.hover != null) "hover ${monster.speed.hover}" else "",
                    style = typography.bodyMedium, textAlign = TextAlign.Center
                )

                Text("Proficiency Bonus: +${monster.proficiency_bonus}", style = typography.bodyMedium)

                Spacer(modifier.height(10.dp))

                Text("STR: ${monster.strength} DEX: ${monster.dexterity}", style = typography.bodyMedium)
                Text("CON: ${monster.constitution} INT: ${monster.intelligence}", style = typography.bodyMedium)
                Text("WIS: ${monster.wisdom} CHA: ${monster.constitution}", style = typography.bodyMedium)

                Spacer(modifier.height(10.dp))

                Text("Languages: ${if (monster.languages != "") monster.languages else "none"}", style = typography.bodyMedium, textAlign = TextAlign.Center)

                if (!monster.proficiencies.isEmpty()) {

                    var expandProficiencies by remember { mutableStateOf(false) }

                    TextButton(
                        onClick = {
                            expandProficiencies = !expandProficiencies
                        },
                    ) {
                        Row() {
                            Text(text = "Proficiencies:")
                            Icon(
                                painter = painterResource(if (expandProficiencies) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                contentDescription = null,
                                modifier = modifier.size(20.dp)
                            )
                        }
                    }

                    if (expandProficiencies) {
                        for (p in monster.proficiencies) {
                            Text(
                                text = "${p.proficiency.name} +${p.value}",
                                style = typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = modifier.padding(horizontal = 25.dp)
                            )
                        }
                    }
                }

                if (!monster.damage_vulnerabilities.isEmpty()) {
                    Text("Vulnerabilities:")
                    for (p in monster.damage_vulnerabilities) {
                        Text(
                            text = p, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding(horizontal = 25.dp)
                        )
                    }
                    Spacer(modifier.height(10.dp))
                }

                if (!monster.damage_resistances.isEmpty()) {
                    Text("Resistances:")
                    for (p in monster.damage_resistances) {
                        Text(
                            text = p, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding(horizontal = 25.dp)
                        )
                    }

                    Spacer(modifier.height(10.dp))
                }

                if (!monster.damage_immunities.isEmpty()) {
                    Text("Immunities:")
                    for (p in monster.damage_immunities) {
                        Text(
                            text = p, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding(horizontal = 25.dp)
                        )
                    }
                    for (p in monster.condition_immunities) {
                        Text(
                            text = p.name, style = typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = modifier.padding(horizontal = 25.dp)
                        )
                    }
                    Spacer(modifier.height(10.dp))
                }

                Text("Senses:")
                for (k in monster.senses.keys) {
                    Text(
                        text = "$k ${monster.senses[k]}", style = typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = modifier.padding( horizontal = 25.dp)
                    )
                }

                if (monster.desc != null) {
                    Text(
                        text = monster.desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                var expandAbilities by remember { mutableStateOf(false) }

                TextButton(
                    onClick = {
                        expandAbilities = !expandAbilities
                    },
                ) {
                    Row() {
                        Text(text = "Abilities:")
                        Icon(
                            painter = painterResource(if (expandAbilities) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandAbilities) {
                    for (ability in monster.special_abilities) {
                        var expand by remember { mutableStateOf(false) }

                        TextButton(
                            onClick = {
                                expand = !expand
                            },
                        ) {
                            Row() {
                                Text(text = ability.name)
                                Icon(
                                    painter = painterResource(if (expand) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                    contentDescription = null,
                                    modifier = modifier.size(20.dp)
                                )
                            }
                        }

                        if (expand) {
//                            if (ability.dc != null) {
//                                Text("DC: ${ability.dc}")
//                            }
//
//
//
//                            if(ability.usage != null) {
//                                var usage = ""
//
//                                if (ability.usage.times != null) usage = usage + ability.usage.times
//                                if (ability.usage.dice != null) usage = usage + ability.usage.dice
//                                usage = usage + ability.usage.type
//                                if (ability.usage.rest_types !== null && !ability.usage.rest_types.isEmpty()) {
//                                    usage = usage + ", regained on" + ability.usage.rest_types.joinToString()
//                                }
//                                if (ability.usage.min_value != null) usage = usage + ", min value " + ability.usage.min_value
//
//                                Text(usage, style = typography.bodyMedium, textAlign = TextAlign.Center)
//                            }
//
//                            if (ability.actions != null) {
//                                Text(ability.actions.name, style = typography.bodyMedium)
//                            }
//
//                            if (ability.spellcasting != null) {
//                                Text("Spells:")
//                                for (spell in ability.spellcasting.spells) {
//                                    Text("Level ${spell.level} ${spell.name}")
//                                }
//                            }

                            if (ability.desc != null) {
                                Text(ability.desc, style = typography.bodyMedium)
                            }
                        }
                    }
                }

                Spacer(modifier.height(10.dp))

                var expandActions by remember { mutableStateOf(false) }

                TextButton(
                    onClick = {
                        expandActions = !expandActions
                    },
                ) {
                    Row() {
                        Text(text = "Actions:")
                        Icon(
                            painter = painterResource(if (expandActions) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandActions) {
                    // actions
                    if (!monster.actions.isEmpty()) {
                        ShowActions(monster.actions)
                    }
                    // reactions
                    if (!monster.reactions.isEmpty()) {
                        Text("Reactions:")
                        ShowActions(monster.reactions)
                    }
                    // legendary actions
                    if (!monster.legendary_actions.isEmpty()) {
                        Text("Legendary Actions:")
                        ShowActions(monster.legendary_actions)
                    }
                }
            }
        }

    }
}

@Composable
fun ShowActions(actions : List<Action>, modifier : Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        for (action in actions) {
            Text("${if (action.name != null) action.name else action.action_name}: ${action.desc}",
                style = typography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier.height(10.dp))
        }
    }
}

@Composable
fun SearchRaces(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowRace((hbfUiState.showThisObject) as Race, hbfVM, dndViewModel)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.raceObjects.isEmpty()) {
            dndViewModel.getRaces()
        }
        if(dndViewModel.subraceObjects.isEmpty()) {
            dndViewModel.getSubraces()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = dndViewModel.raceObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowRace(race: Race, hbfVM: HBFViewModel, dndViewModel : DnDViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = race.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text("Speed: ${race.speed} feet")

                for (ab in race.ability_bonuses) {
                    Text("${ab.ability_score.name} +${ab.bonus}", style = typography.bodyMedium)
                }

                if (race.ability_bonus_options != null) {
                    Text("Choose ${race.ability_bonus_options.choose} from:", style = typography.bodyMedium)

                    var options: String = ((race.ability_bonus_options.from.options)?.get(0) as Option.OptionObject).ability_score?.name ?: ""

                    for (option in race.ability_bonus_options.from.options?.subList(1,race.ability_bonus_options.from.options.size-1)!!) {
                        options = options + ", " + (option as Option.OptionObject).ability_score?.name
                    }

                    Text(options, style = typography.bodyMedium, textAlign = TextAlign.Center)
                }

                Text(
                    text = "Alignment: ${race.alignment}", style = typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Text(race.age, style = typography.bodyMedium)

                Text(race.size_description, style = typography.bodyMedium, textAlign = TextAlign.Center)

                Spacer(modifier.height(10.dp))

                var langs: String = race.languages[0].name

                for (lang in race.languages.subList(1,race.languages.size-1)) {
                    langs = langs + ", " + lang.name
                }

                Text("Languages: ${langs}", style = typography.bodyMedium, textAlign = TextAlign.Center)

                if (race.language_options != null) {
                    Text(race.language_desc, style = typography.bodyMedium)

                    var options: String = ((race.language_options.from.options)?.get(0) as Option.OptionObject).ability_score?.name ?: ""

                    for (option in race.language_options.from.options.subList(1,race.language_options.from.options.size-1)) {
                        options = options + ", " + (option as Option.OptionObject).ability_score?.name
                    }

                    Text(options, style = typography.bodyMedium, textAlign = TextAlign.Center)
                }

                Spacer(modifier.height(10.dp))

                Text("Traits:", style = typography.bodyMedium)

                var traits: String = race.traits.get(0).name

                for (option in race.traits.subList(1,race.traits.size-1)) {
                    traits = traits + ", " + option.name
                }

                Text(traits, style = typography.bodyMedium, textAlign = TextAlign.Center)

                Spacer(modifier.height(10.dp))

                if (race.subraces.size > 0) {
                    Text("Subraces:", style = typography.bodyMedium)

                    for (subraceRef in race.subraces) {
                        var subrace : Subrace? = null
                        for (sub in dndViewModel.subraceObjects) {
                            if (subraceRef.index.equals(sub.index)) {
                                subrace = sub
                                break
                            }
                        }

                        if (subrace != null) {
                            var expand by remember { mutableStateOf(false) }

                            TextButton(
                                onClick = {
                                    expand = !expand
                                },
                            ) {
                                Row() {
                                    Text(text = subrace.name)
                                    Icon(
                                        painter = painterResource(if (expand) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                        contentDescription = null,
                                        modifier = modifier.size(20.dp)
                                    )
                                }
                            }

                            if (expand) {
                                if(!subrace.ability_bonuses.isEmpty()) {
                                    for (score in subrace.ability_bonuses) {
                                        Text("+${score.bonus} ${score.ability_score.name}")
                                    }
                                }

                                Text(
                                    subrace.desc,
                                    style = typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )

                                if (!subrace.racial_traits.isEmpty()) {
                                    for (trait in subrace.racial_traits) {
                                        Text(trait.name, style = typography.bodyMedium)
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun SearchSpells(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowSpell((hbfUiState.showThisObject) as Spell, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.spellObjects.isEmpty()) {
            dndViewModel.getSpells()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.spellObjects) { item ->
            val classes = mutableListOf<String>()

            for (classRef in item.classes) {
                classes.add(classRef.name)
            }
            if (item.name.lowercase().contains(hbfUiState.current_filter) ||
                hbfUiState.current_filter.contains(item.level.toString()) ||
                classes.joinToString().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(65.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowSpell(spell: Spell, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = spell.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text("Level ${spell.level} ${spell.school.name}")
                Text("Range: ${spell.range}", style = typography.bodyMedium)
                Text("Components: ${spell.components.joinToString()}", style = typography.bodyMedium)
                Text("Duration: ${spell.duration}", style = typography.bodyMedium)
                Text("Casting Time: ${spell.casting_time}", style = typography.bodyMedium)
                if (spell.ritual) Text("Ritual", style = typography.bodyMedium)
                if (spell.concentration) Text("Concentration", style = typography.bodyMedium)

                Spacer(modifier.height(10.dp))

                for (desc in spell.desc) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                for (desc in spell.higher_level) {
                    Text(
                        text = desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier.height(10.dp))

                val classes = mutableListOf<String>()

                for (classRef in spell.classes) {
                    classes.add(classRef.name)
                }

                Text("Classes: ${classes.joinToString()}")

                val subClasses = mutableListOf<String>()

                for (classRef in spell.subclasses) {
                    subClasses.add(classRef.name)
                }

                if (!subClasses.isEmpty()) Text("Subclasses: ${subClasses.joinToString()}")
            }
        }

    }
}

@Composable
fun SearchSubclasses(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowSubclass((hbfUiState.showThisObject) as Subclass, dndViewModel, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.subclassObjects.isEmpty()) {
            dndViewModel.getSubclasses()
        }
        if(dndViewModel.featureObjects.isEmpty()) {
            dndViewModel.getFeatures()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.subclassObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(50.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowSubclass(subclass : Subclass, dndViewModel: DnDViewModel, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")
        dndViewModel.getSubclassLevels(subclass.index)
    }
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = subclass.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text("${subclass.req_class.name} Subclass")
                Text(subclass.subclass_flavor, style = typography.bodyMedium)

                Spacer(modifier.height(10.dp))

                for (desc in subclass.desc) {
                    Text(desc, style = typography.bodyMedium, textAlign = TextAlign.Center)
                }

                if (!subclass.spells.isEmpty()) {
                    var expandSpells by remember { mutableStateOf(false) }

                    TextButton(
                        onClick = {
                            expandSpells = !expandSpells
                        },
                    ) {
                        Row() {
                            Text(text = "Spells:")
                            Icon(
                                painter = painterResource(if (expandSpells) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                                contentDescription = null,
                                modifier = modifier.size(20.dp)
                            )
                        }
                    }

                    if (expandSpells) {
                        for (spell in subclass.spells) {
                            Text("${spell.prerequisites[0].name} - ${spell.spell.name}",
                                style = typography.bodyMedium, textAlign = TextAlign.Center)
                        }
                    }

                    for (level in dndViewModel.subclassLevelObjects) {
                        ShowSubclassLevel(level, dndViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowSubclassLevel(level : SubclassLevel, dndViewModel: DnDViewModel, modifier : Modifier = Modifier) {
    var expand by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        if(dndViewModel.featureObjects.isEmpty()) {
            dndViewModel.getFeatures()
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 15.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {
                expand = !expand
            },
        ) {
            Row() {
                Text("Level ${level.level}", style = typography.bodyLarge,
                    textDecoration = TextDecoration.Underline)
                Icon(
                    painter = painterResource(if (expand) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                    contentDescription = null,
                    modifier = modifier.size(20.dp)
                )
            }
        }

        if (expand) {

            if (level.subclass_specific != null) {
                Text("Aura Range: ${level.subclass_specific.aura_range}")
            }

            // get the indexes of the features specific to this level
            val featureList : MutableList<Feature> = mutableListOf()
            for (subclassfeature in level.features) {
                for (feature in dndViewModel.featureObjects) {
                    if (subclassfeature.index == feature.index) {
                        featureList.add(feature)
                    }
                }
            }

            Log.d("MyTAG", featureList.size.toString())

            for (feature in featureList) {

                var expandFeature by remember { mutableStateOf(false) }

                TextButton(
                    onClick = {
                        expandFeature = !expandFeature
                    },
                ) {
                    Row() {
                        Text(
                            feature.name, style = typography.bodyLarge, color = Color.Black, textAlign = TextAlign.Center
                        )
                        Icon(
                            painter = painterResource(if (expandFeature) R.drawable.arrow_drop_up else R.drawable.arrow_drop_down),
                            contentDescription = null,
                            modifier = modifier.size(20.dp)
                        )
                    }
                }

                if (expandFeature) {
                    for (desc in feature.desc) {
                        Text(desc, style = typography.bodyMedium, textAlign = TextAlign.Center)
                        Spacer(modifier.height(3.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTraits(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    if(hbfUiState.showThisObject != null) {
        ShowTrait((hbfUiState.showThisObject) as Trait, hbfVM)
    }

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.traitObjects.isEmpty()) {
            dndViewModel.getTraits()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.traitObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {
                Card(
                    modifier = modifier.height(65.dp).requiredWidth(181.dp)
                ) {
                    Row(
                        modifier = modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                hbfVM.setObjectToShow(item)
                            },
                            modifier.fillMaxSize()
                        ) {
                            Text(
                                item.name.uppercase(),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTrait(trait: Trait, hbfVM: HBFViewModel, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = hbfVM::onDialogDismiss
    ) {
        Card(
            modifier = modifier.width(300.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = trait.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                if (trait.trait_specific != null && trait.trait_specific.breath_weapon != null) {
                    Text(
                        text = trait.trait_specific.breath_weapon.desc, style = typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    for (desc in trait.desc) {
                        Text(
                            text = desc, style = typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun SearchWeaponProperties(hbfVM: HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    val hbfUiState : HBFUiState = hbfVM.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.weaponPropertyObjects.isEmpty()) {
            dndViewModel.getWeaponProperties()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.weaponPropertyObjects) { item ->
            if (item.name.lowercase().contains(hbfUiState.current_filter)) {

                Card(

                ) {
                    Text(item.name)
                    // TODO: ADD SPECIFIC DATA LAYOUT

                }
            }
        }
    }
}
