package com.gofedora.myreminder

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(private val defaultDate: String?): DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var callback: FragmentCallback

    fun setFragmentActionListener(callback: FragmentCallback): DatePickerFragment {
        this.callback = callback
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        context?.let {
            val dialog = DatePickerDialog(it, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            defaultDate?.let {
                val parts = defaultDate.split("-")
                if (parts.size == 3) {
                    dialog.datePicker.updateDate(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                }
            }
            return dialog
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val date = "%d-%02d-%02d".format(year, month + 1, dayOfMonth)
        Log.e(getString(R.string.LogTag), "In DatePickerFragment: $date")

        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.DATE_SELECTED)
            putString(FragmentCallback.DATE, date)
        })
    }
}