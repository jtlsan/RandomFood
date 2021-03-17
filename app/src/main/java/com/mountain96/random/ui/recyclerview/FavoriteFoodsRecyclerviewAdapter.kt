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

class FavoriteFoodsRecyclerviewAdapter(db: AppDatabase, resources: Resources, activity: FragmentActivity) :
    FoodsRecyclerView(db, resources, activity) {
    override lateinit var foodList : ArrayList<Food>
    override var isRemoveStatus : Boolean = false

    override fun loadFoodList() {
        foodList = arrayListOf()
        val savedFoods = db.foodDao().getAllFavorites()
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
        val savedFoods = db.foodDao().getAllFavorites()
        foodList.addAll(savedFoods)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val view = holder.itemView
        val food = foodList.get(position)

        view.icon_favorite_mark.setOnClickListener {
            food.isFavorite = false

            foodList.set(position, food)
            db.foodDao().updateFood(food)
            foodList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}