package com.mountain96.random.ui.foods.dialog

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.model.ModelType
import com.mountain96.random.ui.foods.FoodsFragment

class FoodDialog(val fragmentManager: FragmentManager, val db: AppDatabase, val categoryAdapter: FoodsFragment.CategoryRecyclerviewAdapter?) : NoticeDialogListener {
    fun showCategoryAddDialog() {
        val dialog = CategoryAddDialogFragment(this)
        dialog.show(fragmentManager, "CategoryAddDialogFragment")
    }

    fun showFoodAddDialog() {
        val dialog = FoodAddDialogFragment(this, db)
        dialog.show(fragmentManager, "FoodAddDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, name: String, type: DialogType) {
        when(type) {
            DialogType.TYPE_CATEGORY -> {
                categoryAdapter!!.addCategory(name)
                categoryAdapter!!.notifyDataSetChanged()
            }
            DialogType.TYPE_FOOD -> {

            }
        }
    }
}