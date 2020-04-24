package com.gofedora.myreminder.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gofedora.myreminder.Alarm
import com.gofedora.myreminder.AlarmListAdapter
import com.gofedora.myreminder.AlarmViewModel
import com.gofedora.myreminder.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlarmListFragment : Fragment() {

    private lateinit var callback: FragmentCallback
    private lateinit var alarmViewModel: AlarmViewModel

    fun setFragmentActionListener(callback: FragmentCallback): AlarmListFragment {
        this.callback = callback
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.alarm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        val alarmsList = view.findViewById<RecyclerView>(R.id.alarmsList)
        val alarmAdapter = AlarmListAdapter(this.callback)

        alarmViewModel.allAlarms.observe(viewLifecycleOwner, Observer { alarms ->
            alarms?.let { alarmAdapter.setAlarms(alarms) }
        })

        alarmsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alarmAdapter
        }

        when (arguments?.getInt(FragmentCallback.ACTION_KEY)) {
            FragmentCallback.SAVE_ALARM -> {
                val alarm = arguments?.getSerializable(FragmentCallback.ALARM) as Alarm?

                if (alarm != null) {
                    alarmViewModel.insert(alarm)
                    Toast.makeText(context, "Saved Alarm!", Toast.LENGTH_LONG).show()
                }

                arguments = Bundle()
            }
        }
    }
}
