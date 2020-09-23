package com.mountain96.random.model

import androidx.room.*

@Dao
interface FoodDAO {
    @Query("SELECT * FROM food")
    fun getALL(): List<Food>

    @Query("SELECT * FROM food WHERE uid IN (:foodIds)")
    fun loadAllByIds(foodIds: IntArray): List<Food>

    @Query("SELECT * FROM food WHERE category LIKE :foodCategory")
    fun loadAllByCategory(foodCategory: FoodCategory): List<Food>

    @Query("SELECT * FROM food WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Food

    @Query("SELECT * FROM food WHERE isFavorite = 1")
    fun getAllFavorites(): List<Food>

    @Query("SELECT * FROM food WHERE category LIKE :foodCategory AND isFavorite = 1")
    fun loadAllFavoritesByCategory(foodCategory: FoodCategory): List<Food>

    @Update
    fun updateFood(vararg food:Food)

    @Insert
    fun insertAll(vararg foods: Food)

    @Delete
    fun delete(food: Food)
}