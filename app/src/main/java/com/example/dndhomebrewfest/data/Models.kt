package com.example.dndhomebrewfest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// starter character class for db development
@Entity(tableName = "character")
data class Character(
    @PrimaryKey
    var character_id : Int = 1,
    var name : String = "",
    var character_class : String = "",
    var char_img_uri : String = "",
    var character_stats_json : String = "",
    var character_race : String = "",
    var saving_throws_json : String = "",
    var skills_json : String = "",
    var equipment_json : String = "",
    var features_json : String = "",
    var spells_json : String = ""
)