package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.room.TypeConverter
import com.example.dndhomebrewfest.viewmodels.RoomVM
import kotlinx.serialization.json.Json

@Composable
fun CharacterViewScreen(
    createCharacter: () -> Unit,
    navRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offset by remember { mutableFloatStateOf(0f) }

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
        val roomVM = RoomVM.getInstance()
        Button(
            onClick = {
                Log.d("MYTAG", "Character 1: ${roomVM.getCharacter(1)?.value.toString()}")
                createCharacter()
            }
        ) {
            Text("Create Character")
        }
        LazyColumn() {
            items(items = roomVM.characters.value) { item ->
                Card(
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(item.name)
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