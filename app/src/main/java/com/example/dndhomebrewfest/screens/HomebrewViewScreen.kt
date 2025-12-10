package com.example.dndhomebrewfest.screens

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun HomebrewViewScreen(
    createHomebrew: () -> Unit,
    navRight: () -> Unit,
    navLeft: () -> Unit,
    modifier: Modifier = Modifier
){
    var offset by remember { mutableFloatStateOf(0f) }


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
        }
    ) {

    }
}