package com.example.languagegym.model

data class CategoryModel(
    val name: String,
    val categoryColor: Int,
    val words: List<WordModel>
)