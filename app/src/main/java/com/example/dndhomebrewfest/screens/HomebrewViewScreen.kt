package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dndhomebrewfest.data.Character
import com.example.dndhomebrewfest.data.Homebrew
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import com.example.dndhomebrewfest.viewmodels.RoomVM

@Composable
fun HomebrewViewScreen(
    createHomebrew: () -> Unit,
    hbfVM: HBFViewModel,
    navRight: () -> Unit,
    navLeft: () -> Unit,
    modifier: Modifier = Modifier
){
    var offset by remember { mutableFloatStateOf(0f) }
    val roomVM = RoomVM.getInstance()
    var selectedHomebrewId by remember { mutableStateOf<Int?>(null) }
    val homebrewList by roomVM.homebrews.collectAsState()

    if (selectedHomebrewId != null) {
        var thisBrew : Homebrew = homebrewList[0]
        for(h in homebrewList){
            if(h.homebrewId == selectedHomebrewId){
                thisBrew = h
            }
        }
        ShowHomebrew(
            homebrew = thisBrew,
            hbfVM = hbfVM,
            roomVM = roomVM,
            modifier = Modifier,
            onDismiss = { selectedHomebrewId = null }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
        .pointerInput(Unit) {
            detectHorizontalDragGestures (
                onDragStart = {
                    Log.d("MyTAG", "drag detected")
                    offset = 0f },
                onDragEnd = {
                    if (offset < 0) navRight() else navLeft()
                }
            ) { change, dragAmount ->
                offset += dragAmount
                change.consume()
            }
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                createHomebrew()
            }
        ) {
            Text("Create Homebrew")
        }
        LazyColumn(
            modifier = modifier.defaultMinSize(minHeight = 400.dp),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(items = homebrewList) { item ->
                Card(
                    modifier = modifier
                        .clickable{ selectedHomebrewId = item.homebrewId },
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
fun ShowHomebrew(homebrew: Homebrew, hbfVM : HBFViewModel, roomVM: RoomVM, modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = modifier.width(350.dp)
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = homebrew.name, style = typography.titleLarge, textDecoration = TextDecoration.Underline)

                Spacer(modifier = modifier.height(10.dp))

                Text(text = homebrew.category, style = typography.bodyMedium)
                Spacer(modifier = modifier.height(7.dp))
                Text(text = homebrew.description, style = typography.bodySmall)
            }
        }
    }
}