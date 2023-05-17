package com.example.languagegym.data

import android.provider.BaseColumns

object WordEntry : BaseColumns {
    const val TABLE_NAME = "word"
    const val _ID = "word_id" //
    const val COLUMN_NAME_WORD = "word"
    const val COLUMN_NAME_TRANSLATION = "translation"
    const val COLUMN_NAME_PART_OF_SPEECH = "part_of_speech"
    const val COLUMN_NAME_GENDER = "gender"
    const val COLUMN_NAME_DECLENSION = "declension"
    const val COLUMN_NAME_SYNONYMS = "synonyms"
    const val COLUMN_NAME_IMAGE_URL = "image_url"
    const val COLUMN_NAME_LEARNING_PROGRESS = "learning_progress"
}