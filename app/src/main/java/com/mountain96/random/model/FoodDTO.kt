package com.mountain96.random.model

data class FoodDTO (var name : String? = null,
                    var category : FoodCategory? = null,
                    var image  : String? = null,
                    var isChecked : Boolean? = null,
                    var isFavorite : Boolean? = null)