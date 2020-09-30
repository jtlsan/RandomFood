package com.mountain96.random

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_foods,  R.id.navigation_combination, R.id.navigation_settings))
        navView.setupWithNavController(navController)
        resetFoodCheckbox()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun resetFoodCheckbox() {
        var db: AppDatabase? = AppDatabase.getInstance(this)
        var foodList: List<Food> = db!!.foodDao().getALL()
        for(food in foodList) {
            if (food.isChecked!!) {
                food.isChecked = false
                db!!.foodDao().updateFood(food)
            }
        }
    }
}