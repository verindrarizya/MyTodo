package com.verindrzya.mytodo.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.verindrzya.mytodo.core.domain.feature.detail.DeleteTodoUseCase
import com.verindrzya.mytodo.core.domain.feature.list.GetListTodoUseCase
import com.verindrzya.mytodo.core.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val getListTodoUseCase: GetListTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
) : ViewModel() {
    val todoList: Flow<PagingData<Todo>>
        get() = getListTodoUseCase(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            )
        )

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            deleteTodoUseCase(todo)
        }
    }
}