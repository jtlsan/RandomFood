package com.mountain96.random.model

import androidx.room.*

@Dao
interface FoodCategoryDAO {
    @Query("SELECT * FROM foodcategory")
    fun getAll() : List<FoodCategory>

    @Query("SELECT * FROM food WHERE categoryId LIKE :foodCategoryId")
    fun findFoodByCategoryId(foodCategoryId: Int) : Food

    @Update
    fun updateCategory(vararg category: FoodCategory)

    @Transaction
    @Query("SELECT * FROM foodcategory")
    fun getFoodCategoryWithFood() : List<FoodCategoryWithFood>

    @Insert
    fun insertAll(vararg category: FoodCategory)

    @Delete
    fun delete(category: FoodCategory)


}