package com.verindrzya.todo.core.domain.feature.list

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.verindrzya.todo.core.domain.model.Todo
import com.verindrzya.todo.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    operator fun invoke(pagingConfig: PagingConfig): Flow<PagingData<Todo>> {
        return todoRepository.getItems(pagingConfig)
    }
}