package com.verindrzya.mytodo.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.verindrzya.todo.core.domain.feature.detail.DeleteTodoUseCase
import com.verindrzya.todo.core.domain.feature.list.GetListTodoUseCase
import com.verindrzya.todo.core.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val getListTodoUseCase: GetListTodoUseCase,
    private val deleteTodoUseCase: com.verindrzya.todo.core.domain.feature.detail.DeleteTodoUseCase,
) : ViewModel() {
    val todoList: Flow<PagingData<com.verindrzya.todo.core.domain.model.Todo>>
        get() = getListTodoUseCase(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            )
        )

    fun deleteTodo(todo: com.verindrzya.todo.core.domain.model.Todo) {
        viewModelScope.launch {
            deleteTodoUseCase(todo)
        }
    }
}