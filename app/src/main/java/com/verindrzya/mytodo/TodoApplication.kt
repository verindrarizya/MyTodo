package com.verindrzya.mytodo

import android.app.Application
import com.verindrzya.mytodo.data.TodoRepository
import com.verindrzya.mytodo.data.database.TodoDatabase

class TodoApplication: Application() {
    val todoRepository: TodoRepository by lazy {
        val database = TodoDatabase.getDatabase(this)
        val todoDao = database.todoDao()
        TodoRepository(todoDao)
    }
}