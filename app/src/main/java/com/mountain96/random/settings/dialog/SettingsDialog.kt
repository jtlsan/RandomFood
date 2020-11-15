package com.mountain96.random.settings.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.ui.foods.InitSettings

class SettingsDialog(val fragmentManager: FragmentManager) : NoticeDialogListener {

    fun showDataResetDialog() {
        val dialog = DataResetDialogFragment(this)
        dialog.show(fragmentManager, "FoodAddDialogFragment")
    }


    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val db = AppDatabase.getInstance(dialog.requireContext())
        db!!.clearAllTables()
        InitSettings.initCategory(db, arrayListOf())
        InitSettings.initFoods(db, arrayListOf())
    }


}