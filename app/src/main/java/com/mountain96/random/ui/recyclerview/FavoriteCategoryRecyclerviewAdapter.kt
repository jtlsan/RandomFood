package com.mountain96.random.ui.recyclerview

import android.content.res.Resources
import androidx.fragment.app.FragmentActivity
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.ui.foods.InitSettings

class FavoriteCategoryRecyclerviewAdapter(db: AppDatabase, resources: Resources, activity: FragmentActivity, adapter: FoodsRecyclerView) :
    CategoryRecyclerview(db, resources, activity, adapter) {
    override var isRemoveStatus = false
    override lateinit var categoryList : ArrayList<FoodCategory>

    override fun loadCategoryList() {
        val savedCategory = db.foodCategoryDao().getAll()
        categoryList = arrayListOf()
        categoryList.addAll(savedCategory)
        categoryList.removeAt(0)
        selectCategoryAll()
    }
}