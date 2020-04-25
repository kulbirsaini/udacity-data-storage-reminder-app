package com.gofedora.myreminder.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gofedora.myreminder.R
import com.gofedora.myreminder.Reminder
import kotlinx.android.synthetic.main.edit_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderEditFragment: Fragment() {
    private lateinit var reminder: Reminder
    private lateinit var callback: FragmentCallback

    /**
     * Callback instance to communicate with parent activity/fragment
     */
    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if we received a reminder via arguments. If not, initialize a new reminder with defaults
        reminder = arguments?.let {
            it.getSerializable(FragmentCallback.REMINDER) as Reminder?
        } ?: Reminder(occasion = Reminder.getOccasionId(context, 0))

        // Set default fields
        titleValue.setText(reminder.title)
        setDate(reminder.time)
        setTime(reminder.time)

        // Initialize Occasion spinner dropdown
        context?.let {
            val adapter = ArrayAdapter.createFromResource(it, R.array.occasion_values, android.R.layout.simple_spinner_dropdown_item)

            occasionSpinner.adapter = adapter

            // Set selection to reminder's occasion
            occasionSpinner.setSelection(adapter.getPosition(Reminder.getOccasionValue(it, reminder.occasion)))
        }

        // Attach onClickListener to initiate DatePicker
        dateValue.setOnClickListener {
            pickerClicked(FragmentCallback.DATE_CLICKED, FragmentCallback.DATE, dateValue.text.toString())
        }

        // Attach onClickListener to initiate TimePicker
        timeValue.setOnClickListener {
            pickerClicked(FragmentCallback.TIME_CLICKED, FragmentCallback.TIME, timeValue.text.toString())
        }
    }

    /**
     * Announce the user action to MainActivity so that it can initialize Date/Time Picker
     */
    private fun pickerClicked(pickerType: Int, key: String, value: String) {
        Log.e(getString(R.string.LogTag), "Picker Clicked $pickerType $key $value")
        this.callback.onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, pickerType)
            putString(key, value)
        })
    }

    /**
     * Display selected date in a formatted manner
     */
    private fun setDate(date: Date?) {
        dateValue.text = SimpleDateFormat(getString(R.string.date_format), Locale.US).format(date ?: Date())
    }

    /**
     * Display selected time in a formatted manner
     */
    private fun setTime(time: Date?) {
        timeValue.text = SimpleDateFormat(getString(R.string.time_format), Locale.US).format(time ?: Date())
    }

    /**
     * Override onCreate so that we can enable fragment specific menu items
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    /**
     * Override onCreateOptionsMenu to inflate menu for this fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handle menu item clicks
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Save button was clicked on edit page
            R.id.actionSave -> {
                // Get latest values from various input fields
                reminder.title = titleValue.text.toString()
                reminder.occasion = Reminder.getOccasionId(context, occasionSpinner.selectedItemPosition)
                SimpleDateFormat(getString(R.string.date_time_format), Locale.US).parse("${dateValue.text} ${timeValue.text}")?.let {
                    reminder.time = it
                }

                Log.e(getString(R.string.LogTag), "Saving ${reminder.title} / ${reminder.time} / ${reminder.occasion} / Position: ${occasionSpinner.selectedItemPosition}")

                // If we have invalid input, show error and halt
                if (reminder.title.isEmpty()) {
                    Toast.makeText(context, getString(R.string.title_error), Toast.LENGTH_SHORT).show()
                    return false
                }

                if (reminder.time.before(Date())) {
                    Toast.makeText(context, getString(R.string.date_time_error), Toast.LENGTH_SHORT).show()
                    return false
                }

                if (reminder.occasion < 0) {
                    Toast.makeText(context, getString(R.string.occasion_error), Toast.LENGTH_SHORT).show()
                    return false
                }

                // Announce to MainActivity to save the reminder
                this.callback.onActionPerformed(Bundle().apply {
                    putInt(FragmentCallback.ACTION_KEY, FragmentCallback.SAVE_REMINDER_CLICKED)
                    putSerializable(FragmentCallback.REMINDER, reminder)
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Handle callbacks to this fragment invoked by MainActivity
     */
    fun onFragmentActionPerformed(bundle: Bundle) {
        when (bundle.getInt(FragmentCallback.ACTION_KEY)) {
            // Date selected using DatePicker
            FragmentCallback.DATE_SELECTED -> {
                val date = bundle.getSerializable(FragmentCallback.DATE) as Date
                Log.e(getString(R.string.LogTag), "In EditFragment: $date")
                setDate(date)
            }
            // Time selected using TimePicker
            FragmentCallback.TIME_SELECTED -> {
                val time = bundle.getSerializable(FragmentCallback.TIME) as Date
                Log.e(getString(R.string.LogTag), "In EditFragment: $time")
                setTime(time)
            }
        }
    }
}