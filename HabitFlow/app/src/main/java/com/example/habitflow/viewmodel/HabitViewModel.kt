package com.example.habitflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habitflow.data.Habit
import com.example.habitflow.data.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.content.Context
import androidx.work.*
import com.example.habitflow.notifications.ReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _habitList = MutableStateFlow<List<Habit>>(emptyList())
    val habitList: StateFlow<List<Habit>> = _habitList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allHabits.collect { habits ->
                _habitList.value = habits
            }
        }
    }

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            repository.insert(habit)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.update(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.delete(habit)
        }
    }

    fun deleteAllHabits() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun scheduleDailyReminder(context: Context, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // Si la hora ya pasó, agenda para el día siguiente
            }
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("habit_reminder")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
    fun onReminderTimeSelected(context: Context, hour: Int, minute: Int) {
        scheduleDailyReminder(context, hour, minute)
    }

}