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
import kotlinx.android.synthetic.main.item_food.view.*

open class OriginFoodsRecyclerviewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit open var db : AppDatabase
    lateinit open var activity: FragmentActivity
    lateinit open var resources: Resources
    open var isRemoveStatus = false
    open var foodList : ArrayList<Food> = arrayListOf()
    lateinit open var glide : FoodGlide

    constructor(db: AppDatabase, resources: Resources, activity: FragmentActivity): this() {
        this.db = db
        this.resources = resources
        this.activity = activity
        this.glide = FoodGlide(activity)

        val savedFoods = db!!.foodDao().getALL()
        foodList.addAll(savedFoods)
    }

    open fun selectAll() {}
    open fun selectByCategory(newList: List<Food>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder.itemView
        var food = foodList.get(position)

        view.icon_remove_food.visibility = View.GONE

        view.linearlayout_select_area.setOnClickListener(View.OnClickListener {
            if (isRemoveStatus) {
                isRemoveStatus = false
                notifyDataSetChanged()
                return@OnClickListener
            }
            if(view.checkbox_itemfood.isChecked) {
                food.isChecked = false
                view.checkbox_itemfood.isChecked = false
            } else {
                food.isChecked = true
                view.checkbox_itemfood.isChecked = true
            }
            foodList.set(position, food)
            db!!.foodDao().updateFood(food)
        })

        view.icon_favorite_mark.setOnClickListener {
            if (food.isFavorite) {
                food.isFavorite = false
                view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))
            } else {
                food.isFavorite = true
                view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
            }
            foodList.set(position, food)
            db!!.foodDao().updateFood(food)
        }
        view.textview_foodname.text = foodList.get(position).name


        glide.loadImageTo(food.image, view.imageview_food)

        if (food.isFavorite)
            view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
        else
            view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))

        if (food.isChecked)
            view.checkbox_itemfood.isChecked = true
        else
            view.checkbox_itemfood.isChecked = false

        view.linearlayout_select_area.setOnLongClickListener {
            view.icon_remove_food.visibility = View.VISIBLE
            view.linearlayout_select_area.isEnabled = false
            isRemoveStatus = true
            return@setOnLongClickListener true
        }

        view.icon_remove_food.setOnClickListener {
            foodList.removeAt(position)
            db!!.foodDao().delete(food)
            notifyItemRemoved(position)
        }
    }
}