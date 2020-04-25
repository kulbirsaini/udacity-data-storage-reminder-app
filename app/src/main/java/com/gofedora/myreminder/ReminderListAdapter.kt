package com.gofedora.myreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gofedora.myreminder.fragments.FragmentCallback
import kotlinx.android.synthetic.main.reminder.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var reminders = emptyList<Reminder>()
    private lateinit var callback: FragmentCallback

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun setFragmentActionListener(callback: FragmentCallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.reminder, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reminder = reminders[position]

        holder.itemView.titleView.text = reminder.title
        holder.itemView.dateView.text = SimpleDateFormat("EEE, d MMM, yyyy", Locale.US).format(reminder.time)
        holder.itemView.timeView.text = SimpleDateFormat("hh:mm a", Locale.US).format(reminder.time)
        holder.itemView.occasionView.text = Reminder.getOccasionValue(holder.itemView.context, reminder.occasion)

        holder.itemView.deleteIcon.setOnClickListener {
            this.callback.onActionPerformed(Bundle().apply {
                putInt(FragmentCallback.ACTION_KEY, FragmentCallback.DELETE_REMINDER_CLICKED)
                putSerializable(FragmentCallback.REMINDER, reminder)
            })
        }

        holder.itemView.setOnClickListener {
            this.callback.onActionPerformed(Bundle().apply {
                putInt(FragmentCallback.ACTION_KEY, FragmentCallback.REMINDER_CLICKED)
                putSerializable(FragmentCallback.REMINDER, reminder)
            })
        }
    }

    override fun getItemCount() = reminders.size

    fun setReminders(reminders: List<Reminder>) {
        this.reminders = reminders
        notifyDataSetChanged()
    }
}