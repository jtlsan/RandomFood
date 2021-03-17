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
import com.mountain96.random.model.ModelType
import com.mountain96.random.ui.foods.InitSettings
import com.mountain96.random.ui.foods.dialog.FoodDialog
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food_add.view.*

class FoodsRecyclerviewAdapter(db: AppDatabase, resources: Resources, activity: FragmentActivity) :
    FoodsRecyclerView(db, resources, activity) {
    lateinit var foodDialog: FoodDialog
    override lateinit var foodList : ArrayList<Food>
    override var isRemoveStatus : Boolean = false


    override fun loadFoodList() {
        val savedFoods = db.foodDao().getALL()
        foodList = arrayListOf()
        if (savedFoods.isNotEmpty()) {
            foodList.addAll(savedFoods)
        } else {
            InitSettings.initFoods(db, foodList)
        }
    }

    fun setDialog(foodDialog: FoodDialog) {
        this.foodDialog = foodDialog
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        val result: Int
        when(foodList.get(position).type) {
            ModelType.TYPE_ADD_BUTTON -> result = 0
            ModelType.TYPE_ITEM -> result = 1
        }
        return result
    }

    override fun selectByCategory(newList: List<Food>) {
        val button = foodList.get(0)
        foodList.clear()
        foodList.add(button)
        foodList.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun selectAll() {
        foodList.clear()
        val savedFoods = db.foodDao().getALL()
        foodList.addAll(savedFoods)
        this.notifyDataSetChanged()
    }

    fun addFood(name: String, position: Int, imageUrl: String) {
        var food: Food
        db.foodCategoryDao().getAll().let {
            food = Food(0, name, it.get(position).foodCategoryId, imageUrl, false, false, ModelType.TYPE_ITEM, true)
        }
        db.foodDao().insertAll(food)
        foodList.add(db.foodDao().getALL().last())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder : RecyclerView.ViewHolder? = null
        when(viewType) {
            0 -> viewHolder = FoodAddViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food_add, parent, false))
            1 -> viewHolder = FoodViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false))
        }
        return viewHolder!!
    }

    inner class FoodAddViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class FoodViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView
        val food = foodList.get(position)

        if (food.type == ModelType.TYPE_ADD_BUTTON) {
            view.button_add_food.setOnClickListener {
                foodDialog.showFoodAddDialog()
            }
            return
        }
        super.onBindViewHolder(holder, position)
    }
}