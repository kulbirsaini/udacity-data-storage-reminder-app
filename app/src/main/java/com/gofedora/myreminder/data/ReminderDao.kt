package com.gofedora.myreminder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gofedora.myreminder.Reminder

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder_table")
    fun getReminders(): LiveData<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reminder: Reminder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultiple(reminders: List<Reminder>)

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("DELETE FROM reminder_table")
    suspend fun deleteAll()

}