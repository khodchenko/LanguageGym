package com.example.languagegym.model

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

    @Transaction
    @Query("SELECT * FROM category")
    suspend fun getCategoriesWithWords(): List<CategoryWithWords>
}