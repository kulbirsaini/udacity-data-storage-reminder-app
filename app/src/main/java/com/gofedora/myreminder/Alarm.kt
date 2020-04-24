package com.gofedora.myreminder

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "alarm_table")
data class Alarm (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String = "",
    var time: Date = getDefaultDate(),
    var occasion: Int = 0,
    var created_at: Date = Date()
): Serializable {
    companion object {
        fun getOccasionId(context: Context?, position: Int): Int {
            context?.let {
                val keys = it.resources.getStringArray(R.array.occasion_keys)

                if (position >= 0 && position < keys.size)
                    return keys[position].toInt()
            }

            return 0
        }

        fun getOccasionValue(context: Context?, id: Int): String {
            context?.let {
                val values = it.resources.getStringArray(R.array.occasion_values)
                val keys = it.resources.getStringArray(R.array.occasion_keys)

                val index = keys.indexOf(id.toString())
                if (index >= 0 && index < values.size)
                    return values[index].toString()
            }

            return ""
        }

        fun getDefaultDate(): Date {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, 1)
            cal.set(Calendar.HOUR_OF_DAY, 10)
            cal.set(Calendar.MINUTE, 10)

            return cal.time
        }
    }
}