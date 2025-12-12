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

    @Query("DELETE FROM character") // "characters" should be your table name
    suspend fun deleteAllCharacters()

    @Query("SELECT * FROM character")
    fun getAllCharacters() : Flow<List<Character>>

    @Query("UPDATE character SET char_img_uri = :imageUri WHERE character_id = :charId")
    suspend fun updateImageUri(charId: Int, imageUri: String)

    @Query("SELECT * FROM character WHERE character_id = :charID")
    fun getCharacter(charID : Int) : Flow<Character>
}

@Dao
interface HomebrewDao {
    @Upsert
    suspend fun upsertHomebrew(homebrew: Homebrew)

    @Query("SELECT * FROM homebrew")
    fun getAllHomebrews() : Flow<List<Homebrew>>

    @Query("SELECT * FROM homebrew WHERE homebrewId = :brewID")
    fun getHomebrew(brewID : Int) : Flow<Homebrew>
}