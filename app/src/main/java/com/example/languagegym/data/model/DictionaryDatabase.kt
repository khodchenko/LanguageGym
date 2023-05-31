package com.example.languagegym.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WordModel::class, CategoryModel::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun categoryDao(): CategoryDao
    companion object {
        @Volatile
        private var instance: DictionaryDatabase? = null

        fun getInstance(context: Context): DictionaryDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DictionaryDatabase::class.java,
                    "dictionary_database"
                ).build().also { instance = it }
            }
        }
    }
}