package com.example.dndhomebrewfest.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Upsert
    suspend fun upsertCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)

    @Query("SELECT * FROM character")
    fun getAllCharacters() : Flow<List<Character>>
}