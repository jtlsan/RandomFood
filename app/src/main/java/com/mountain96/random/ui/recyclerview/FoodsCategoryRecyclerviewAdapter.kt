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
import com.mountain96.random.model.ModelType
import com.mountain96.random.ui.foods.InitSettings
import com.mountain96.random.ui.foods.dialog.FoodDialog
import kotlinx.android.synthetic.main.item_category_add.view.*

class FoodsCategoryRecyclerviewAdapter() : OriginCategoryRecyclerviewAdapter() {
    lateinit override var db : AppDatabase
    lateinit override var activity: FragmentActivity
    lateinit override var resources: Resources
    lateinit override var adapter: FoodsRecyclerView
    lateinit var foodDialog: FoodDialog
    override var isRemoveStatus = false
    override var categoryList : ArrayList<FoodCategory> = arrayListOf()

    constructor(db: AppDatabase, resources: Resources, activity: FragmentActivity, adapter: FoodsRecyclerView): this() {
        this.db = db
        this.resources = resources
        this.activity = activity
        this.adapter = adapter

        val savedCategory = db!!.foodCategoryDao().getAll()
        categoryList.addAll(savedCategory)

        selectCategoryAll()
    }

    fun addCategory(name: String) {
        FoodCategory(0, name, false, ModelType.TYPE_ITEM).let{ category ->
            db!!.foodCategoryDao().insertAll(category)
            categoryList.add(db!!.foodCategoryDao().getAll().last())
        }
    }

    fun setDialog(foodDialog: FoodDialog) {
        this.foodDialog = foodDialog
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        var result : Int
        when(categoryList.get(position).type) {
            ModelType.TYPE_ADD_BUTTON -> result = 0
            ModelType.TYPE_ITEM -> result = 1
        }
        return result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder : RecyclerView.ViewHolder? = null
        when(viewType) {
            0 -> viewHolder = CategoryAddViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category_add, parent, false))
            1 -> viewHolder = CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
        }
        return viewHolder!!
    }

    inner class CategoryAddViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var itemview = holder.itemView
        var category = categoryList.get(position)

        if (category.type == ModelType.TYPE_ADD_BUTTON) {
            itemview.category_add_button.setOnClickListener {
                foodDialog!!.showCategoryAddDialog()
            }
            return
        }
        super.onBindViewHolder(holder, position)
    }
}