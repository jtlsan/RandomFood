package com.mountain96.random.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDAO {
    @Query("SELECT * FROM food")
    fun getALL(): List<Food>

    @Query("SELECT * FROM food WHERE uid IN (:foodIds)")
    fun loadAllByIds(foodIds: IntArray): List<Food>

    @Query("SELECT * FROM food WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Food

    @Insert
    fun insertAll(vararg foods: Food)

    @Delete
    fun delete(food: Food)
}