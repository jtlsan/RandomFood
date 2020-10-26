package com.mountain96.random.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mountain96.random.R
import com.mountain96.random.model.*
import com.mountain96.random.ui.foods.FoodsFragment
import com.mountain96.random.ui.foods.InitSettings
import kotlinx.android.synthetic.main.fragment_foods.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_food.view.*

class FavoriteFragment : Fragment(){

    var adapter: FavoriteFragment.FavoriteFoodsRecyclerviewAdapter? = null
    var db: AppDatabase? = null
    var recyclerview: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foods, container, false)
        db = AppDatabase.getInstance(requireContext())


        var categoryAdapter = CategoryRecyclerviewAdapter()
        view.category_recyclerview.adapter = categoryAdapter

        var categoryLayout = LinearLayoutManager(activity)
        categoryLayout.orientation = RecyclerView.HORIZONTAL
        view.category_recyclerview.layoutManager = categoryLayout
        view.category_recyclerview.isHorizontalScrollBarEnabled = false

        adapter = FavoriteFoodsRecyclerviewAdapter()
        view.foods_recyclerview.adapter = adapter
        view.foods_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    fun updateFavorite(position: Int) {
        adapter!!.foodList.removeAt(position)
        //recyclerview!!.removeViewAt(position)
        adapter!!.notifyItemRemoved(position);
        adapter!!.notifyItemRangeChanged(position, adapter!!.foodList.size)
        adapter!!.notifyDataSetChanged()
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
            //remove add button
            categoryList.removeAt(0)
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


    inner class FavoriteFoodsRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var foodList : ArrayList<Food> = arrayListOf()
        //var db: AppDatabase? = null

        init {
            db = AppDatabase.getInstance(context!!)
            val savedFoods = db!!.foodDao().getAllFavorites()
            foodList.addAll(savedFoods)
        }

        fun selectByCategory(newList: List<Food>) {
            foodList.clear()
            for(food in newList) {
                if (food.isFavorite)
                    foodList.add(food)
            }
            this.notifyDataSetChanged()
        }

        fun selectAll() {
            foodList.clear()
            val savedFoods = db!!.foodDao().getAllFavorites()
            foodList.addAll(savedFoods)
            this.notifyDataSetChanged()
        }

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
                updateFavorite(position)

            }
            view.textview_foodname.text = foodList.get(position).name

            if(food.image.isEmpty())
                Glide.with(requireActivity()).load(resources.getString(R.string.empty_food)).apply(RequestOptions().circleCrop()).into(view.imageview_food)
            else
                Glide.with(requireActivity()).load(food.image).apply(RequestOptions().circleCrop()).into(view.imageview_food)

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