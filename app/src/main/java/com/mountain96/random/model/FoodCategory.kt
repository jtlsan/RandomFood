package com.mountain96.random.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foodcategory")
data class FoodCategory (
    @PrimaryKey(autoGenerate = true) val foodCategoryId: Int,
    @ColumnInfo(name = "name", collate = ColumnInfo.RTRIM) var name: String,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean,
    @ColumnInfo(name = "type") var type : ModelType
)
