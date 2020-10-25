package com.mountain96.random.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Food::class, FoodCategory::class), version = 9)
@TypeConverters(ModelTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDAO
    abstract fun foodCategoryDao() : FoodCategoryDAO
    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-food"
                ).allowMainThreadQueries().addMigrations(MIGRATION_8_9).build()
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

        val MIGRATION_2_3 = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("CREATE TABLE FoodCategory ("+
                        "foodcategoryId INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL)")

                database.execSQL("DROP TABLE food")
                database.execSQL("CREATE TABLE food (" +
                        "uid INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "categoryId INTEGER NOT NULL, " +
                        "imageUrl TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "isFavorite INTEGER NOT NULL)")
            }
        }

        val MIGRATION_3_4 = object: Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE foodcategory")
                database.execSQL("CREATE TABLE foodcategory ("+
                        "foodCategoryId INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL)")
            }
        }
        val MIGRATION_4_5 = object: Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE food")
                database.execSQL("CREATE TABLE food (" +
                        "uid INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "categoryId INTEGER NOT NULL, " +
                        "imageUrl TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "isFavorite INTEGER NOT NULL," +
                        "FOREIGN KEY (categoryId) REFERENCES foodcategory (foodCategoryId))")
            }
        }

        val MIGRATION_5_6 = object: Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE food")
                database.execSQL("CREATE TABLE food (" +
                        "uid INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "categoryId INTEGER NOT NULL, " +
                        "imageUrl TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "isFavorite INTEGER NOT NULL, " +
                        "type INTEGER NOT NULL, " +
                        "FOREIGN KEY (categoryId) REFERENCES foodcategory (foodCategoryId))")
                database.execSQL("DROP TABLE foodcategory")
                database.execSQL("CREATE TABLE foodcategory ("+
                        "foodCategoryId INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "type INTEGER NOT NULL)")
            }
        }

        val MIGRATION_6_7 = object: Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE food")
                database.execSQL("CREATE TABLE food (" +
                        "uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "categoryId INTEGER NOT NULL, " +
                        "imageUrl TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "isFavorite INTEGER NOT NULL, " +
                        "type INTEGER NOT NULL, " +
                        "createdByUser INTEGER NOT NULL, " +
                        "FOREIGN KEY (categoryId) REFERENCES foodcategory (foodCategoryId) ON DELETE CASCADE)")

                database.execSQL("DROP TABLE foodcategory")
                database.execSQL("CREATE TABLE foodcategory ("+
                        "foodCategoryId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "type INTEGER NOT NULL)")
            }
        }

        val MIGRATION_7_8 = object: Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE foodcategory")
                database.execSQL("CREATE TABLE foodcategory ("+
                        "foodCategoryId INTEGER PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "type INTEGER NOT NULL)")
            }
        }

        val MIGRATION_8_9 = object: Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE foodcategory")
                database.execSQL("CREATE TABLE foodcategory ("+
                        "foodCategoryId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "isChecked INTEGER NOT NULL, " +
                        "type INTEGER NOT NULL)")
            }
        }
    }
}