package com.mountain96.random.ui.foods.dialog

import androidx.fragment.app.DialogFragment
import com.mountain96.random.model.Food

interface NoticeDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment, name: String)
    fun onDialogPositiveClick(dialog: DialogFragment, name: String, position : Int, imageUrl : String)
}