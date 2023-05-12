package com.example.languagegym.data

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DictionaryDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertWord(word: WordModel): Long {
        val db = writableDatabase

        val values = word.toContentValues()

        return db.insert(WordEntry.TABLE_NAME, null, values)
    }

    fun updateWord(wordModel: WordModel): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(WordEntry.COLUMN_NAME_WORD, wordModel.word)
            put(WordEntry.COLUMN_NAME_TRANSLATION, wordModel.translation)
            put(WordEntry.COLUMN_NAME_PART_OF_SPEECH, wordModel.partOfSpeech)
            put(WordEntry.COLUMN_NAME_GENDER, wordModel.gender)
            put(WordEntry.COLUMN_NAME_SYNONYMS, wordModel.synonyms.joinToString(","))
        }
        val rowsAffected = db.update(WordEntry.TABLE_NAME, values, "${BaseColumns._ID} = ?", arrayOf(wordModel.id.toString()))

        Log.d(TAG, "Rows affected: $rowsAffected")

        return rowsAffected
    }

    fun deleteWord(word: WordModel): Int {
        val db = writableDatabase

        val selection = "${WordEntry._ID} = ?"
        val selectionArgs = arrayOf(word.id.toString())

        return db.delete(
            WordEntry.TABLE_NAME,
            selection,
            selectionArgs
        )
    }

    fun getAllWords(): List<WordModel> {
        val db = readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            WordEntry.COLUMN_NAME_WORD,
            WordEntry.COLUMN_NAME_TRANSLATION,
            WordEntry.COLUMN_NAME_PART_OF_SPEECH,
            WordEntry.COLUMN_NAME_GENDER,
            WordEntry.COLUMN_NAME_DECLENSION,
            WordEntry.COLUMN_NAME_SYNONYMS,
            WordEntry.COLUMN_NAME_IMAGE_URL,
            WordEntry.COLUMN_NAME_LEARNING_PROGRESS
        )

        val sortOrder = "${WordEntry.COLUMN_NAME_WORD} ASC"

        val cursor = db.query(
            WordEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val words = mutableListOf<WordModel>()

        with(cursor) {
            while (moveToNext()) {
                words.add(WordModel(cursor))
            }
        }

        return words
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Dictionary.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${WordEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${WordEntry.COLUMN_NAME_WORD} TEXT," +
                    "${WordEntry.COLUMN_NAME_TRANSLATION} TEXT," +
                    "${WordEntry.COLUMN_NAME_PART_OF_SPEECH} TEXT," +
                    "${WordEntry.COLUMN_NAME_GENDER} TEXT," +
                    "${WordEntry.COLUMN_NAME_DECLENSION} TEXT," +
                    "${WordEntry.COLUMN_NAME_SYNONYMS} TEXT," +
                    "${WordEntry.COLUMN_NAME_IMAGE_URL} TEXT," +
                    "${WordEntry.COLUMN_NAME_LEARNING_PROGRESS} INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${WordEntry.TABLE_NAME}"
    }
}
