package com.mountain96.random.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "food",
    foreignKeys = [
        ForeignKey(
            entity = FoodCategory::class,
            parentColumns = ["foodCategoryId"],
            childColumns = ["categoryId"]
        )
    ]
)
data class Food (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "categoryId")var categoryId : Int,
    @ColumnInfo(name = "imageUrl")var image  : String,
    @ColumnInfo(name = "isChecked")var isChecked : Boolean = false,
    @ColumnInfo(name = "isFavorite")var isFavorite : Boolean = false)
