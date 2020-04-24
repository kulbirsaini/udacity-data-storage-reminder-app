package com.gofedora.myreminder.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gofedora.myreminder.Alarm
import com.gofedora.myreminder.R
import kotlinx.android.synthetic.main.edit_alarm.*
import java.text.SimpleDateFormat
import java.util.*

class EditAlarmFragment: Fragment() {
    private var alarm: Alarm? = null
    private lateinit var callback: FragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_alarm, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarm = arguments?.let {
            it.getSerializable(FragmentCallback.ALARM) as Alarm?
        } ?: Alarm(occasion = Alarm.getOccasionId(context, 0))

        titleValue.setText(alarm?.title)
        setDate(alarm?.time)
        setTime(alarm?.time)

        val spinner = view.findViewById<Spinner>(R.id.occasionSpinner)
        context?.let {
            val adapter = ArrayAdapter.createFromResource(it, R.array.occasion_values, android.R.layout.simple_spinner_dropdown_item)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.setSelection(adapter.getPosition(Alarm.getOccasionValue(it, alarm?.occasion ?: 0)))
        }

        dateValue.setOnClickListener {
            pickerClicked(FragmentCallback.DATE_PICKER_CLICKED, FragmentCallback.DATE, dateValue.text.toString())
        }

        timeValue.setOnClickListener {
            pickerClicked(FragmentCallback.TIME_PICKER_CLICKED, FragmentCallback.TIME, timeValue.text.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSave -> {
                val title = titleValue.text
                val cal = Calendar.getInstance()
                SimpleDateFormat("EEE, d MMM, yyyy hh:mm a", Locale.US).parse("${dateValue.text} ${timeValue.text}")?.let {
                    cal.time = it
                }
                val occasion = Alarm.getOccasionId(context, occasionSpinner.selectedItemPosition)
                val alarm = Alarm(title = title.toString(), time = cal.time, occasion = occasion)
                Log.e(getString(R.string.LogTag), "Saving $title / ${cal.time} / $occasion / Position: ${occasionSpinner.selectedItemPosition}")

                if (title.isEmpty() || cal.time.before(Date()) || occasion < 0) {
                    Toast.makeText(context, "Input error!", Toast.LENGTH_LONG).show()
                    return false
                }

                this.callback.onActionPerformed(Bundle().apply {
                    putInt(FragmentCallback.ACTION_KEY, FragmentCallback.SAVE_ALARM)
                    putSerializable(FragmentCallback.ALARM, alarm)
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun pickerClicked(pickerType: Int, key: String, value: String) {
        Log.e(getString(R.string.LogTag), "Picker Clicked $pickerType $key $value")
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
                val date = bundle.getSerializable(FragmentCallback.DATE) as Date
                Log.e(getString(R.string.LogTag), "In EditFragment: $date")
                setDate(date)
            }
            FragmentCallback.TIME_SELECTED -> {
                val time = bundle.getSerializable(FragmentCallback.TIME) as Date
                Log.e(getString(R.string.LogTag), "In EditFragment: $time")
                setTime(time)
            }
        }
    }

    private fun setDate(date: Date?) {
        dateValue.text = SimpleDateFormat("EEE, d MMM, yyyy", Locale.US).format(date ?: Date())
    }

    private fun setTime(time: Date?) {
        timeValue.text = SimpleDateFormat("hh:mm a", Locale.US).format(time ?: Date())
    }
}