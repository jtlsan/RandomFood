package com.mountain96.random.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") var name : String? = null,
    @ColumnInfo(name = "category")var category : FoodCategory? = null,
    @ColumnInfo(name = "imageUrl")var image  : String? = null,
    @ColumnInfo(name = "isChecked")var isChecked : Boolean? = null,
    @ColumnInfo(name = "isFavorite")var isFavorite : Boolean? = null)