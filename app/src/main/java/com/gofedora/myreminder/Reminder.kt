package com.gofedora.myreminder

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "reminder_table")
data class Reminder (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String = "",
    var time: Date = getDefaultDate(),
    var occasion: Int = 0,
    var created_at: Date = Date(),
    var updated_at: Date = Date()
): Serializable {
    companion object {
        /**
         * Get the occasionId based on the position of the element in the occasion spinner dropdown
         */
        fun getOccasionId(context: Context?, position: Int): Int {
            context?.let {
                val keys = it.resources.getIntArray(R.array.occasion_keys)

                if (position >= 0 && position < keys.size)
                    return keys[position]
            }

            return 0
        }

        /**
         * Get human readable occasion value from occasion Id of a reminder
         */
        fun getOccasionValue(context: Context?, id: Int): String {
            context?.let {
                val values = it.resources.getStringArray(R.array.occasion_values)
                val keys = it.resources.getIntArray(R.array.occasion_keys)

                val index = keys.indexOf(id)
                if (index >= 0 && index < values.size)
                    return values[index].toString()
            }

            return ""
        }

        /**
         * Generates a default date/time to tomorrow at 10:10 AM
         */
        fun getDefaultDate(): Date {
            return Calendar.getInstance().apply {
                add(Calendar.DATE, 1)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 10)
            }.time
        }
    }
}