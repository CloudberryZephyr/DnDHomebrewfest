package com.example.dndhomebrewfest.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Character::class],
    version = 4
)

abstract class Database : RoomDatabase() {
    abstract fun characterDao() : CharacterDao
}