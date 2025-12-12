package com.example.dndhomebrewfest.screens

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.room.TypeConverter
import com.example.dndhomebrewfest.HBFUiState
import com.example.dndhomebrewfest.data.Character
import com.example.dndhomebrewfest.viewmodels.AbilityScore
import com.example.dndhomebrewfest.viewmodels.Condition
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import com.example.dndhomebrewfest.viewmodels.RoomVM
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream

@Composable
fun CharacterViewScreen(
    createCharacter: () -> Unit,
    hbfVM: HBFViewModel,
    navRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offset by remember { mutableFloatStateOf(0f) }
    var selectedCharacterId by remember { mutableStateOf<Int?>(null) }
    val roomVM = RoomVM.getInstance()
    val characterList by roomVM.characters.collectAsState()

    if (selectedCharacterId != null) {
        var thisChar : Character = characterList[0]
        for(c in characterList){
            if(c.character_id == selectedCharacterId){
                thisChar = c
            }
        }
        ShowCharacter(
            character = thisChar,
            hbfVM = hbfVM,
            roomVM = roomVM,
            modifier = Modifier,
            onDismiss = { selectedCharacterId = null }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        Log.d("MyTAG", "drag detected")
                        offset = 0f
                    },
                    onDragEnd = {
                        if (offset < 0) navRight()
                    }
                ) { change, dragAmount ->
                    offset += dragAmount
                    change.consume()
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.i("MYTAG", characterList.toString())
        Button(
            onClick = {
                Log.d("MYTAG", "Character 1: ${roomVM.getCharacter(1)?.value.toString()}")
                createCharacter()
            }
        ) {
            Text("Create Character")
        }
        LazyColumn(
            modifier = modifier.defaultMinSize(minHeight = 400.dp),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(items = characterList) { item ->
                Card(
                    modifier = modifier
                        .clickable{ selectedCharacterId = item.character_id },
                ) {
                    Text(item.name,
                        modifier = modifier.fillMaxSize(),
                        style = typography.titleMedium,
                        textAlign = TextAlign.Center)
                }
                Spacer(modifier = modifier.padding(5.dp))
            }
        }
    }
}

@Composable
fun ShowCharacter(character: Character, hbfVM : HBFViewModel, roomVM: RoomVM, modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val thisChar = character
    Log.i("MYTAG", thisChar.toString())
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = modifier.width(350.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                if (character.char_img_uri != "") {
                    val directory = LocalContext.current.filesDir
                    val file = File(directory, character.char_img_uri)
                    val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                    val bitmapPainter = BitmapPainter(bitmap.asImageBitmap())

                    Image(
                        painter = bitmapPainter,
                        contentDescription = null,
                        modifier = modifier.size(200.dp)
                    )

                    Spacer(modifier.height(10.dp))
                }
                Text(text = thisChar.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Log.i("MYTAG", thisChar.character_stats_json)
                val stats: Map<String, Int> = toStringIntMap(thisChar.character_stats_json)
                Text(text = "Stats",
                    style = typography.titleMedium,
                    textAlign = TextAlign.Center)
                Text(text = "Strength: ${stats["Strength"]}, Mod: ${CalcMod(stats["Strength"]?:10)}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                Spacer(modifier = modifier.height(2.dp))
                Text(text = "Dexterity: ${stats["Dexterity"]}, Mod: ${CalcMod(stats["Dexterity"]?:10)}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                Spacer(modifier = modifier.height(2.dp))
                Text(text = "Constitution: ${stats["Constitution"]}, Mod: ${CalcMod(stats["Constitution"]?:10)}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                Spacer(modifier = modifier.height(2.dp))
                Text(text = "Intelligence: ${stats["Intelligence"]}, Mod: ${CalcMod(stats["Intelligence"]?:10)}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                Spacer(modifier = modifier.height(2.dp))
                Text(text = "Wisdom: ${stats["Wisdom"]}, Mod: ${CalcMod(stats["Wisdom"]?:10)}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                Spacer(modifier = modifier.height(2.dp))
                Text(text = "Charisma: ${stats["Charisma"]}, Mod: ${CalcMod(stats["Charisma"]?:10)}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)
                Spacer(modifier = modifier.height(10.dp))


                val saves = toStringList(thisChar.saving_throws_json)
                val saveMap : MutableMap<String, Int> = mutableMapOf()

                if(saves.contains("STR")){
                    saveMap["STR"] = CalcMod(stats["Strength"] ?: 10) + 2
                }
                if(saves.contains("DEX")){
                    saveMap["DEX"] = CalcMod(stats["Dexterity"] ?: 10) + 2
                }
                if(saves.contains("CON")){
                    saveMap["CON"] = CalcMod(stats["Constitution"] ?: 10) + 2
                }
                if(saves.contains("INT")){
                    saveMap["INT"] = CalcMod(stats["Intelligence"] ?: 10) + 2
                }
                if(saves.contains("WIS")){
                    saveMap["WIS"] = CalcMod(stats["Wisdom"] ?: 10) + 2
                }
                if(saves.contains("CHA")){
                    saveMap["CHA"] = CalcMod(stats["Charisma"] ?: 10) + 2
                }

                Text(text = "Saving Throws",
                    style = typography.titleMedium,
                    textAlign = TextAlign.Center)
                Text(text = "${saves[0]}: ${saveMap[saves[0]]}; ${saves[1]}: ${saveMap[saves[1]]}",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)

                val skills = toStringList(thisChar.skills_json)
                Text(text = "Skills",
                    style = typography.titleMedium,
                    textAlign = TextAlign.Center)
                Text(text = skills.toString(),
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)

                val equipment = toStringList(thisChar.equipment_json)
                Text(text = "Equipment",
                    style = typography.titleMedium,
                    textAlign = TextAlign.Center)
                Text(text = equipment.toString(),
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center)

                val features = toStringStringMap(thisChar.features_json)
                Text(text = "Features",
                    style = typography.titleMedium,
                    textAlign = TextAlign.Center)
                for(f in features){
                    Text(text = f.key,
                        style = typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Text(text = f.value,
                        style = typography.bodySmall,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = modifier.height(2.dp))
                }

            }
        }
    }
}



@TypeConverter
fun toStringList(value: String): List<String> {
    return Json.decodeFromString(value)
}

@TypeConverter
fun toStringStringMap(value: String): Map<String, String> {
    return Json.decodeFromString(value)
}

@TypeConverter
fun toStringIntMap(value: String) : Map<String, Int> {
    return Json.decodeFromString(value)
}

fun CalcMod(stat: Int) : Int{
    return when(stat){
        3 -> -4
        4, 5 -> -3
        6, 7 -> -2
        8, 9 -> -1
        10, 11 -> 0
        12, 13 -> 1
        14, 15 -> 2
        16, 17 -> 3
        18, 19 -> 4
        else -> 5
    }
}