package com.gofedora.myreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gofedora.myreminder.Reminder
import com.gofedora.myreminder.converters.DateConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ReminderRoomDatabase: RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    private class ReminderDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    clearDatabase(database.reminderDao())
                }
            }
        }

        suspend fun clearDatabase(reminderDao: ReminderDao) {
            reminderDao.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ReminderRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ReminderRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ReminderRoomDatabase::class.java, "reminder_database")
                    .addCallback(ReminderDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}