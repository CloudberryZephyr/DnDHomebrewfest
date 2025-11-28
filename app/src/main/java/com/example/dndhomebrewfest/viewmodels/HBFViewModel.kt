package com.example.dndhomebrewfest.viewmodels

import androidx.lifecycle.ViewModel
import com.example.dndhomebrewfest.HBFUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MarioRaceViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(HBFUiState())

    val uiState: StateFlow<HBFUiState> = _uiState.asStateFlow()
}