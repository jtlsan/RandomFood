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
import kotlinx.android.synthetic.main.item_category.view.*

open class OriginCategoryRecyclerviewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit open var db : AppDatabase
    lateinit open var activity: FragmentActivity
    lateinit open var resources: Resources
    lateinit open var adapter: OriginFoodsRecyclerviewAdapter
    open var isRemoveStatus = false
    open var categoryList : ArrayList<FoodCategory> = arrayListOf()

    constructor(db: AppDatabase, resources: Resources, activity: FragmentActivity, adapter: OriginFoodsRecyclerviewAdapter): this() {
        this.db = db
        this.resources = resources
        this.activity = activity
        this.adapter = adapter

        val savedFoods = db!!.foodCategoryDao().getAll()
        categoryList.addAll(savedFoods)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder = CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
        return viewHolder!!
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var itemview = holder.itemView
        var category = categoryList.get(position)

        itemview.category_button.text = category.name
        itemview.icon_remove_category.visibility= View.INVISIBLE

        if (category.isChecked) {
            itemview.category_button.background = resources.getDrawable(R.drawable.button_category_filled)
            itemview.category_button.setTextColor(resources.getColor(R.color.colorCategorySelected))
            setFoodsByCategory(category)
        }
        else {
            itemview.category_button.background = resources.getDrawable(R.drawable.button_category)
            itemview.category_button.setTextColor(resources.getColor(R.color.colorCategoryUnselected))
        }


        itemview.category_button.setOnClickListener {
            if (isRemoveStatus) {
                isRemoveStatus = false
                notifyDataSetChanged()
                return@setOnClickListener
            }
            if (category.isChecked) {
                selectCategoryAll()
                adapter.selectAll()
            } else {
                for(category in categoryList) {
                    category.isChecked = false
                }
                category.isChecked = true

                setFoodsByCategory(category)
                categoryList.set(position, category)
            }
            this.notifyDataSetChanged()

            var categoryDao = db!!.foodCategoryDao()
            for(category in categoryList) {
                categoryDao.updateCategory(category)
            }
        }

        itemview.category_button.setOnLongClickListener {
            itemview.icon_remove_category.visibility = View.VISIBLE
            it.isEnabled = false
            isRemoveStatus = true
            return@setOnLongClickListener true
        }

        itemview.icon_remove_category.setOnClickListener {
            categoryList.removeAt(position)
            db!!.foodCategoryDao().delete(category)
            notifyDataSetChanged()
        }
    }

    fun selectCategoryAll() {
        for(category in categoryList) {
            category.isChecked = false
        }
        for(category in categoryList) {
            if (category.name.equals(resources.getString(R.string.checkbox_all))) {
                category.isChecked = true
                break
            }
        }
    }

    fun setFoodsByCategory(category: FoodCategory) {
        val foodCategoryId = category.foodCategoryId
        if (category.name.equals(resources.getString(R.string.checkbox_all))) {
            category.isChecked = true
            adapter.selectAll()
            return
        }
        val foodCategoryWithFood = db.foodCategoryDao().getFoodCategoryWithFood()
        var selectedCategoryWithFood : FoodCategoryWithFood? = null
        for (item in foodCategoryWithFood) {
            if (item.category.foodCategoryId == foodCategoryId) {
                selectedCategoryWithFood = item
                break
            }
        }
        adapter.selectByCategory(selectedCategoryWithFood?.foodList!!)
    }
}