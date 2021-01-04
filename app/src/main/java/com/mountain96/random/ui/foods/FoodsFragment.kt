package com.mountain96.random.ui.foods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mountain96.random.R
import com.mountain96.random.model.*
import com.mountain96.random.ui.foods.dialog.FoodDialog
import com.mountain96.random.ui.recyclerview.FoodsCategoryRecyclerviewAdapter
import com.mountain96.random.ui.recyclerview.FoodsRecyclerviewAdapter
import kotlinx.android.synthetic.main.fragment_foods.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category.view.category_button
import kotlinx.android.synthetic.main.item_category_add.view.*

class FoodsFragment : Fragment() {
    //viewtype for recyclerview
    companion object{
        const val TYPE_ADD = 0
        const val TYPE_ITEM = 1
    }

    private lateinit var dashboardViewModel: FoodsViewModel
    var adapter: FoodsRecyclerviewAdapter? = null
    var categoryAdapter: FoodsCategoryRecyclerviewAdapter? = null
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


        adapter = FoodsRecyclerviewAdapter(db!!, resources, requireActivity())
        view.foods_recyclerview.adapter = adapter
        view.foods_recyclerview.layoutManager = LinearLayoutManager(activity)

        categoryAdapter = FoodsCategoryRecyclerviewAdapter(db!!, resources, requireActivity(), adapter!!)
        view.category_recyclerview.adapter = categoryAdapter
        var categoryLayout = LinearLayoutManager(activity)
        categoryLayout.orientation = RecyclerView.HORIZONTAL
        view.category_recyclerview.layoutManager = categoryLayout
        view.category_recyclerview.isHorizontalScrollBarEnabled = false

        foodDialog = FoodDialog(childFragmentManager, db!!, categoryAdapter, adapter)

        view.foods_recyclerview.setOnClickListener {
            if (adapter!!.isRemoveStatus) {
                adapter!!.notifyDataSetChanged()
            }
        }

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
            } else {
                InitSettings.initCategory(db!!, categoryList)
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

            if (category.type == ModelType.TYPE_BUTTON) {
                itemview.category_add_button.setOnClickListener {
                    foodDialog!!.showCategoryAddDialog()
                }
                return
            }

            itemview.category_button.text = category.name
            itemview.icon_remove_category.visibility=View.INVISIBLE

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
            if (foodCategoryId == categoryList.get(1).foodCategoryId) {
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
}