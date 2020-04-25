package com.gofedora.myreminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gofedora.myreminder.data.ReminderRepository
import com.gofedora.myreminder.data.ReminderRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application): AndroidViewModel(application) {

    val allReminders: LiveData<List<Reminder>>
    private val repository: ReminderRepository

    init {
        val reminderDao = ReminderRoomDatabase.getDatabase(application, viewModelScope).reminderDao()
        repository = ReminderRepository(reminderDao)
        allReminders = repository.allReminders
    }

    fun insert(reminder: Reminder) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reminder)
    }

    fun update(reminder: Reminder) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(reminder)
    }

    fun delete(reminder: Reminder) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(reminder)
    }

    fun insertMultiple(reminders: List<Reminder>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMultiple(reminders)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}