package com.gofedora.myreminder

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gofedora.myreminder.fragments.AlarmListFragment
import com.gofedora.myreminder.fragments.EditAlarmFragment
import com.gofedora.myreminder.fragments.FragmentCallback
import com.gofedora.myreminder.pickers.DatePickerFragment
import com.gofedora.myreminder.pickers.TimePickerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    FragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = AlarmListFragment()
        fragment.arguments = Bundle()
        fragment.setFragmentActionListener(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        fab.setOnClickListener {
            val editFragment = EditAlarmFragment().setFragmentActionListener(this)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack("AlarmFragments")
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
                Toast.makeText(this, "Insert Dummy Data!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.actionDeleteAll -> {
                Toast.makeText(this, "Delete All Entries!", Toast.LENGTH_SHORT).show()
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
            FragmentCallback.ALARM_CLICKED -> {
                val fragment = EditAlarmFragment().setFragmentActionListener(this)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("AlarmFragments")
                    .commit()
                fab.visibility = View.GONE
            }
            FragmentCallback.DATE_PICKER_CLICKED -> {
                DatePickerFragment(bundle.getString(FragmentCallback.DATE))
                    .setFragmentActionListener(this)
                    .show(supportFragmentManager, "datePicker")
            }
            FragmentCallback.TIME_PICKER_CLICKED -> {
                TimePickerFragment(bundle.getString(FragmentCallback.TIME))
                    .setFragmentActionListener(this)
                    .show(supportFragmentManager, "timePicker")
            }
            FragmentCallback.DATE_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: " + bundle.getSerializable(FragmentCallback.DATE))
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as EditAlarmFragment
                fragment.onFragmentActionPerformed(bundle)
            }
            FragmentCallback.TIME_SELECTED -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: " + bundle.getSerializable(FragmentCallback.TIME))
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as EditAlarmFragment
                fragment.onFragmentActionPerformed(bundle)
            }
            FragmentCallback.SAVE_ALARM -> {
                Log.e(getString(R.string.LogTag), "In MainActivity: Save Alarm")
                val fragment = AlarmListFragment().setFragmentActionListener(this)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()

                fab.visibility = View.VISIBLE
            }
        }
    }
}