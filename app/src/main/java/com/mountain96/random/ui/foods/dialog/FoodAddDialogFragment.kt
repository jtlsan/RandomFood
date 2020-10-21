package com.mountain96.random.ui.foods.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.FoodCategory
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.dialog_add_category.view.textCategoryName
import kotlinx.android.synthetic.main.dialog_add_food.view.*
import kotlinx.android.synthetic.main.fragment_combination.view.*
import java.lang.ClassCastException
import java.lang.IllegalStateException

class FoodAddDialogFragment(var listener: NoticeDialogListener, val db : AppDatabase) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val itemview = inflater.inflate(R.layout.dialog_add_food, null)

            val spinner = itemview.spinner_add_food_dialog_category

            var nameList: ArrayList<String> = arrayListOf()
            db.foodCategoryDao().getAll().iterator().let {foodIterator ->
                foodIterator.next()
                foodIterator.forEach {
                    nameList.add(it.name)
                }
            }
            var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nameList)
            spinner!!.adapter = spinnerAdapter
            builder.setView(itemview)
                .setPositiveButton(R.string.add_button, DialogInterface.OnClickListener { dialog, id ->
                    listener.onDialogPositiveClick(this, itemview.textCategoryName.text.toString(), DialogType.TYPE_FOOD)
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {dialog, id ->
                    getDialog()?.cancel()
                })

            builder.create().apply {
                setOnShowListener {
                    this.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorDialogButton))
                    this.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorDialogButton))
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}