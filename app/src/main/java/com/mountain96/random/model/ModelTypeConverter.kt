package com.mountain96.random.model

import androidx.room.TypeConverter

class ModelTypeConverter {
    @TypeConverter
    fun fromModelType(value: ModelType): Int {
        var result : Int
        when(value) {
            ModelType.TYPE_ADD_BUTTON -> result = 0
            ModelType.TYPE_ITEM -> result = 1
        }
        return result
    }

    @TypeConverter
    fun toModelType(value: Int): ModelType? {
        var result : ModelType? = null
        when(value) {
            0 -> result = ModelType.TYPE_ADD_BUTTON
            1 -> result = ModelType.TYPE_ITEM
        }
        return result
    }
}