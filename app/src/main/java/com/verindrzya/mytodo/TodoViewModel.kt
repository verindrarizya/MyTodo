package com.verindrzya.mytodo

import androidx.lifecycle.*
import androidx.paging.toLiveData
import com.verindrzya.mytodo.data.TodoRepository
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.data.database.TodoDao
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository): ViewModel() {

    private val todoId: MutableLiveData<Int> = MutableLiveData()
    val todoItem = Transformations.switchMap(todoId) { id ->
        todoRepository.getItem(id)
    }

    val todoList get() = todoRepository.getItems()

    fun getItem(id: Int) {
        todoId.value = id
    }

    fun insertItem(title: String, description: String, priorityLevel: String) {
        viewModelScope.launch {
            val item = Todo(title = title, description = description, priorityLevel = priorityLevel)
            todoRepository.insertItem(item)
        }
    }

    fun updateItem(id: Int, title: String, description: String, priorityLevel: String) {
        viewModelScope.launch {
            val item = Todo(id, title, description, priorityLevel)
            todoRepository.updateItem(item)
        }
    }

    fun deleteItem(item: Todo) {
        viewModelScope.launch {
            todoRepository.deleteItem(item)
        }
    }
}