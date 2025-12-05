package com.example.dndhomebrewfest.screens

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dndhomebrewfest.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dndhomebrewfest.HBFUiState
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.AbilityScore
import com.example.dndhomebrewfest.viewmodels.AlignmentDnD
import com.example.dndhomebrewfest.viewmodels.Background
import com.example.dndhomebrewfest.viewmodels.BackgroundFeature
import com.example.dndhomebrewfest.viewmodels.Choice
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.Equipments
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
            "Alignments" -> searchAlignments(hbfVM, dndViewModel)
            "Backgrounds" -> searchBackgrounds(hbfVM,dndViewModel)
            "Classes" -> searchClasses( dndViewModel)
            "Conditions" -> searchConditions(dndViewModel)
            "Damage Types" -> searchDamageTypes(dndViewModel)
            "Equipment" -> searchEquipment(dndViewModel)
            "Equipment Categories" -> searchEquipmentObjects(dndViewModel)
            "Feats" -> searchFeats(dndViewModel)
            "Features" -> searchFeatures(dndViewModel)
            "Languages" -> searchLanguages(dndViewModel)
            "Magic Items" -> searchMagicItems(dndViewModel)
            "Magic Schools" -> searchMagicSchool(dndViewModel)
            "Monsters" -> searchMonsters(dndViewModel)
            "Proficiencies" -> searchProficiencies(dndViewModel)
            "Races" -> searchRaces(dndViewModel)
            "Rule Sections" -> searchRuleSections(dndViewModel)
            "Rules" -> searchRules(dndViewModel)
            "Skills" -> searchSkills(dndViewModel)
            "Spells" -> searchSpells(dndViewModel)
            "Subclasses" -> searchSubclasses(dndViewModel)
            "Subraces" -> searchSubraces(dndViewModel)
            "Traits" -> searchTraits(dndViewModel)
            else -> searchWeaponProperties(dndViewModel)
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
fun searchAlignments(hbfVM : HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
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
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.alignmentDnDObjects) { item ->
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
                        Text(item.name.uppercase() ,
                            style = typography.bodyLarge,
                            textAlign = TextAlign.Center)
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
fun searchBackgrounds(hbfVM : HBFViewModel, dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
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
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.backgroundObjects) { item ->
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
                        Text(item.name.uppercase() ,
                            style = typography.bodyLarge,
                            textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowBackground(background: Background, hbfVM : HBFViewModel, modifier: Modifier = Modifier) {
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
                Text(text = background.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

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

                for (starting_option in background.starting_equipment_options) {
                    Text("Choose ${starting_option.choose} ${starting_option.from.equipment_category?.name}")
                }

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Feature: ${background.feature.name}")
                for (p in background.feature.desc) {
                    Text(text = p, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                }

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Choose ${background.personality_traits.choose} traits:")
                for (option in background.personality_traits.from.options!!) {
                    Text(text = (option as Option.OptionObject).string!!, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = modifier.height(7.dp))
                }

                Spacer(modifier = modifier.height(10.dp))

                Text(text = "Choose ${background.ideals.choose} ideal:")
                for (option in background.ideals.from.options!!) {
                    Text(text = (option as Option.OptionObject).desc!!, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)

                    var alignments = ""
                    for (i in range(0, option.alignments!!.size - 1)) {
                        alignments = alignments + option.alignments[i].name + ", "
                    }
                    alignments += option.alignments[option.alignments.size-1].name

                    Text(text = alignments, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = modifier.height(7.dp))
                }

                Spacer(modifier = modifier.height(3.dp))

                Text(text = "Choose ${background.bonds.choose} bond:")
                for (option in background.bonds.from.options!!) {
                    Text(text = (option as Option.OptionObject).string!!, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = modifier.height(7.dp))
                }

                Spacer(modifier = modifier.height(3.dp))

                Text(text = "Choose ${background.flaws.choose} flaw:")
                for (option in background.flaws.from.options!!) {
                    Text(text = (option as Option.OptionObject).string!!, style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = modifier.height(7.dp))
                }

            }
        }

    }
}

@Composable
fun searchClasses(dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.classObjects.isEmpty()) {
            dndViewModel.getClasses()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.classObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }

}

@Composable
fun searchConditions( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.conditionObjects.isEmpty()) {
            dndViewModel.getConditions()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.conditionObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchDamageTypes( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.damageTypeObjects.isEmpty()) {
            dndViewModel.getDamageTypes()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.damageTypeObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchEquipment( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.equipmentObjects.isEmpty()) {
            dndViewModel.getEquipment()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.equipmentObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchEquipmentObjects( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.equipmentCategoryObjects.isEmpty()) {
            dndViewModel.getEquipmentCategories()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.equipmentCategoryObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchFeats( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.featObjects.isEmpty()) {
            dndViewModel.getFeats()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.featObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchFeatures( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.featureObjects.isEmpty()) {
            dndViewModel.getFeatures()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.featureObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchLanguages( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.languageObjects.isEmpty()) {
            dndViewModel.getLanguages()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.languageObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchMagicItems( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.magicItemObjects.isEmpty()) {
            dndViewModel.getMagicItems()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.magicItemObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchMagicSchool( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.magicSchoolObjects.isEmpty()) {
            dndViewModel.getMagicSchools()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.magicSchoolObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchMonsters( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.monsterObjects.isEmpty()) {
            dndViewModel.getMonsters()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.monsterObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchProficiencies( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.proficiencyObjects.isEmpty()) {
            dndViewModel.getProficiencies()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.proficiencyObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchRaces( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.raceObjects.isEmpty()) {
            dndViewModel.getRaces()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.raceObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchRuleSections( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.ruleSectionObjects.isEmpty()) {
            dndViewModel.getRuleSections()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.ruleSectionObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchRules( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.ruleObjects.isEmpty()) {
            dndViewModel.getRules()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.ruleObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchSkills( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.skillObjects.isEmpty()) {
            dndViewModel.getSkills()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.skillObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchSpells( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.spellObjects.isEmpty()) {
            dndViewModel.getSpells()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.spellObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchSubclasses( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.subclassObjects.isEmpty()) {
            dndViewModel.getSubclasses()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.subclassObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchSubraces( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.subraceObjects.isEmpty()) {
            dndViewModel.getSubraces()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.subraceObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchTraits( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.traitObjects.isEmpty()) {
            dndViewModel.getTraits()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.traitObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchWeaponProperties( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
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
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowAbilityScorePreview() {
    DnDHomebrewfestTheme {
        ShowBackground(Background(
            index = "thing",
            name = "Background",
            starting_proficiencies = listOf(
                ObjectReference(
                    index = "ability",
                    name = "athetics",
                    url = "url"
                )
            ),
            language_options = Choice(
                choose = 1,
                type = "language",
                from = OptionsSet(
                    option_set_type = "resource_list",
                    resource_list_url = "resource list url"
                )
            ),
            starting_equipment = listOf(
                Equipments(
                    equipment = ObjectReference(
                        index = "ind",
                        name = "clothes",
                        url = "equipment url"
                    ),
                    quantity = 2
                )
            ),
            starting_equipment_options = listOf(
                Choice(
                    choose = 1,
                    type = "equipment",
                    from = OptionsSet(
                        option_set_type = "equipment category",
                        equipment_category = ObjectReference(
                            index = "thing",
                            name = "holy symbols",
                            url = "url"
                        )
                    )
                )
            ),
            feature = BackgroundFeature(
                name = "Background Feature",
                desc = listOf("feature details aljshdkahs ahsdkjha sjhda ajsdh  hdsh hd s")
            ),
            personality_traits = Choice(
                choose = 2,
                type = "traits",
                from = OptionsSet(
                    option_set_type = "options array",
                    options = listOf(
                        Option.OptionObject(
                            option_type = "string",
                            string = "akjs s asdh hsjdah aksjhd jshdhhakshkdiwjsn"
                        )
                    )
                )
            ),
            ideals = Choice(
                choose = 1,
                type = "ideals",
                from = OptionsSet(
                    option_set_type = "options array",
                    options = listOf(
                        Option.OptionObject(
                            option_type = "ideal",
                            desc = "tradition",
                            alignments = listOf(
                                ObjectReference(
                                    index = "good",
                                    name = "neutral good",
                                    url = "url"
                                )
                            )
                        )
                    )
                )
            ),
            bonds = Choice(
                choose = 2,
                type = "bonds",
                from = OptionsSet(
                    option_set_type = "options array",
                    options = listOf(
                        Option.OptionObject(
                            option_type = "string",
                            string = "akjs s asdh hsjdah aksjhd jshdhhakshkdiwjsn"
                        )
                    )
                )
            ),
            flaws = Choice(
                choose = 2,
                type = "flaws",
                from = OptionsSet(
                    option_set_type = "options array",
                    options = listOf(
                        Option.OptionObject(
                            option_type = "string",
                            string = "akjs s asdh hsjdah aksjhd jshdhhakshkdiwjsn"
                        )
                    )
                )
            ),
            url = "background url",
            updated_at = "now",
        ),
            hbfVM = viewModel())
    }
}