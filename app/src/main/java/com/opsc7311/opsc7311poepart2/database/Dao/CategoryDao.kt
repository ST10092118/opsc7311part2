package com.opsc7311.opsc7311poepart2.database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.opsc7311.opsc7311poepart2.database.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    fun insertCategory(category: Category)

    @Query("SELECT * FROM Category WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): Category?

    @Query("SELECT * FROM Category WHERE userId = :userId")
    fun getCategoriesForUser(userId: Int): Flow<List<Category>>
}