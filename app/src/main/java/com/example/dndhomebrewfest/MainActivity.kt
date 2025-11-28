package com.example.dndhomebrewfest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.RoomVM
import com.example.dndhomebrewfest.data.Character

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DnDHomebrewfestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Homebrewery()
                }
            }
        }
    }
}

@Composable
fun Homebrewery(modifier: Modifier = Modifier) {
    val APIVM : DnDViewModel = viewModel()

    val roomVM = RoomVM.getInstance()

    val characters = roomVM.characters.collectAsState().value

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(characters) {character ->
            Text("${character.character_id}")
            dbCard(character)
        }
    }
}

@Composable
fun dbCard(character : Character, modifier: Modifier = Modifier) {

    Card(modifier = modifier.fillMaxWidth()) {
        Text(character.name)
        Text(character.character_class)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DnDHomebrewfestTheme {
        Homebrewery()
    }
}

