package com.gofedora.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.alarm.view.*
import java.io.Serializable

class AlarmsAdapter(private val alarms: ArrayList<Alarm>, private val callback: FragmentCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.alarm, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val alarm = alarms[position]

        holder.itemView.alarmTitle.text = alarm.title
        holder.itemView.setOnClickListener {
            callback.onActionPerformed(Bundle().apply {
                putInt(FragmentCallback.ACTION_KEY, FragmentCallback.ALARM_CLICKED)
                putSerializable(FragmentCallback.ALARM, alarm as Serializable)
            })
        }
    }

    override fun getItemCount() = alarms.size
}