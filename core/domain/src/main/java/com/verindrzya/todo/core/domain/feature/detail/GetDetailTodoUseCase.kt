package com.verindrzya.todo.core.domain.feature.detail

import androidx.lifecycle.LiveData
import com.verindrzya.todo.core.domain.model.Todo
import com.verindrzya.todo.core.domain.repository.TodoRepository
import javax.inject.Inject

class GetDetailTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    operator fun invoke(id: Int): LiveData<Todo> {
        return todoRepository.getItem(id)
    }
}