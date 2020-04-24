package com.gofedora.myreminder.data

import androidx.lifecycle.LiveData
import com.gofedora.myreminder.Alarm
import java.util.*

class AlarmRepository(private val alarmDao: AlarmDao) {
    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAlarms()

    suspend fun insert(alarm: Alarm) {
        alarm.created_at = Date()

        if (alarm.title.isNotEmpty() && alarm.occasion >= 0 && alarm.time > Date()) {
            alarmDao.insert(alarm)
        }
    }
}