package com.mountain96.random.ui.foods.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.ui.foods.FoodsFragment
import com.mountain96.random.ui.recyclerview.FoodsCategoryRecyclerviewAdapter
import com.mountain96.random.ui.recyclerview.FoodsRecyclerviewAdapter

class FoodDialog(val fragmentManager: FragmentManager, val db: AppDatabase, val categoryAdapter: FoodsCategoryRecyclerviewAdapter?, val foodAdapter: FoodsRecyclerviewAdapter?) : NoticeDialogListener {
    init {
        foodAdapter!!.setDialog(this)
        categoryAdapter!!.setDialog(this)
    }

    fun showCategoryAddDialog() {
        val dialog = CategoryAddDialogFragment(this)
        dialog.show(fragmentManager, "CategoryAddDialogFragment")
    }

    fun showFoodAddDialog() {
        val dialog = FoodAddDialogFragment(this, db)
        dialog.show(fragmentManager, "FoodAddDialogFragment")
    }


    override fun onDialogPositiveClick(dialog: DialogFragment, name: String) {
        categoryAdapter!!.addCategory(name)
        categoryAdapter!!.notifyDataSetChanged()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, name: String, position : Int, imageUrl : String) {
        foodAdapter!!.addFood(name, position, imageUrl)
        foodAdapter!!.notifyDataSetChanged()
    }

}