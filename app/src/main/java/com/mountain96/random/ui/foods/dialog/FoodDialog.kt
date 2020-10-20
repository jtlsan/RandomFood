package com.mountain96.random.ui.foods.dialog

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.model.ModelType
import com.mountain96.random.ui.foods.FoodsFragment

class FoodDialog(val fragmentManager: FragmentManager, val categoryAdapter: FoodsFragment.CategoryRecyclerviewAdapter?) : CategoryAddDialogFragment.NoticeDialogListener {
    fun showCategoryAddDialog() {
        val dialog = CategoryAddDialogFragment(this)
        dialog.show(fragmentManager, "CategoryAddDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, name: String) {
        categoryAdapter!!.addCategory(name)
        categoryAdapter!!.notifyDataSetChanged()
    }
}