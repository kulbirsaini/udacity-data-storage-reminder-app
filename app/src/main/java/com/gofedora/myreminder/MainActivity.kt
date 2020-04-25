package com.gofedora.myreminder

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.gofedora.myreminder.fragments.FragmentCallback
import com.gofedora.myreminder.fragments.ReminderEditFragment
import com.gofedora.myreminder.fragments.ReminderListFragment
import com.gofedora.myreminder.pickers.DatePickerFragment
import com.gofedora.myreminder.pickers.TimePickerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentCallback {

    companion object {
        private const val BACKSTACK_TAG = "ReminderFragments"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Initiate a draw the reminder list on initial load
        val fragment = ReminderListFragment().apply {
            setFragmentActionListener(this@MainActivity)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        // Attach onClickListener to FloatingActionButton to switch to edit page
        fab.setOnClickListener {
            val editFragment = ReminderEditFragment().apply {
                setFragmentActionListener(this@MainActivity)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack(BACKSTACK_TAG)
                .commit()

            // Hide FloatingActionButton on edit page
            fab.visibility = View.GONE
        }
    }

    /**
     * Override onOptionsItemSelected to handle menu item clicks
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
                deleteAlertDialog(R.string.delete_all_dialog_title, R.string.delete_all_dialog_message, DialogInterface.OnClickListener { _, _ ->
                    ReminderViewModel(application).deleteAll()
                    Toast.makeText(this, getString(R.string.reminder_deleted_all), Toast.LENGTH_SHORT).show()
                })
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
                val fragment = ReminderEditFragment().apply {
                    setFragmentActionListener(this@MainActivity)
                    arguments = bundle
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(BACKSTACK_TAG) // Add to back stack so that user can navigate back to reminder list
                    .commit()

                // Hide FloatingActionButton
                fab.visibility = View.GONE
            }
            // Date was clicked on edit page, initiate and show DatePicker
            FragmentCallback.DATE_CLICKED -> {
                DatePickerFragment().apply {
                    setFragmentActionListener(this@MainActivity)
                    setDate(bundle.getString(FragmentCallback.DATE))
                }.show(supportFragmentManager, "datePicker")
            }
            // Time was clicked on edit page, initiate and show TimePicker
            FragmentCallback.TIME_CLICKED -> {
                TimePickerFragment().apply {
                    setTime(bundle.getString(FragmentCallback.TIME))
                    setFragmentActionListener(this@MainActivity)
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

                // Flush the FragmentManager backstack to clear any fragments
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                // Time to switch back to reminder list
                val fragment = ReminderListFragment().apply {
                    setFragmentActionListener(this@MainActivity)
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()

                // Show the FloatingActionButton
                fab.visibility = View.VISIBLE
            }
            // Delete Icon clicked
            FragmentCallback.DELETE_REMINDER_CLICKED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Delete Reminder")

                val reminder = bundle.getSerializable(FragmentCallback.REMINDER) as Reminder?

                // We need to something only if we actually have a reminder
                reminder?.let { thisReminder ->
                    // Show confirmation dialog before performing the actual delete action
                    deleteAlertDialog(R.string.delete_dialog_title, R.string.delete_dialog_message, DialogInterface.OnClickListener { _, _ ->
                        ReminderViewModel(application).delete(thisReminder)
                        Toast.makeText(this@MainActivity, getString(R.string.reminder_deleted), Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }

    /**
     * General purpose function to initiate and show AlertDialog
     */
    private fun deleteAlertDialog(titleResource: Int, messageResource: Int, positiveCallback: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(getString(titleResource))
            .setMessage(getString(messageResource))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, positiveCallback)
            .setNegativeButton(android.R.string.no, null)
            .show()
    }
}