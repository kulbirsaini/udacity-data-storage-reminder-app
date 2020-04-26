package com.gofedora.myreminder.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gofedora.myreminder.MainActivity
import com.gofedora.myreminder.R
import com.gofedora.myreminder.ReminderListAdapter
import com.gofedora.myreminder.ReminderViewModel
import kotlinx.android.synthetic.main.reminder_list.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ReminderListFragment : Fragment() {

    private lateinit var reminderListAdapter: ReminderListAdapter

    /**
     * Override onCreate so that we can enable fragment specific menu items
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Announce that this fragment has its own menu
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.reminder_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        reminderListAdapter = ReminderListAdapter()

        // Observe changes to allReminders and announce the fresh list to ReminderListAdapter
        reminderViewModel.allReminders.observe(viewLifecycleOwner, Observer { reminders ->
            reminders?.let {
                reminderListAdapter.setReminders(reminders)
            }
        })

        reminderList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderListAdapter
        }
    }

    /**
     * Override onActivityCreated to handle orientation changes or other activity restarts
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set callback for RecyclerViewAdapter
        reminderListAdapter.setFragmentActionListener(activity as MainActivity)
        (activity as MainActivity).onActionPerformed(Bundle().apply {
            putInt(FragmentCallback.ACTION_KEY, FragmentCallback.REMINDER_FRAGMENT_RENDERED)
            putInt(FragmentCallback.FRAGMENT_TYPE, FragmentCallback.REMINDER_FRAGMENT_LIST)
        })
    }

    /**
     * Override onCreateOptionsMenu to inflate menu for this fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Override onPrepareOptionsMenu to dynamically enable/disable certain menu items
     */
    override fun onPrepareOptionsMenu(menu: Menu) {
        val reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        reminderViewModel.allReminders.observe(viewLifecycleOwner, Observer { reminders ->
            reminders?.let {
                val hasReminders = reminders.isNotEmpty()

                // Enable Delete All menu item only when we have reminders
                menu.findItem(R.id.actionDeleteAll).isEnabled = hasReminders

                // Enable Insert Dummy Data menu item only when we don't have any reminders
                menu.findItem(R.id.actionInsertDummyData).isEnabled = !hasReminders
            }
        })

        super.onPrepareOptionsMenu(menu)
    }
}