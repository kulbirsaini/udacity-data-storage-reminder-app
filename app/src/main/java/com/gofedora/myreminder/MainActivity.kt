package com.gofedora.myreminder

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gofedora.myreminder.fragments.ReminderListFragment
import com.gofedora.myreminder.fragments.ReminderEditFragment
import com.gofedora.myreminder.fragments.FragmentCallback
import com.gofedora.myreminder.pickers.DatePickerFragment
import com.gofedora.myreminder.pickers.TimePickerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = ReminderListFragment().apply {
            setFragmentActionListener(this@MainActivity)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        fab.setOnClickListener {
            val editFragment = ReminderEditFragment().apply {
                setFragmentActionListener(this@MainActivity)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack(getString(R.string.reminder_fragment_backstack))
                .commit()

            fab.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.actionInsertDummyData -> {
                val reminders = resources.getStringArray(R.array.dummy_reminders).map {
                    Reminder(title = it)
                }
                ReminderViewModel(application).insertMultiple(reminders)
                Toast.makeText(this, "Added Dummy Reminders!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.actionDeleteAll -> {
                ReminderViewModel(application).deleteAll()
                Toast.makeText(this, "Deleted All Reminders!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        fab.visibility = View.VISIBLE
        super.onBackPressed()
    }

    override fun onActionPerformed(bundle: Bundle) {
        Log.e(getString(R.string.LogTag), "Action: " + bundle.getInt(FragmentCallback.ACTION_KEY).toString())

        when (bundle.getInt(FragmentCallback.ACTION_KEY)) {
            FragmentCallback.REMINDER_CLICKED -> {
                val fragment = ReminderEditFragment().apply {
                    setFragmentActionListener(this@MainActivity)
                    arguments = bundle
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(getString(R.string.reminder_fragment_backstack))
                    .commit()

                fab.visibility = View.GONE
            }
            FragmentCallback.DATE_PICKER_CLICKED -> {
                DatePickerFragment().apply {
                    setFragmentActionListener(this@MainActivity)
                    setDate(bundle.getString(FragmentCallback.DATE))
                }.show(supportFragmentManager, "datePicker")
            }
            FragmentCallback.TIME_PICKER_CLICKED -> {
                TimePickerFragment().apply {
                    setTime(bundle.getString(FragmentCallback.TIME))
                    setFragmentActionListener(this@MainActivity)
                }.show(supportFragmentManager, "timePicker")
            }
            FragmentCallback.DATE_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: " + bundle.getSerializable(FragmentCallback.DATE))
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ReminderEditFragment
                fragment.onFragmentActionPerformed(bundle)
            }
            FragmentCallback.TIME_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: " + bundle.getSerializable(FragmentCallback.TIME))
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ReminderEditFragment
                fragment.onFragmentActionPerformed(bundle)
            }
            FragmentCallback.SAVE_REMINDER_CLICKED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Save Reminder")

                val reminder = bundle.getSerializable(FragmentCallback.REMINDER) as Reminder?
                reminder?.let {
                    if (it.id > 0) {
                        ReminderViewModel(application).update(it)
                        Toast.makeText(this, "Reminder Updated!", Toast.LENGTH_LONG).show()
                    } else {
                        ReminderViewModel(application).insert(it)
                        Toast.makeText(this, "Reminder Saved!", Toast.LENGTH_LONG).show()
                    }
                }

                val fragment = ReminderListFragment().apply {
                    setFragmentActionListener(this@MainActivity)
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()

                fab.visibility = View.VISIBLE
            }
            FragmentCallback.DELETE_REMINDER_CLICKED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Delete Reminder")
                val reminder = bundle.getSerializable(FragmentCallback.REMINDER) as Reminder?
                reminder?.let {
                    ReminderViewModel(application).delete(it)
                    Toast.makeText(this, "Reminder Deleted!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}