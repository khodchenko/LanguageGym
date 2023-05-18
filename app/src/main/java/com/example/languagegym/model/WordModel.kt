package com.example.languagegym.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable

class WordModel(
    var word: String = "",
    var translation: String = "",
    var partOfSpeech: String = "",
    var gender: String = "",
    var declension: List<String> = emptyList(),
    var synonyms: List<String> = emptyList(),
    var imageUrl: String = "",
    var learningProgress: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(word)
        parcel.writeString(translation)
        parcel.writeString(partOfSpeech)
        parcel.writeString(gender)
        parcel.writeStringList(declension)
        parcel.writeStringList(synonyms)
        parcel.writeString(imageUrl)
        parcel.writeInt(learningProgress)
    }

    companion object CREATOR : Parcelable.Creator<WordModel> {
        override fun createFromParcel(parcel: Parcel): WordModel {
            return WordModel(parcel)
        }

        override fun newArray(size: Int): Array<WordModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(cursor: Cursor) : this(
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
