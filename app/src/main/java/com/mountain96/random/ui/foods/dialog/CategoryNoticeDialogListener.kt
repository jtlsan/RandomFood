package com.mountain96.random.ui.foods.dialog

import androidx.fragment.app.DialogFragment

interface NoticeDialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment, name: String, type: DialogType)
}