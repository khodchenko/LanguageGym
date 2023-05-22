package com.example.languagegym.ui.model

data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val categoryColor: Int,
    val words: List<WordModel>
)
