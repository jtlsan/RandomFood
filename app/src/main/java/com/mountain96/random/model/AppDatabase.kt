package com.mountain96.random.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Food::class), version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDAO
    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-food"
                ).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build()
            }
            return instance
        }

        val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("DROP TABLE food")
                database.execSQL("CREATE TABLE food (" +
                        "uid INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "category INTEGER NOT NULL, " +
                        "imageUrl TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "isFavorite INTEGER NOT NULL)")
            }
        }
    }
}