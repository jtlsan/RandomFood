package com.mountain96.random.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "category")var category : FoodCategory,
    @ColumnInfo(name = "imageUrl")var image  : String,
    @ColumnInfo(name = "isChecked")var isChecked : Boolean = false,
    @ColumnInfo(name = "isFavorite")var isFavorite : Boolean = false)
