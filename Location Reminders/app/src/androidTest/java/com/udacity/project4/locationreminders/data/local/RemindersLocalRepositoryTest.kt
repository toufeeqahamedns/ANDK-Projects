package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    // TODO: Add testing implementation to the RemindersLocalRepository.kt
    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var remindersDao: RemindersDao

    private lateinit var repository: RemindersLocalRepository

    val reminder1 =
        ReminderDTO("title1", "description1", "U of I", 40.1019728891, -88.2271671295)
    val reminder2 =
        ReminderDTO("title2", "description2", "Carle", 40.1169726776, -88.2155799866)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runBlocking {

        remindersDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        remindersDao = remindersDatabase.reminderDao()

        repository = RemindersLocalRepository(remindersDao)

        repository.saveReminder(reminder1)
        repository.saveReminder(reminder2)
    }

    @After
    fun tearDown() = remindersDatabase.close()

    @Test
    fun getAllReminders() = runBlocking {
        val result = repository.getReminders() as Result.Success
        assertThat(result.data, IsEqual(listOf(reminder1, reminder2)))
    }

    @Test
    fun saveAndGetReminderByID() = runBlocking {
        val reminderRetrieved = repository.getReminder(reminder1.id) as Result.Success<ReminderDTO>
        val data = reminderRetrieved.data
        assertThat(data, `is`(reminder1))
    }

    @Test
    fun deleteAllReminders() = runBlocking {
        repository.deleteAllReminders()
        val result = repository.getReminders() as Result.Success
        assertThat(result.data.isEmpty(), `is`(true))
    }

    @Test
    fun getReminder_NotFoundError()=runBlocking{
        repository.deleteAllReminders()
        val result = repository.getReminder(reminder1.id) as Result.Error
        assertThat(result.message.toString(), `is`("Reminder not found!"))
    }

}