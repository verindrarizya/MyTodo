package com.verindrzya.mytodo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.toLiveData
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.data.database.TodoDao

class TodoRepository(
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

    fun getItems() = Pager(
        config = PagingConfig(
            pageSize = PAGED_SIZE_VALUE,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            todoDao.getItems()
        }
    )

}