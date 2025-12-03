package com.example.dndhomebrewfest.viewmodels

import androidx.lifecycle.ViewModel
import com.example.dndhomebrewfest.HBFUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HBFViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(HBFUiState())

    val uiState: StateFlow<HBFUiState> = _uiState.asStateFlow()

    init{
        resetVM()
    }

    fun resetVM() {
        _uiState.update { currentState ->
            currentState.copy(
                current_filter = "",
                current_type = "",
                showThisObject = null
            )
        }
    }

    fun onCurrentFilterChanged(current_filter : String) {
        _uiState.update { currentState ->
            currentState.copy(
                current_filter = current_filter.lowercase()
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

    fun setObjectToShow(dndObject : Any) {
        _uiState.update { currentState ->
            currentState.copy(
                showThisObject = dndObject
            )
        }
    }

    fun onDialogDismiss() {
        _uiState.update { currentState ->
            currentState.copy(
                showThisObject = null
            )
        }
    }
}