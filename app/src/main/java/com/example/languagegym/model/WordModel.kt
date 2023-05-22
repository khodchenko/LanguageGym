package com.example.languagegym.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "word")
data class WordModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String = "",
    val translation: String = "",
    @ColumnInfo(name = "part_of_speech")
    val partOfSpeech: String = "",
    val gender: String = "",
//    val declension: List<String> = emptyList(),
//    val synonyms: List<String> = emptyList(),
    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",
    @ColumnInfo(name = "learning_progress")
    val learningProgress: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
//        parcel.createStringArrayList() ?: emptyList(),
//        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(word)
        parcel.writeString(translation)
        parcel.writeString(partOfSpeech)
        parcel.writeString(gender)
//        parcel.writeStringList(declension)
//        parcel.writeStringList(synonyms)
        parcel.writeString(imageUrl)
        parcel.writeInt(learningProgress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordModel> {
        override fun createFromParcel(parcel: Parcel): WordModel {
            return WordModel(parcel)
        }

        override fun newArray(size: Int): Array<WordModel?> {
            return arrayOfNulls(size)
        }
    }
}
