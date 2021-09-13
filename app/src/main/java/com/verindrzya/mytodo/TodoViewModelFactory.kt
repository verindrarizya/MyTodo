package com.verindrzya.mytodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.verindrzya.mytodo.data.TodoRepository
import com.verindrzya.mytodo.data.database.TodoDao

class TodoViewModelFactory(private val todoRepository: TodoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(todoRepository) as T
        }

        throw IllegalArgumentException("ViewModel Class Not Found")
    }
}