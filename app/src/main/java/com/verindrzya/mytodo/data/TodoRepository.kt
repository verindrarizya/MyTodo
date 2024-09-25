package com.verindrzya.mytodo.data

import com.verindrzya.mytodo.constant.PriorityLevel
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.data.database.TodoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    val todoDao: TodoDao
) {

    companion object {
        private const val PAGED_SIZE_VALUE = 10
    }

    suspend fun insertItem(item: Todo) {
        todoDao.insertItem(item)
    }

    suspend fun updateItem(item: Todo) {
        todoDao.updateItem(item)
    }

    suspend fun deleteItem(item: Todo) {
        todoDao.deleteItem(item)
    }

    fun getItem(id: Int) = todoDao.getItem(id)

    fun getItems() = todoDao.getItems()

    fun getLimitedItems(
        limit: Int,
        priorityLevel: String
    ): Flow<List<Todo>> {
        return if (priorityLevel.equals(PriorityLevel.All.name, true)) {
            todoDao.getLimitedItems(limit)
        } else {
            todoDao.getLimitedItems(limit, priorityLevel)
        }
    }

}