package com.verindrzya.mytodo.core.data

import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.verindrzya.mytodo.core.data.local.TodoLocalDataSource
import com.verindrzya.mytodo.core.data.local.entity.toEntity
import com.verindrzya.mytodo.core.domain.model.Todo
import com.verindrzya.mytodo.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val todoLocalDataSource: TodoLocalDataSource
) : TodoRepository {

    override suspend fun insertItem(item: Todo) {
        todoLocalDataSource.insertItem(item.toEntity())
    }

    override suspend fun updateItem(item: Todo) {
        todoLocalDataSource.updateItem(item.toEntity())
    }

    override suspend fun deleteItem(item: Todo) {
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