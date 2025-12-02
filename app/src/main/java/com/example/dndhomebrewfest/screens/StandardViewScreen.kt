package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dndhomebrewfest.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel

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
            "Ability Scores" -> searchAbilityScores(dndViewModel)
            "Alignments" -> searchAlignments(dndViewModel)
            "Backgrounds" -> searchBackgrounds(dndViewModel)
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
fun searchAbilityScores( dndViewModel : DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.abilityScoreObjects.isEmpty()) {
            dndViewModel.getAbilityScores()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.abilityScoreObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchAlignments( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        Log.d("MyTAG", "In launched effect")

        if(dndViewModel.alignmentObjects.isEmpty()) {
            dndViewModel.getAlignments()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = dndViewModel.alignmentObjects) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}

@Composable
fun searchBackgrounds( dndViewModel: DnDViewModel, modifier: Modifier = Modifier) {
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

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

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
