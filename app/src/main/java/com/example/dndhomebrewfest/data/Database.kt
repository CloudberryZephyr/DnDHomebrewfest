package com.example.dndhomebrewfest.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Character::class],
    version = 1
)

abstract class Database : RoomDatabase() {
    abstract val characterDao : CharacterDao
}