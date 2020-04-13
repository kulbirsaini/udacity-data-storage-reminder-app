package com.gofedora.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.edit_alarm.view.*

class EditAlarmFragment: Fragment() {

    companion object {
        const val ALARM = "ALARM"
    }

    private lateinit var callback: FragmentCallback

    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_alarm, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarm = arguments?.getSerializable(FragmentCallback.ALARM) as Alarm?

        if (alarm != null) {
            view.title.append(alarm.title)
        }
    }
}