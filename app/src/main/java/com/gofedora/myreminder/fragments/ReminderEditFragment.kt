package com.gofedora.myreminder.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gofedora.myreminder.Reminder
import com.gofedora.myreminder.R
import kotlinx.android.synthetic.main.edit_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderEditFragment: Fragment() {
    private lateinit var reminder: Reminder
    private lateinit var callback: FragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_reminder, container, false)
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

        reminder = arguments?.let {
            it.getSerializable(FragmentCallback.REMINDER) as Reminder?
        } ?: Reminder(occasion = Reminder.getOccasionId(context, 0))

        titleValue.setText(reminder.title)
        setDate(reminder.time)
        setTime(reminder.time)

        val spinner = view.findViewById<Spinner>(R.id.occasionSpinner)
        context?.let {
            val adapter = ArrayAdapter.createFromResource(it, R.array.occasion_values, android.R.layout.simple_spinner_dropdown_item).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            spinner.adapter = adapter
            spinner.setSelection(adapter.getPosition(Reminder.getOccasionValue(it, reminder.occasion)))
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
                reminder.title = titleValue.text.toString()
                reminder.occasion = Reminder.getOccasionId(context, occasionSpinner.selectedItemPosition)
                SimpleDateFormat("EEE, d MMM, yyyy hh:mm a", Locale.US).parse("${dateValue.text} ${timeValue.text}")?.let {
                    reminder.time = it
                }

                Log.e(getString(R.string.LogTag), "Saving ${reminder.title} / ${reminder.time} / ${reminder.occasion} / Position: ${occasionSpinner.selectedItemPosition}")

                if (reminder.title.isEmpty() || reminder.time.before(Date()) || reminder.occasion < 0) {
                    Toast.makeText(context, "Input error!", Toast.LENGTH_LONG).show()
                    return false
                }

                this.callback.onActionPerformed(Bundle().apply {
                    putInt(FragmentCallback.ACTION_KEY, FragmentCallback.SAVE_REMINDER_CLICKED)
                    putSerializable(FragmentCallback.REMINDER, reminder)
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

    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
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