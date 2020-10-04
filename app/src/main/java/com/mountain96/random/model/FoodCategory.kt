package com.mountain96.random.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foodcategory")
data class FoodCategory (
    @PrimaryKey val foodCategoryId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean
)
