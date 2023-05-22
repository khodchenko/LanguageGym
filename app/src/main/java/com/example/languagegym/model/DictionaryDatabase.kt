package com.example.languagegym.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordModel::class], version = 1)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}