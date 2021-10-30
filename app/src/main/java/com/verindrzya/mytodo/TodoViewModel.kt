package com.verindrzya.mytodo

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.paging.toLiveData
import com.verindrzya.mytodo.data.TodoRepository
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.data.database.TodoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepository: TodoRepository): ViewModel() {

    private val todoId: MutableLiveData<Int> = MutableLiveData()
    val todoItem = Transformations.switchMap(todoId) { id ->
        todoRepository.getItem(id)
    }

    val todoList
        get() = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                todoRepository.getItems()
            }
        ).flow


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