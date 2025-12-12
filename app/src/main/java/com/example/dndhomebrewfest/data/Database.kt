package com.example.dndhomebrewfest.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Character::class,
               Homebrew::class],
    version = 5
)

abstract class Database : RoomDatabase() {
    abstract fun characterDao() : CharacterDao
    abstract fun homebrewDao() : HomebrewDao
}