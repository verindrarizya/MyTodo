package com.verindrzya.todo.core.data

import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.verindrzya.todo.core.data.local.TodoLocalDataSource
import com.verindrzya.todo.core.data.local.entity.toEntity
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val todoLocalDataSource: TodoLocalDataSource
) : com.verindrzya.todo.core.domain.repository.TodoRepository {

    override suspend fun insertItem(item: com.verindrzya.todo.core.domain.model.Todo) {
        todoLocalDataSource.insertItem(item.toEntity())
    }

    override suspend fun updateItem(item: com.verindrzya.todo.core.domain.model.Todo) {
        todoLocalDataSource.updateItem(item.toEntity())
    }

    override suspend fun deleteItem(item: com.verindrzya.todo.core.domain.model.Todo) {
        todoLocalDataSource.deleteItem(item.toEntity())
    }

    override fun getItem(id: Int) = todoLocalDataSource.getItem(id).map { it.toDomain() }

    override fun getItems(
        pagingConfig: PagingConfig
    ) = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            todoLocalDataSource.getItems()
        }
    ).flow
        .map { pagingData ->
            pagingData.map { todoEntity ->
                todoEntity.toDomain()
            }
        }

}