package com.mountain96.random.ui.foods

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mountain96.random.R
import com.mountain96.random.model.*
import com.mountain96.random.ui.foods.dialog.CategoryAddDialogFragment
import com.mountain96.random.ui.foods.dialog.FoodDialog
import kotlinx.android.synthetic.main.fragment_foods.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food.view.cardview_container
import kotlinx.android.synthetic.main.item_food_add.view.*

class FoodsFragment : Fragment() {
    //viewtype for recyclerview
    companion object{
        const val TYPE_ADD = 0
        const val TYPE_ITEM = 1
    }

    private lateinit var dashboardViewModel: FoodsViewModel
    var adapter: FoodsRecyclerviewAdapter? = null
    var categoryAdapter: CategoryRecyclerviewAdapter? = null
    var db: AppDatabase? = null
    val TAG: String = "FoodsFragment"
    var foodDialog : FoodDialog? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(FoodsViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_foods, container, false)
        db = AppDatabase.getInstance(requireContext())

        categoryAdapter = CategoryRecyclerviewAdapter()


        view.category_recyclerview.adapter = categoryAdapter

        var categoryLayout = LinearLayoutManager(activity)
        categoryLayout.orientation = RecyclerView.HORIZONTAL
        view.category_recyclerview.layoutManager = categoryLayout
        view.category_recyclerview.isHorizontalScrollBarEnabled = false

        adapter = FoodsRecyclerviewAdapter()
        view.foods_recyclerview.adapter = adapter
        view.foods_recyclerview.layoutManager = LinearLayoutManager(activity)

        foodDialog = FoodDialog(childFragmentManager, db!!, categoryAdapter)


        return view
    }

    override fun onDestroyView() {
        updateDB()
        super.onDestroyView()
    }

    fun updateDB() {
        for(food in adapter!!.foodList) {
            db!!.foodDao().updateFood(food)
        }
    }



    inner class CategoryRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var categoryList : ArrayList<FoodCategory> = arrayListOf()

        init {
            //** 모델 변경시에만
            //db?.clearAllTables()
            //**
            val savedCategory = db!!.foodCategoryDao().getAll()
            if (savedCategory.isNotEmpty()) {
                categoryList.addAll(savedCategory)
                //db?.clearAllTables()
                //InitSettings.initCategory(db!!, categoryList)
            } else {
                InitSettings.initCategory(db!!, categoryList)
            }
        }

        fun addCategory(name: String) {
            FoodCategory(db!!.foodCategoryDao().getAll().size, name, false, ModelType.TYPE_ITEM).let{ category ->
                categoryList.add(category)
                db!!.foodCategoryDao().insertAll(category)
            }
        }

        override fun getItemViewType(position: Int): Int {
            super.getItemViewType(position)
            var result : Int
            when(categoryList.get(position).type) {
                ModelType.TYPE_BUTTON -> result = 0
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

        override fun getItemCount(): Int {
            return categoryList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var itemview = holder.itemView
            var category = categoryList.get(position)

            itemview.category_button.text = category.name

            if (category.type == ModelType.TYPE_BUTTON) {
                itemview.category_button.setOnClickListener {
                    foodDialog!!.showCategoryAddDialog()
                }
                return
            }


            if (category.isChecked) {
                itemview.category_button.background = resources.getDrawable(R.drawable.button_category_filled)
                itemview.category_button.setTextColor(resources.getColor(R.color.colorCategorySelected))
                setFoodsByCategory(category.foodCategoryId)
            }
            else {
                itemview.category_button.background = resources.getDrawable(R.drawable.button_category)
                itemview.category_button.setTextColor(resources.getColor(R.color.colorCategoryUnselected))
            }

            itemview.category_button.setOnClickListener {
                if (category.isChecked) {
                    selectCategoryAll()
                    adapter!!.selectAll()
                } else {
                    for(category in categoryList) {
                        category.isChecked = false
                    }
                    category.isChecked = true

                    setFoodsByCategory(category.foodCategoryId)
                    categoryList.set(position, category)
                }
                this.notifyDataSetChanged()

                var categoryDao = db!!.foodCategoryDao()
                for(category in categoryList) {
                    categoryDao.updateCategory(category)
                }
            }
        }

        fun selectCategoryAll() {
            for(category in categoryList) {
                category.isChecked = false
            }
            for(category in categoryList) {
                if (category.foodCategoryId == 0) {
                    category.isChecked = true
                    break
                }
            }
        }

        fun setFoodsByCategory(foodCategoryId: Int) {
            if (foodCategoryId == 1) {
                adapter!!.selectAll()
                return
            }
            val foodCategoryWithFood = db!!.foodCategoryDao().getFoodCategoryWithFood()
            var selectedCategoryWithFood : FoodCategoryWithFood? = null
            for (item in foodCategoryWithFood) {
                if (item.category.foodCategoryId == foodCategoryId) {
                    selectedCategoryWithFood = item
                    break
                }
            }
            adapter!!.selectByCategory(selectedCategoryWithFood?.foodList!!)
        }
    }



    inner class FoodsRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var foodList : ArrayList<Food> = arrayListOf()
        //var db: AppDatabase? = null

        init {
            val savedFoods = db!!.foodDao().getALL()
            if (savedFoods.isNotEmpty()) {
                //배포시 내용 삭제하고 주석 없애기
                foodList.addAll(savedFoods)
                //db?.clearAllTables()
                //InitSettings.initFoods(db, foodList)
            } else {
                InitSettings.initFoods(db, foodList)
            }
        }

        override fun getItemViewType(position: Int): Int {
            super.getItemViewType(position)
            var result: Int
            when(foodList.get(position).type) {
                ModelType.TYPE_BUTTON -> result = 0
                ModelType.TYPE_ITEM -> result = 1
            }
            return result
        }

        fun selectByCategory(newList: List<Food>) {
            val button = foodList.get(0)
            foodList.clear()
            foodList.add(button)
            foodList.addAll(newList)
            this.notifyDataSetChanged()

        }

        fun selectAll() {
            foodList.clear()
            val savedFoods = db!!.foodDao().getALL()
            foodList.addAll(savedFoods)
            this.notifyDataSetChanged()
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

        override fun getItemCount(): Int {
            return foodList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var view = holder.itemView
            var food = foodList.get(position)

            if (food.type == ModelType.TYPE_BUTTON) {
                view.button_add_food.setOnClickListener {
                    foodDialog!!.showFoodAddDialog()
                }
                return
            }

            view.linearlayout_select_area.setOnClickListener(View.OnClickListener {
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
            Glide.with(requireActivity()).load(foodList.get(position).image).apply(RequestOptions().circleCrop()).into(view.imageview_food)

            if (food.isFavorite) {
                view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
            } else {
                view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))
            }

            if (food.isChecked) {
                view.checkbox_itemfood.isChecked = true
            } else {
                view.checkbox_itemfood.isChecked = false
            }
        }
    }
}