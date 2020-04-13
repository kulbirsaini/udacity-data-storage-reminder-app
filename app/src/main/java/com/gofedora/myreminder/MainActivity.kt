package com.gofedora.myreminder

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentCallback {

    companion object {
        val ALARMS = arrayListOf(
            Alarm("Alarm 1"),
            Alarm("Alarm 2"),
            Alarm("Alarm 3"),
            Alarm("Alarm 4"),
            Alarm("Alarm 5"),
            Alarm("Alarm 6"),
            Alarm("Alarm 7"),
            Alarm("Alarm 8"),
            Alarm("Alarm 9"),
            Alarm("Alarm 10"),
            Alarm("Alarm 11")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = AlarmListFragment()
        fragment.setFragmentActionListener(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        fab.setOnClickListener { view ->
            val editFragment = EditAlarmFragment()
            editFragment.arguments = Bundle().apply {
                putSerializable(EditAlarmFragment.ALARM, null)
            }
            editFragment.setFragmentActionListener(this)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack("AlarmFragments")
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Settings Clicked!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActionPerformed(bundle: Bundle) {
        Log.e("AlarmLog", "Action: " + bundle.getInt(FragmentCallback.ACTION_KEY).toString())

        when (bundle.getInt(FragmentCallback.ACTION_KEY)) {
            FragmentCallback.ALARM_CLICKED -> {
                val fragment = EditAlarmFragment()
                fragment.arguments = bundle
                fragment.setFragmentActionListener(this)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("AlarmFragments")
                    .commit()
            }
        }
    }
}