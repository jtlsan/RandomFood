package com.mountain96.random.settings.dialog


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mountain96.random.R
import java.lang.IllegalStateException

class DataResetDialogFragment(var listener: NoticeDialogListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_reset_data)
                .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->
                    listener.onDialogPositiveClick(this)
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialog!!.cancel()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }
}