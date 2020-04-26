package com.gofedora.myreminder.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.gofedora.myreminder.MainActivity
import com.gofedora.myreminder.R
import com.gofedora.myreminder.fragments.FragmentCallback
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var callback: FragmentCallback

    /**
     * Override onActivityCreated to reassign callback
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.callback = activity as MainActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            val cal = Calendar.getInstance()

            // If we have a date, use it
            arguments?.getString(FragmentCallback.DATE)?.let { dateStr ->
                SimpleDateFormat(getString(R.string.date_format), Locale.US).parse(dateStr)?.let { parsedDate ->
                    cal.time = parsedDate
                }
            }

            Log.e(getString(R.string.LogTag), "Setting date to ${cal.time}")

            return DatePickerDialog(it, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        // Build a date from selected values
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        Log.e(getString(R.string.LogTag), "In DatePickerFragment: ${cal.time}")

        // Announce the selected date to activity/fragment
        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.DATE_SELECTED)
            putSerializable(FragmentCallback.DATE, cal.time)
        })
    }
}