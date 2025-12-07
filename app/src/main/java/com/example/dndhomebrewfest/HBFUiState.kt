package com.example.dndhomebrewfest

data class HBFUiState(
    var current_filter : String = "",
    var current_type : String = "",
    var showThisObject : Any? = null,
    var current_equip_cat : String = ""
)