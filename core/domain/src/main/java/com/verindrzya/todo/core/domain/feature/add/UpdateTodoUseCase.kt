package com.verindrzya.todo.core.domain.feature.add

import com.verindrzya.todo.core.domain.model.Todo
import com.verindrzya.todo.core.domain.repository.TodoRepository
import javax.inject.Inject

class UpdateTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(todo: Todo) {
        todoRepository.updateItem(todo)
    }

}