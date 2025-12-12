package com.example.dndhomebrewfest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dndhomebrewfest.data.Homebrew
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import com.example.dndhomebrewfest.viewmodels.RoomVM

@Composable
fun HomebrewCreationScreen(
    hbfVM: HBFViewModel,
    finish: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nextId by remember{mutableStateOf<Int>(0)}
    val roomVM = RoomVM.getInstance()

    var name by remember{mutableStateOf<String>("")}
    var category by remember{mutableStateOf<String>("")}
    var description by remember{mutableStateOf<String>("")}


    val homebrewList by roomVM.homebrews.collectAsState()
    for(hb in homebrewList){
        if(hb.homebrewId >= nextId){
            nextId = hb.homebrewId + 1
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("Enter your homebrew's name")},
            singleLine = false
        )
        Spacer(modifier.padding(10.dp))
        TextField(
            value = category,
            onValueChange = {category = it},
            label = {Text("Enter your homebrew's category")},
            singleLine = false
        )
        Spacer(modifier.padding(10.dp))
        TextField(
            value = description,
            onValueChange = {description = it},
            label = {Text("Describe your homebrew")},
            singleLine = false
        )
        Spacer(modifier.padding(10.dp))
        Button(
            onClick = {
                roomVM.addHomebrew(
                    Homebrew(
                        homebrewId = nextId,
                        name = name,
                        category = category,
                        description = description
                    )
                )
                finish()
            },
            enabled = (name != "" && category != "" && description != "")
        ) {
            Text("Brew your brew!")
        }
    }
}