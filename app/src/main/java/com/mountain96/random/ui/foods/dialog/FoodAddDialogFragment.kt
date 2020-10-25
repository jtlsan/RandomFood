package com.mountain96.random.ui.foods.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.model.ModelType
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.dialog_add_category.view.textCategoryName
import kotlinx.android.synthetic.main.dialog_add_food.view.*
import kotlinx.android.synthetic.main.fragment_combination.view.*
import java.lang.ClassCastException
import java.lang.IllegalStateException

class FoodAddDialogFragment(var listener: NoticeDialogListener, val db : AppDatabase) : DialogFragment() {
    companion object {
        val GALLERY_REQUEST_CODE : Int = 111
    }

    lateinit var itemview: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            itemview = inflater.inflate(R.layout.dialog_add_food, null)

            val spinner = itemview.spinner_add_food_dialog_category

            var nameList: ArrayList<String> = arrayListOf()
            var categoryList = db.foodCategoryDao().getAll()
            categoryList.iterator().let {foodIterator ->
                foodIterator.next()
                foodIterator.next()
                foodIterator.forEach {
                    nameList.add(it.name)
                }
            }
            var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nameList)
            spinner!!.adapter = spinnerAdapter
            builder.setView(itemview)
                .setPositiveButton(R.string.add_button, DialogInterface.OnClickListener { dialog, id ->
                    validateDialog()
                    var imageUrl : String
                    if(itemview.DialogCheckBox.isChecked)
                        imageUrl = ""
                    else
                        imageUrl = itemview.textDialogFoodUrl.text.toString()

                    listener.onDialogPositiveClick(this, itemview.textFoodName.text.toString(), spinner.selectedItemPosition+2, imageUrl)
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {dialog, id ->
                    getDialog()?.cancel()
                })


            itemview.DialogCheckBox.setOnCheckedChangeListener { compoundButton, b ->
                if(b) {
                    itemview.textDialogFoodUrl.isEnabled = false
                }
                else {
                    itemview.textDialogFoodUrl.isEnabled = true
                }
            }

            itemview.dialogNoImgTextview.setOnClickListener {
                itemview.DialogCheckBox.toggle()
            }
            itemview.textDialogFoodUrl.setOnClickListener {
                Intent().let {
                    it.setType("image/*")
                    it.setAction(Intent.ACTION_PICK)
                    startActivityForResult(it, GALLERY_REQUEST_CODE)
                }
            }

            builder.create().apply {
                setOnShowListener {
                    this.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorDialogButton))
                    this.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorDialogButton))
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null!")
    }

    fun validateDialog(): Boolean {
        if (itemview.textFoodName.text.isEmpty()) {
            Toast.makeText(requireContext(), "음식이름이 올바르지 않습니다.", Toast.LENGTH_LONG).show()
            return false
        }
        if (!itemview.DialogCheckBox.isChecked && itemview.textDialogFoodUrl.text.isEmpty()) {
            Toast.makeText(requireContext(), "사진을 선택해 주세요.", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                var imageUri = data?.data
                itemview.textDialogFoodUrl.setText(imageUri.toString())
            }
        }
    }
}