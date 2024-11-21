package com.verindrzya.mytodo.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.verindrzya.mytodo.core.domain.feature.detail.DeleteTodoUseCase
import com.verindrzya.mytodo.core.domain.feature.detail.GetDetailTodoUseCase
import com.verindrzya.mytodo.core.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val getDetailTodoUseCase: GetDetailTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val todoId: LiveData<Int> = savedStateHandle.getLiveData("id")

    val todoDetail: LiveData<Todo> = todoId.switchMap {
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