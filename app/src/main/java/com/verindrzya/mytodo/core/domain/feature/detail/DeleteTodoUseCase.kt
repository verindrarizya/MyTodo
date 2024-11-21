package com.verindrzya.mytodo.core.domain.feature.detail

import com.verindrzya.mytodo.core.domain.model.Todo
import com.verindrzya.mytodo.core.domain.repository.TodoRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo) {
        todoRepository.deleteItem(todo)
    }

}