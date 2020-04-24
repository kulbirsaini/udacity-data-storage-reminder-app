package com.gofedora.myreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gofedora.myreminder.Alarm
import com.gofedora.myreminder.converters.DateConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AlarmRoomDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    private class AlarmDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.alarmDao())
                }
            }
        }

        suspend fun populateDatabase(alarmDao: AlarmDao) {
            alarmDao.deleteAll()
            alarmDao.insert(Alarm(title = "First Alarm"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AlarmRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AlarmRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AlarmRoomDatabase::class.java, "alarm_database")
                    .addCallback(AlarmDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
