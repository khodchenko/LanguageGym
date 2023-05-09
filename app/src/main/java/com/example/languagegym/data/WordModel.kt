package com.example.languagegym.data

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns


class WordModel(
    var id: Long = -1,
    var word: String = "",
    var translation: String = "",
    var partOfSpeech: String = "",
    var gender: String = "",
    var declension: List<String> = emptyList(),
    var synonyms: List<String> = emptyList(),
    var imageUrl: String = "",
    var learningProgress: Int = 0
) {

    constructor(cursor: Cursor) : this(
        id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
        word = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_WORD)),
        translation = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_TRANSLATION)),
        partOfSpeech = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_PART_OF_SPEECH)),
        gender = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_GENDER)),
        declension = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_DECLENSION)).split(","),
        synonyms = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_SYNONYMS)).split(","),
        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_IMAGE_URL)),
        learningProgress = cursor.getInt(cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_LEARNING_PROGRESS))
    )

    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put(WordEntry.COLUMN_NAME_WORD, word)
            put(WordEntry.COLUMN_NAME_TRANSLATION, translation)
            put(WordEntry.COLUMN_NAME_PART_OF_SPEECH, partOfSpeech)
            put(WordEntry.COLUMN_NAME_GENDER, gender)
            put(WordEntry.COLUMN_NAME_DECLENSION, declension.joinToString(","))
            put(WordEntry.COLUMN_NAME_SYNONYMS, synonyms.joinToString(","))
            put(WordEntry.COLUMN_NAME_IMAGE_URL, imageUrl)
            put(WordEntry.COLUMN_NAME_LEARNING_PROGRESS, learningProgress)
        }
    }
}
