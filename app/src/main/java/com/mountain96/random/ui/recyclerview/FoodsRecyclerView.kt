package com.mountain96.random.ui.recyclerview

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.ui.foods.InitSettings
import kotlinx.android.synthetic.main.item_food.view.*

abstract class FoodsRecyclerView() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var db : AppDatabase
    lateinit var activity: FragmentActivity
    lateinit var resources: Resources
    abstract var isRemoveStatus : Boolean
    abstract var foodList : ArrayList<Food>
    lateinit var glide : FoodGlide

    constructor(db: AppDatabase, resources: Resources, activity: FragmentActivity): this() {
        this.db = db
        this.resources = resources
        this.activity = activity
        this.glide = FoodGlide(activity)

        loadFoodList()
    }

    protected abstract fun loadFoodList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView
        val food = foodList.get(position)
        view.icon_remove_food.visibility = View.GONE

        setViewClickListeners(view, food, position)
        drawView(view, food)
    }

    private fun setViewClickListeners(view: View, food : Food, position: Int) {
        view.linearlayout_select_area.setOnClickListener(View.OnClickListener {
            if (isRemoveStatus) {
                isRemoveStatus = false
                notifyDataSetChanged()
                return@OnClickListener
            }

            toggleCheckMark(view, food)
            foodList.set(position, food)
            db.foodDao().updateFood(food)
        })

        view.linearlayout_select_area.setOnLongClickListener {
            view.icon_remove_food.visibility = View.VISIBLE
            view.linearlayout_select_area.isEnabled = false
            isRemoveStatus = true
            return@setOnLongClickListener true
        }

        view.icon_favorite_mark.setOnClickListener {
            toggleFavoriteMark(view, food)
            foodList.set(position, food)
            db.foodDao().updateFood(food)
        }

        view.icon_remove_food.setOnClickListener {
            foodList.removeAt(position)
            db.foodDao().delete(food)
            notifyItemRemoved(position)
        }
    }

    private fun toggleCheckMark(view : View, food : Food) {
        if(view.checkbox_itemfood.isChecked) {
            food.isChecked = false
            view.checkbox_itemfood.isChecked = false
        } else {
            food.isChecked = true
            view.checkbox_itemfood.isChecked = true
        }
    }

    private fun toggleFavoriteMark(view: View, food: Food) {
        if (food.isFavorite) {
            food.isFavorite = false
            view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))
        } else {
            food.isFavorite = true
            view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
        }
    }

    private fun drawView(view : View, food : Food) {
        view.textview_foodname.text = food.name
        glide.loadImageTo(food.image, view.imageview_food)

        if (food.isFavorite)
            view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
        else
            view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))

        if (food.isChecked)
            view.checkbox_itemfood.isChecked = true
        else
            view.checkbox_itemfood.isChecked = false
    }

    abstract fun selectAll()
    abstract fun selectByCategory(newList: List<Food>)
}