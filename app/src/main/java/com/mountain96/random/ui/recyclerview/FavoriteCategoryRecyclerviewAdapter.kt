package com.mountain96.random.ui.recyclerview

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.model.FoodCategoryWithFood
import com.mountain96.random.model.ModelType
import com.mountain96.random.ui.foods.InitSettings
import kotlinx.android.synthetic.main.item_category.view.*

class FavoriteCategoryRecyclerviewAdapter() : OriginCategoryRecyclerviewAdapter() {
    lateinit override var db : AppDatabase
    lateinit override var activity: FragmentActivity
    lateinit override var resources: Resources
    lateinit override var adapter: OriginFoodsRecyclerviewAdapter
    override var isRemoveStatus = false
    override var categoryList : ArrayList<FoodCategory> = arrayListOf()

    constructor(db: AppDatabase, resources: Resources, activity: FragmentActivity, adapter: OriginFoodsRecyclerviewAdapter): this() {
        this.db = db
        this.resources = resources
        this.activity = activity
        this.adapter = adapter

        val savedCategory = db!!.foodCategoryDao().getAll()
        if (savedCategory.isNotEmpty()) {
            categoryList.addAll(savedCategory)
        } else {
            InitSettings.initCategory(db!!, categoryList)
        }
        //remove add button
        categoryList.removeAt(0)
        selectCategoryAll()
    }
}