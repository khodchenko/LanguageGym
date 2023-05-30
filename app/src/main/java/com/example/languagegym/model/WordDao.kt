package com.example.languagegym.model

import androidx.room.*

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWord(word: WordModel): Long

    @Update
    fun updateWord(word: WordModel): Int

    @Delete
    fun deleteWord(word: WordModel): Int

    @Query("SELECT * FROM word WHERE id = :id")
    suspend fun getWordById(id: Int): WordModel?

    @Query("SELECT * FROM word ORDER BY word ASC")
    fun getAllWords(): List<WordModel>

    @Query("SELECT * FROM word WHERE part_of_speech = :inputPartOfSpeech")
    fun getWordsByPartOfSpeech(inputPartOfSpeech: String): List<WordModel>
}