package com.example.languagegym.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface CategoryDao {
    @Insert
    fun insertCategory(category: CategoryModel): Long

    @Update
    fun updateCategory(category: CategoryModel): Int

    @Delete
    fun deleteCategory(category: CategoryModel): Int

    @Query("SELECT * FROM category ORDER BY name ASC")
    fun getAllCategories(): List<CategoryModel>

    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoriesWithWords(): List<CategoryWithWords>

    @Transaction
    @Query("SELECT * FROM word")
    fun getAllWordsFromCategories(): List<WordModel>

    @Transaction
    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun getCategoryWithWords(categoryId: Int): CategoryWithWords

}