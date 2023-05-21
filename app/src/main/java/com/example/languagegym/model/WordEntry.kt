package com.example.languagegym.model

import android.provider.BaseColumns
import androidx.room.PrimaryKey

object WordEntry : BaseColumns {
    @PrimaryKey(autoGenerate = true)
    const val TABLE_NAME = "word"
    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_WORD = "word"
    const val COLUMN_NAME_TRANSLATION = "translation"
    const val COLUMN_NAME_PART_OF_SPEECH = "part_of_speech"
    const val COLUMN_NAME_GENDER = "gender"
    const val COLUMN_NAME_DECLENSION = "declension"
    const val COLUMN_NAME_SYNONYMS = "synonyms"
    const val COLUMN_NAME_IMAGE_URL = "image_url"
    const val COLUMN_NAME_LEARNING_PROGRESS = "learning_progress"
}