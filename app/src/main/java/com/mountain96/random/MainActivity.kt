package com.mountain96.random

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mountain96.random.mainInterface.NavViewLocker
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.pedro.library.AutoPermissions
import com.pedro.library.AutoPermissionsListener

class MainActivity : AppCompatActivity(), NavViewLocker {
    private val PERMISSION_CODE = 151
    var navView : BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_foods,  R.id.navigation_combination, R.id.navigation_settings))
        navView!!.setupWithNavController(navController)
        resetFoodCheckbox()
        resetFoodCategory()
        
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
        }

    }

    override fun setNavViewEnabled(enabled: Boolean) {
        val menu1 = navView!!.menu.findItem(R.id.navigation_combination)
        val menu2 = navView!!.menu.findItem(R.id.navigation_favorite)
        val menu3 = navView!!.menu.findItem(R.id.navigation_foods)
        val menu4 = navView!!.menu.findItem(R.id.navigation_settings)
        if (enabled) {
            menu1.isEnabled = true
            menu2.isEnabled = true
            menu3.isEnabled = true
            menu4.isEnabled = true
        } else {
            menu1.isEnabled = false
            menu2.isEnabled = false
            menu3.isEnabled = false
            menu4.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun resetFoodCheckbox() {
        val db: AppDatabase? = AppDatabase.getInstance(this)
        val foodList: List<Food> = db!!.foodDao().getALL()
        for(food in foodList) {
            if (food.isChecked) {
                food.isChecked = false
                db.foodDao().updateFood(food)
            }
        }
    }

    fun resetFoodCategory() {
        val db: AppDatabase? = AppDatabase.getInstance(this)
        val categoryList = db!!.foodCategoryDao().getAll()
        for (category in categoryList) {
            if (category.foodCategoryId == 1) {
                category.isChecked = true
                db.foodCategoryDao().updateCategory(category)
                continue
            }
            if (category.isChecked) {
                category.isChecked = false
                db.foodCategoryDao().updateCategory(category)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder(this).apply {
                    setTitle("접근 권한 없음")
                    setMessage("카메라 권한이 없을 시 앱을 사용하실 수 없습니다.")

                    setOnCancelListener {
                        moveTaskToBack(true)
                        finish()
                        android.os.Process.killProcess(android.os.Process.myPid())
                    }
                    setPositiveButton("확인", DialogInterface.OnClickListener {dialogInterface, i ->
                        moveTaskToBack(true)
                        finish()
                        android.os.Process.killProcess(android.os.Process.myPid())
                    })
                    show()
                }
            }
        }
    }
}