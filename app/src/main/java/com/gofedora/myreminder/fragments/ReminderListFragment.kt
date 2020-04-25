package com.gofedora.myreminder.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gofedora.myreminder.R
import com.gofedora.myreminder.ReminderListAdapter
import com.gofedora.myreminder.ReminderViewModel
import kotlinx.android.synthetic.main.reminder_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ReminderListFragment : Fragment() {

    private lateinit var callback: FragmentCallback

    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        reminderViewModel.allReminders.observe(viewLifecycleOwner, Observer { reminders ->
            reminders?.let {
                menu.findItem(R.id.actionDeleteAll).isEnabled = reminders.isNotEmpty()
            }
        })

        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.reminder_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        val reminderListAdapter = ReminderListAdapter().apply {
            setFragmentActionListener(this@ReminderListFragment.callback)
        }

        reminderViewModel.allReminders.observe(viewLifecycleOwner, Observer { reminders ->
            reminders?.let {
                reminderListAdapter.setReminders(reminders)
            }
        })

        remindersList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderListAdapter
        }
    }
}