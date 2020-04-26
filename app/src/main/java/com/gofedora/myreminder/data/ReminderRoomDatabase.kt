package com.gofedora.myreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gofedora.myreminder.Reminder
import com.gofedora.myreminder.converters.DateConverter

/**
 * Copied from https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#11
 * Not a freaking clue!
 */
@Database(entities = [Reminder::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ReminderRoomDatabase: RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderRoomDatabase? = null

        fun getDatabase(context: Context): ReminderRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ReminderRoomDatabase::class.java, "reminder_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}