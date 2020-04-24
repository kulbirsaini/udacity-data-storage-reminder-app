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

class TimePickerFragment(private val defaultTime: String?): DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var callback: FragmentCallback

    fun setFragmentActionListener(callback: FragmentCallback): TimePickerFragment {
        this.callback = callback
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        context?.let {
            val dialog = TimePickerDialog(it, this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(it))

            defaultTime?.let { timeStr: String ->
                SimpleDateFormat("hh:mm a", Locale.US).parse(timeStr)?.let { time: Date ->
                    cal.time = time
                    Log.e(getString(R.string.LogTag), "Setting time to $defaultTime")
                    dialog.updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                }
            }

            return dialog
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)

        Log.e(getString(R.string.LogTag), "In DatePickerFragment: ${cal.time}")

        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.TIME_SELECTED)
            putSerializable(FragmentCallback.TIME, cal.time)
        })
    }
}