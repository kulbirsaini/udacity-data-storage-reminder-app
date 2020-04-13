package com.gofedora.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlarmListFragment : Fragment() {

    private lateinit var callback: FragmentCallback

    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.alarm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmsList = view.findViewById<RecyclerView>(R.id.alarmsList)
        alarmsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AlarmsAdapter(MainActivity.ALARMS, callback)
        }
    }
}
