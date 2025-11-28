package com.example.dndhomebrewfest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// starter character class for db development
@Entity(tableName = "character")
data class Character(
    @PrimaryKey
    var character_id : Int = 1,
    var name : String = "",
    var character_class : String = ""
)