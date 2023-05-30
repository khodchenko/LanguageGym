package com.example.languagegym.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,
    val name: String,
    val categoryColor: Int
)