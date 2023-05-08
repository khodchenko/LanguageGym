package com.example.languagegym.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler


@Parcelize
data class Word(
    var word: String,
    var translation: String,
    var partOfSpeech: String,
    var gender: String,
    var declension: List<String>,
    var synonyms: List<String>,
    val imageUrl: String?,
    val learningProgress: Int
): Parcelable {
    companion object : Parceler<Word> {

        override fun Word.write(dest: Parcel, flags: Int) {
            dest.writeString(word)
            dest.writeString(translation)
            dest.writeString(partOfSpeech)
            dest.writeString(gender)
            dest.writeStringList(declension)
            dest.writeStringList(synonyms)
            dest.writeString(imageUrl)
            dest.writeInt(learningProgress)
        }

        override fun create(parcel: Parcel): Word {
            return Word(parcel)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.readString(),
        parcel.readInt()
    )

}