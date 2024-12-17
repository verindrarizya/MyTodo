package com.verindrzya.todo.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.verindrzya.todo.core.domain.feature.add.AddTodoUseCase
import com.verindrzya.todo.core.domain.feature.add.UpdateTodoUseCase
import com.verindrzya.todo.core.domain.feature.detail.GetDetailTodoUseCase
import com.verindrzya.todo.core.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoAddUpdateViewModel @Inject constructor(
    private val getDetailTodoUseCase: com.verindrzya.todo.core.domain.feature.detail.GetDetailTodoUseCase,
    private val addTodoUseCase: com.verindrzya.todo.core.domain.feature.add.AddTodoUseCase,
    private val updateTodoUseCase: com.verindrzya.todo.core.domain.feature.add.UpdateTodoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val todoId: LiveData<Int> = savedStateHandle.getLiveData("id", 0)
    val todoDetail: LiveData<com.verindrzya.todo.core.domain.model.Todo> = todoId.switchMap {
        getDetailTodoUseCase(it)
    }

    fun updateTodo(
        title: String,
        description: String,
        priorityLevel: String
    ) {
        val currentTodo = todoDetail.value?.copy(
            title = title,
            description = description,
            priorityLevel = priorityLevel
        )

        currentTodo?.let {
            viewModelScope.launch {
                updateTodoUseCase(it)
            }
        }
    }

    fun addTodo(
        title: String,
        description: String,
        priorityLevel: String
    ) {
        val newTodo = com.verindrzya.todo.core.domain.model.Todo(
            title = title,
            description = description,
            priorityLevel = priorityLevel
        )

        viewModelScope.launch {
            addTodoUseCase(newTodo)
        }
    }
}