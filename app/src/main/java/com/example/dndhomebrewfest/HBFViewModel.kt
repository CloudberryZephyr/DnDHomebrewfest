package com.example.dndhomebrewfest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HBFViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(HBFUiState())

    val uiState: StateFlow<HBFUiState> = _uiState.asStateFlow()
}