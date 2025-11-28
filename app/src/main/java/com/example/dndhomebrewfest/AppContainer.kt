package com.example.dndhomebrewfest

import android.content.Context
import androidx.room.Room
import com.example.dndhomebrewfest.data.Database
import com.example.dndhomebrewfest.data.CharacterDao

interface AppContainer {
    val characterDao : CharacterDao
}

class DefaultAppContainer(val context: Context) : AppContainer{

    // we create the database in this DefaultAppContainer
    private val db by lazy {
        Room.databaseBuilder(
            context,
            Database::class.java,
            "database.db"
        ).fallbackToDestructiveMigration(true) // drop old tables when the schema is updated
            .build()
    }

    override val characterDao: CharacterDao by lazy{
        db.characterDao
    }

}