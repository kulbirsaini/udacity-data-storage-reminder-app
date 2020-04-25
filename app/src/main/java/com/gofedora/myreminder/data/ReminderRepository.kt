package com.gofedora.myreminder.data

import androidx.lifecycle.LiveData
import com.gofedora.myreminder.Reminder
import java.util.*

class ReminderRepository(private val reminderDao: ReminderDao) {
    val allReminders: LiveData<List<Reminder>> = reminderDao.getReminders()

    suspend fun insert(reminder: Reminder) {
        // Validate data once again before inserting
        if (reminder.title.isNotEmpty() && reminder.occasion >= 0 && reminder.time > Date()) {
            // Set created_at to now
            reminder.created_at = Date()

            reminderDao.insert(reminder)
        }
    }

    suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

    suspend fun update(reminder: Reminder) {
        // Validate data before updating
        if (reminder.title.isNotEmpty() && reminder.occasion >= 0 && reminder.time > Date()) {
            // Set updated_at to now
            reminder.updated_at = Date()

            reminderDao.update(reminder)
        }
    }

    suspend fun insertMultiple(reminders: List<Reminder>) {
        reminderDao.insertMultiple(reminders)
    }

    suspend fun deleteAll() {
        reminderDao.deleteAll()
    }
}