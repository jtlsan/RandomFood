package com.mountain96.random.ui.foods.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mountain96.random.R
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import java.lang.ClassCastException
import java.lang.IllegalStateException

class CategoryAddDialogFragment(var listener: NoticeDialogListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val itemView = inflater.inflate(R.layout.dialog_add_category, null)

            builder.setView(itemView)
                .setPositiveButton(R.string.add_button, DialogInterface.OnClickListener { dialog, id ->
                    listener.onDialogPositiveClick(this, itemView.textCategoryName.text.toString())
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