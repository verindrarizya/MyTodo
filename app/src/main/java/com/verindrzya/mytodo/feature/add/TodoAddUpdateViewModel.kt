package com.verindrzya.mytodo.feature.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.verindrzya.mytodo.core.domain.feature.add.AddTodoUseCase
import com.verindrzya.mytodo.core.domain.feature.add.UpdateTodoUseCase
import com.verindrzya.mytodo.core.domain.feature.detail.GetDetailTodoUseCase
import com.verindrzya.mytodo.core.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoAddUpdateViewModel @Inject constructor(
    private val getDetailTodoUseCase: GetDetailTodoUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val todoId: LiveData<Int> = savedStateHandle.getLiveData("id", 0)
    val todoDetail: LiveData<Todo> = todoId.switchMap {
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
        val newTodo = Todo(
            title = title,
            description = description,
            priorityLevel = priorityLevel
        )

        viewModelScope.launch {
            addTodoUseCase(newTodo)
        }
    }
}