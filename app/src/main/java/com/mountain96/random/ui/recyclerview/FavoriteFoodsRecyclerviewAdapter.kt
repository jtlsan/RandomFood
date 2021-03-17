package com.mountain96.random.ui.recyclerview

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.ui.foods.dialog.FoodDialog
import kotlinx.android.synthetic.main.item_food.view.*

class FavoriteFoodsRecyclerviewAdapter : FoodsRecyclerView {
    //lateinit override var db : AppDatabase
    lateinit var foodDialog: FoodDialog
    //lateinit override var activity: FragmentActivity
    //lateinit override var resources: Resources
    override lateinit var foodList : ArrayList<Food>
    override var isRemoveStatus : Boolean = false

    constructor(db: AppDatabase, resources: Resources, activity: FragmentActivity): super(db, resources, activity)

    override fun loadFoodList() {
        foodList = arrayListOf()
        val savedFoods = db!!.foodDao().getAllFavorites()
        foodList.addAll(savedFoods)
    }

    override fun selectByCategory(newList: List<Food>) {
        foodList.clear()
        for(food in newList) {
            if (food.isFavorite)
                foodList.add(food)
        }
        this.notifyDataSetChanged()
    }

    override fun selectAll() {
        foodList.clear()
        val savedFoods = db!!.foodDao().getAllFavorites()
        foodList.addAll(savedFoods)
        this.notifyDataSetChanged()
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        var view = holder.itemView
        var food = foodList.get(position)

        view.icon_favorite_mark.setOnClickListener {
            food.isFavorite = false

            foodList.set(position, food)
            db!!.foodDao().updateFood(food)
            foodList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}