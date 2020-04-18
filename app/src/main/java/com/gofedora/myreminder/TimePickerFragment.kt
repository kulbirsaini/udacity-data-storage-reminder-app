package com.gofedora.myreminder

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
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

            defaultTime?.let {
                val parts = defaultTime.split(":")
                if (parts.size == 2) {
                    dialog.updateTime(parts[0].toInt(), parts[1].toInt())
                }
            }

            return dialog
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val time = "%02d:%02d".format(hourOfDay, minute)
        Log.e(getString(R.string.LogTag), "In DatePickerFragment: $time")

        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.TIME_SELECTED)
            putString(FragmentCallback.TIME, time)
        })
    }
}