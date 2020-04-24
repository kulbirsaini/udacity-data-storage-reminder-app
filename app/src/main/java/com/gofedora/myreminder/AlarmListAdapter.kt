package com.gofedora.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gofedora.myreminder.fragments.FragmentCallback
import kotlinx.android.synthetic.main.alarm.view.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmListAdapter(private val callback: FragmentCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var alarms = emptyList<Alarm>()

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.alarm, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val alarm = alarms[position]

        holder.itemView.alarmTitle.text = alarm.title
        holder.itemView.alarmDate.text = SimpleDateFormat("EEE, d MMM, yyyy", Locale.US).format(alarm.time)
        holder.itemView.alarmTime.text = SimpleDateFormat("hh:mm a", Locale.US).format(alarm.time)
        holder.itemView.alarmOccasion.text = Alarm.getOccasionValue(holder.itemView.context, alarm.occasion)

        holder.itemView.alarmDeleteIcon.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Delete button clicked!", Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {
            this.callback.onActionPerformed(Bundle().apply {
                putInt(FragmentCallback.ACTION_KEY, FragmentCallback.ALARM_CLICKED)
                putSerializable(FragmentCallback.ALARM, alarm)
            })
        }
    }

    override fun getItemCount() = alarms.size

    fun setAlarms(alarms: List<Alarm>) {
        this.alarms = alarms
        notifyDataSetChanged()
    }
}