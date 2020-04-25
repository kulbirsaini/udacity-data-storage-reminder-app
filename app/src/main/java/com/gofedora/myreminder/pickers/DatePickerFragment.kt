package com.gofedora.myreminder.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.gofedora.myreminder.R
import com.gofedora.myreminder.fragments.FragmentCallback
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var callback: FragmentCallback
    private var date: String? = null

    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    fun setDate(date: String?) {
        this.date = date
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        context?.let {
            val dialog = DatePickerDialog(it, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            this.date?.let { dateStr: String ->
                SimpleDateFormat("EEE, d MMM, yyyy", Locale.US).parse(dateStr)?.let { parsedDate: Date ->
                    cal.time = parsedDate
                    Log.e(getString(R.string.LogTag), "Setting date to $dateStr")
                    dialog.datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                }
            }

            return dialog
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        Log.e(getString(R.string.LogTag), "In DatePickerFragment: ${cal.time}")

        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.DATE_SELECTED)
            putSerializable(FragmentCallback.DATE, cal.time)
        })
    }
}