package com.example.languagegym.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<WordModel> {
        val listType = object : TypeToken<List<WordModel>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<WordModel>): String {
        return gson.toJson(list)
    }
}