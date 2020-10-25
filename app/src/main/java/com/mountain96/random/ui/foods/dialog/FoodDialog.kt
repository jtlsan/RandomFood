package com.mountain96.random.ui.foods.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.ui.foods.FoodsFragment

class FoodDialog(val fragmentManager: FragmentManager, val db: AppDatabase, val categoryAdapter: FoodsFragment.CategoryRecyclerviewAdapter?, val foodAdapter: FoodsFragment.FoodsRecyclerviewAdapter?) : NoticeDialogListener {
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