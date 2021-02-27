package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeAndroidTestDataSource : ReminderDataSource{

    var reminders = mutableListOf<ReminderDTO>()
    var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (shouldReturnError) {
            Result.Error("Test Exception")
        } else {
            Result.Success(reminders)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return if (shouldReturnError) {
            Result.Error("Test Exception")
        } else {
            val reminder = reminders.find { it.id == id }
            reminder?.let {
                return Result.Success(reminder)
            }
            return Result.Error("Reminder not found")
        }
    }

    override suspend fun deleteAllReminders() {
        reminders.clear()
    }
}