package com.mountain96.random.model

import androidx.room.Embedded
import androidx.room.Relation

data class FoodCategoryWithFood (
    @Embedded val category: FoodCategory,
    @Relation(
        parentColumn = "foodCategoryId",
        entity = Food::class,
        entityColumn = "categoryId"
    )
    val foodList: List<Food>
)
