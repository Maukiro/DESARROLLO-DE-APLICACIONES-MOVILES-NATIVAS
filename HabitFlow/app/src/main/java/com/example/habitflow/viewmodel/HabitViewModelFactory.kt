package com.example.habitflow.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habitflow.data.HabitDatabase
import com.example.habitflow.data.HabitRepository

class HabitViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = HabitDatabase.getDatabase(context).habitDao()
        val repository = HabitRepository(dao)

        @Suppress("UNCHECKED_CAST")
        return HabitViewModel(repository) as T
    }
}