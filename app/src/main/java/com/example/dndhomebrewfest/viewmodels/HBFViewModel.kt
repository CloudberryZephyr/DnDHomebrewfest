package com.example.dndhomebrewfest.viewmodels

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.isEmpty
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dndhomebrewfest.HBFUiState
import com.example.dndhomebrewfest.screens.viewClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HBFViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(HBFUiState())

    val uiState: StateFlow<HBFUiState> = _uiState.asStateFlow()

    fun onCurrentFilterChanged(current_filter : String) {
        _uiState.update { currentState ->
            currentState.copy(
                current_filter = current_filter
            )
        }
    }

    fun setType(current_type : String) {
        _uiState.update { currentState ->
            currentState.copy(
                current_type = current_type
            )
        }
    }


}