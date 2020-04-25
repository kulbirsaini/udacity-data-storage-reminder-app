package com.gofedora.myreminder.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.gofedora.myreminder.R
import com.gofedora.myreminder.fragments.FragmentCallback
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var callback: FragmentCallback
    private var time: String? = null

    /**
     * Callback instance to communicate with parent activity/fragment
     */
    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    /**
     * Set time that should be the default time on the TimePicker when initialized
     * Format: R.string.time_format
     */
    fun setTime(time: String?) {
        this.time = time
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            val cal = Calendar.getInstance()

            // If we have a time, use it
            time?.let { timeStr ->
                SimpleDateFormat(getString(R.string.time_format), Locale.US).parse(timeStr)?.let { parsedTime ->
                    cal.time = parsedTime
                }
            }

            return TimePickerDialog(it, this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(it))
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // Build a Date instance with values provided
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        Log.e(getString(R.string.LogTag), "In DatePickerFragment: ${cal.time}")

        // Announce the selected time to activity/fragment
        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.TIME_SELECTED)
            putSerializable(FragmentCallback.TIME, cal.time)
        })
    }
}