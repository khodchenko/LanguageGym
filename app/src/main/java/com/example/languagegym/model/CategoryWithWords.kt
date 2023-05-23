package com.example.languagegym.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithWords(
    @Embedded val category: CategoryModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val words: List<WordModel>
)