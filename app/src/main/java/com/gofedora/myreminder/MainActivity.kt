package com.gofedora.myreminder

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.gofedora.myreminder.dialogs.DatePickerFragment
import com.gofedora.myreminder.dialogs.MyAlertDialog
import com.gofedora.myreminder.dialogs.TimePickerFragment
import com.gofedora.myreminder.fragments.FragmentCallback
import com.gofedora.myreminder.fragments.ReminderEditFragment
import com.gofedora.myreminder.fragments.ReminderListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Initiate and draw the reminder list on initial load. Don't draw on activity restart
        if (savedInstanceState == null)
            navigateToFragment(FragmentCallback.REMINDER_FRAGMENT_LIST)

        // Attach onClickListener to FloatingActionButton to switch to edit page
        fab.setOnClickListener {
            navigateToFragment(FragmentCallback.REMINDER_FRAGMENT_EDIT)
        }
    }

    /**
     * Navigate between fragments
     */
    private fun navigateToFragment(fragmentId: Int, bundle: Bundle = Bundle()) {
        when (fragmentId) {
            FragmentCallback.REMINDER_FRAGMENT_LIST -> {
                // Flush the FragmentManager backstack to clear any fragments
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ReminderListFragment())
                    .commit()
            }
            FragmentCallback.REMINDER_FRAGMENT_EDIT -> {
                val fragment = ReminderEditFragment().apply {
                    arguments = bundle
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("ReminderFragments")
                    .commit()
            }
        }
    }

    /**
     * Override onOptionsItemSelected to handle menu item clicks
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                Toast.makeText(this, "Go back", Toast.LENGTH_SHORT).show()
                true
            }
            // Insert Dummy Data clicked
            R.id.actionInsertDummyData -> {
                // Generate reminders from our string resource
                val reminders = resources.getStringArray(R.array.dummy_reminders).map {
                    Reminder(title = it)
                }

                ReminderViewModel(application).insertMultiple(reminders)
                Toast.makeText(this, getString(R.string.dummy_reminders_added), Toast.LENGTH_SHORT).show()
                true
            }
            // Delete All clicked
            R.id.actionDeleteAll -> {
                // Show confirmation dialog before performing delete all action
                MyAlertDialog().apply {
                    arguments = Bundle().apply {
                        putInt(FragmentCallback.ALERT_TITLE, R.string.delete_all_dialog_title)
                        putInt(FragmentCallback.ALERT_MESSAGE, R.string.delete_all_dialog_message)
                        putInt(FragmentCallback.ALERT_TYPE, FragmentCallback.ALERT_TYPE_DELETE_ALL)
                    }
                }.show(supportFragmentManager, "alertDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Override onBackPressed method so that we can show the FloatingActionButton when user navigates back to reminder list
     */
    override fun onBackPressed() {
        // Show FloatingActionButton
        fab.visibility = View.VISIBLE

        // Disable back to home navigation button
        toolbar.navigationIcon = null

        super.onBackPressed()
    }

    /**
     * Implement onActionPerformed method to handle actions invoked by other fragments
     */
    override fun onActionPerformed(bundle: Bundle) {
        Log.e(getString(R.string.LogTag), "Action: " + bundle.getInt(FragmentCallback.ACTION_KEY).toString())

        when (bundle.getInt(FragmentCallback.ACTION_KEY)) {
            // A reminder in the reminder list was clicked, navigate to edit page
            FragmentCallback.REMINDER_CLICKED -> {
                // Initiate ReminderEditFragment and pass along the arguments
                navigateToFragment(FragmentCallback.REMINDER_FRAGMENT_EDIT, bundle)
            }
            // Date was clicked on edit page, initiate and show DatePicker
            FragmentCallback.DATE_CLICKED -> {
                DatePickerFragment().apply {
                    arguments = bundle
                }.show(supportFragmentManager, "datePicker")
            }
            // Time was clicked on edit page, initiate and show TimePicker
            FragmentCallback.TIME_CLICKED -> {
                TimePickerFragment().apply {
                    arguments = bundle
                }.show(supportFragmentManager, "timePicker")
            }
            // Date was selected on DatePicker, announce it to ReminderEditFragment
            FragmentCallback.DATE_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: " + bundle.getSerializable(FragmentCallback.DATE))
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ReminderEditFragment
                fragment.onFragmentActionPerformed(bundle)
            }
            // Time was selected on TimePicker, announce it to ReminderEditFragment
            FragmentCallback.TIME_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: " + bundle.getSerializable(FragmentCallback.TIME))
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ReminderEditFragment
                fragment.onFragmentActionPerformed(bundle)
            }
            // Save button was clicked on edit page
            FragmentCallback.SAVE_REMINDER_CLICKED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Save Reminder")

                // Retrieve reminder from edit page
                val reminder = bundle.getSerializable(FragmentCallback.REMINDER) as Reminder?
                reminder?.let {
                    // If this reminder has a positive id, we need to update it instead of creating a new record
                    if (it.id > 0) {
                        ReminderViewModel(application).update(it)
                        Toast.makeText(this, getString(R.string.reminder_updated), Toast.LENGTH_SHORT).show()
                    } else {
                        ReminderViewModel(application).insert(it)
                        Toast.makeText(this, getString(R.string.reminder_added), Toast.LENGTH_SHORT).show()
                    }
                }

                // Time to switch back to reminder list
                navigateToFragment(FragmentCallback.REMINDER_FRAGMENT_LIST)
            }
            // Delete Icon clicked
            FragmentCallback.DELETE_REMINDER_CLICKED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Delete Reminder")

                val reminder = bundle.getSerializable(FragmentCallback.REMINDER) as Reminder?

                // We need to something only if we actually have a reminder
                reminder?.let { thisReminder ->
                    // Show confirmation dialog before performing the actual delete action
                    MyAlertDialog().apply {
                        arguments = Bundle().apply {
                            putInt(FragmentCallback.ALERT_TITLE, R.string.delete_dialog_title)
                            putInt(FragmentCallback.ALERT_MESSAGE, R.string.delete_dialog_message)
                            putInt(FragmentCallback.ALERT_TYPE, FragmentCallback.ALERT_TYPE_DELETE)
                            putSerializable(FragmentCallback.REMINDER, thisReminder)
                        }
                    }.show(supportFragmentManager, "alertDialog")
                }
            }
            // Alert Action clicked
            FragmentCallback.ALERT_ACTION_PERFORMED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Alert Action Performed: ${bundle.getInt(FragmentCallback.ALERT_BUTTON)}")

                // See which button on the alert was clicked
                when (bundle.getInt(FragmentCallback.ALERT_BUTTON)) {
                    // User agreed to the message
                    DialogInterface.BUTTON_POSITIVE -> {
                        Log.e(getString(R.string.LogTag), "In MainActivity: Alert Type: ${bundle.getInt(FragmentCallback.ALERT_TYPE)}")

                        // See what alert we showed to the user so that we can take appropriate post-process action
                        when (bundle.getInt(FragmentCallback.ALERT_TYPE)) {
                            // Delete Reminder Alert
                            FragmentCallback.ALERT_TYPE_DELETE -> {
                                Log.e(getString(R.string.LogTag), "In MainActivity: Alert Delete: ${(bundle.getSerializable(FragmentCallback.REMINDER) as Reminder).title}")

                                val reminder = bundle.getSerializable(FragmentCallback.REMINDER) as Reminder?
                                reminder?.let {
                                    ReminderViewModel(application).delete(it)
                                    Toast.makeText(this@MainActivity, getString(R.string.reminder_deleted), Toast.LENGTH_SHORT).show()
                                }
                            }
                            // Delete All Reminders Alert
                            FragmentCallback.ALERT_TYPE_DELETE_ALL -> {
                                Log.e(getString(R.string.LogTag), "In MainActivity: Alert Delete All")

                                ReminderViewModel(application).deleteAll()
                                Toast.makeText(this, getString(R.string.reminder_deleted_all), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            // Manage various UI elements based on which fragment is currently visible
            FragmentCallback.REMINDER_FRAGMENT_RENDERED -> {
                when (bundle.getInt(FragmentCallback.FRAGMENT_TYPE)) {
                    FragmentCallback.REMINDER_FRAGMENT_LIST -> {
                        // Show FloatingActionButton
                        fab.visibility = View.VISIBLE

                        // Disable back to home navigation button
                        toolbar.navigationIcon = null
                        toolbar.setNavigationOnClickListener(null)
                    }
                    FragmentCallback.REMINDER_FRAGMENT_EDIT -> {
                        // Hide FloatingActionButton on edit page
                        fab.visibility = View.GONE

                        // Add Back to Home navigation
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
                        toolbar.setNavigationOnClickListener {
                            navigateToFragment(FragmentCallback.REMINDER_FRAGMENT_LIST)
                        }
                    }
                }
            }
        }
    }
}