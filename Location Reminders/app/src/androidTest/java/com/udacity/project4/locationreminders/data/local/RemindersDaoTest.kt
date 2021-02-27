package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    // TODO: Add testing implementation to the RemindersDao.kt
    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() =
        database.close()

    @Test
    fun insertReminderAndGetById()= runBlockingTest {
        val reminder = ReminderDTO("title","desc","location",0.0,0.0)
        database.reminderDao().saveReminder(reminder)
        val savedReminder = database.reminderDao().getReminderById(reminder.id)

        assertThat<ReminderDTO>(savedReminder as ReminderDTO, notNullValue())
        assertThat(savedReminder.id, `is`(reminder.id))
        assertThat(savedReminder.title, `is`(reminder.title))
        assertThat(savedReminder.description, `is`(reminder.description))
        assertThat(savedReminder.location, `is`(reminder.location))
        assertThat(savedReminder.latitude, `is`(reminder.latitude))
        assertThat(savedReminder.longitude, `is`(reminder.longitude))
    }

    @Test
    fun saveGetAndDeleteReminder() = runBlockingTest {
        val reminder = ReminderDTO("title","desc","location",0.0,0.0)

        database.reminderDao().saveReminder(reminder)
        val reminders = database.reminderDao().getReminders()
        assertThat(reminders.isNotEmpty(), `is`(true))

        database.reminderDao().deleteAllReminders()
        val remindersAfterDeletion = database.reminderDao().getReminders()
        assertThat(remindersAfterDeletion.isEmpty(), `is`(true))
    }
}