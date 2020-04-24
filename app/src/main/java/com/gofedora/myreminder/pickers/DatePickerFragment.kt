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

            defaultDate?.let { dateStr: String ->
                SimpleDateFormat("EEE, d MMM, yyyy", Locale.US).parse(dateStr)?.let { date: Date ->
                    cal.time = date
                    Log.e(getString(R.string.LogTag), "Setting date to $defaultDate")
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