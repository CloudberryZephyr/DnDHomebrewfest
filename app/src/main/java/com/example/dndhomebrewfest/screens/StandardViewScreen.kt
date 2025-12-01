package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dndhomebrewfest.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.dndhomebrewfest.Homebrewery
import com.example.dndhomebrewfest.data.DataSource
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.Class
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import kotlinx.coroutines.runBlocking

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
    if (dndViewModel.abilityScoreObjects.isEmpty()) { dndViewModel.getAbilityScores() }
    val list = dndViewModel.abilityScoreObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.alignmentObjects.isEmpty()) { dndViewModel.getAlignments() }
    val list = dndViewModel.alignmentObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.backgroundObjects.isEmpty()) dndViewModel.getBackgrounds()
    val list = dndViewModel.backgroundObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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

        Log.d("MyTAG", "Leaving")


    }

    Log.d("MyTAG", "Out of if")

    viewClass(dndViewModel.classObjects)

}

@Composable
fun viewClass(list : List<Class>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.conditionObjects.isEmpty()) dndViewModel.getConditions()
    val list = dndViewModel.conditionObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.damageTypeObjects.isEmpty()) dndViewModel.getDamageTypes()
    val list = dndViewModel.damageTypeObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.equipmentObjects.isEmpty()) dndViewModel.getEquipment()
    val list = dndViewModel.equipmentObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.equipmentCategoryObjects.isEmpty()) dndViewModel.getEquipmentCategories()
    val list = dndViewModel.equipmentCategoryObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.featObjects.isEmpty()) dndViewModel.getFeats()
    val list = dndViewModel.featObjects
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.featureObjects.isEmpty()) dndViewModel.getFeatures()
    val list = dndViewModel.featureObjects
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.languageObjects.isEmpty()) dndViewModel.getLanguages()
    val list = dndViewModel.languageObjects
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.magicItemObjects.isEmpty()) dndViewModel.getMagicItems()
    val list = dndViewModel.magicItemObjects
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.magicSchoolObjects.isEmpty()) dndViewModel.getMagicSchools()
    val list = dndViewModel.magicSchoolObjects
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.monsterObjects.isEmpty()) dndViewModel.getMonsters()
    val list = dndViewModel.monsterObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.proficiencyObjects.isEmpty()) dndViewModel.getProficiencies()
    val list = dndViewModel.proficiencyObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.raceObjects.isEmpty()) dndViewModel.getRaces()
    val list = dndViewModel.raceObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.ruleSectionObjects.isEmpty()) dndViewModel.getRuleSections()
    val list = dndViewModel.ruleSectionObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.ruleObjects.isEmpty()) dndViewModel.getRules()
    val list = dndViewModel.ruleObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.skillObjects.isEmpty()) dndViewModel.getSkills()
    val list = dndViewModel.skillObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.spellObjects.isEmpty()) dndViewModel.getSpells()
    val list = dndViewModel.spellObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.subclassObjects.isEmpty()) dndViewModel.getSubclasses()
    val list = dndViewModel.subclassObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.subraceObjects.isEmpty()) dndViewModel.getSubraces()
    val list = dndViewModel.subraceObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.traitObjects.isEmpty()) dndViewModel.getTraits()
    val list = dndViewModel.traitObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
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
    if (dndViewModel.weaponPropertyObjects.isEmpty()) dndViewModel.getWeaponProperties()
    val list = dndViewModel.weaponPropertyObjects

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)

    ) {
        items(items = list) { item ->
            Card(

            ) {
                Text(item.name)
                // TODO: ADD SPECIFIC DATA LAYOUT

            }
        }
    }
}
