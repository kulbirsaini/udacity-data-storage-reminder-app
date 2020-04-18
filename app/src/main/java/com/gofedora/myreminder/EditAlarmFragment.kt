package com.gofedora.myreminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.edit_alarm.*

class EditAlarmFragment: Fragment() {
    private lateinit var callback: FragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarm = arguments?.getSerializable(FragmentCallback.ALARM) as Alarm?

        alarm?.let {
            titleValue.setText(alarm.title)
        }

        val spinner = view.findViewById<Spinner>(R.id.occasionSpinner)
        context?.let {
            val adapter = ArrayAdapter.createFromResource(it, R.array.occasion_values, android.R.layout.simple_spinner_dropdown_item)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.setSelection(adapter.getPosition("Birthday"))
        }

        dateValue.setOnClickListener {
            pickerClicked(FragmentCallback.DATE_PICKER_CLICKED, FragmentCallback.DATE, dateValue.text.toString())
        }

        timeValue.setOnClickListener {
            pickerClicked(FragmentCallback.TIME_PICKER_CLICKED, FragmentCallback.TIME, timeValue.text.toString())
        }
    }

    private fun pickerClicked(pickerType: Int, key: String, value: String) {
        Log.e(getString(R.string.LogTag), pickerType.toString())
        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, pickerType)
            putString(key, value)
        })
    }

    fun setFragmentActionListener(callback: FragmentCallback): EditAlarmFragment {
        this.callback = callback
        return this
    }

    fun onFragmentActionPerformed(bundle: Bundle) {
        when (bundle.getInt(FragmentCallback.ACTION_KEY)) {
            FragmentCallback.DATE_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In EditFragment: " + bundle.getString(FragmentCallback.DATE))
                dateValue.setText(bundle.getString(FragmentCallback.DATE))
            }
            FragmentCallback.TIME_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In EditFragment: " + bundle.getString(FragmentCallback.TIME))
                timeValue.setText(bundle.getString(FragmentCallback.TIME))
            }
        }
    }
}