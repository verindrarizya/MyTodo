package com.verindrzya.mytodo.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.verindrzya.todo.core.domain.feature.detail.DeleteTodoUseCase
import com.verindrzya.todo.core.domain.feature.detail.GetDetailTodoUseCase
import com.verindrzya.todo.core.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val getDetailTodoUseCase: com.verindrzya.todo.core.domain.feature.detail.GetDetailTodoUseCase,
    private val deleteTodoUseCase: com.verindrzya.todo.core.domain.feature.detail.DeleteTodoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val todoId: LiveData<Int> = savedStateHandle.getLiveData("id")

    val todoDetail: LiveData<com.verindrzya.todo.core.domain.model.Todo> = todoId.switchMap {
        getDetailTodoUseCase(it)
    }

    fun deleteTodo() {
        viewModelScope.launch {
            todoDetail.value?.let {
                deleteTodoUseCase(it)
            }
        }
    }
}