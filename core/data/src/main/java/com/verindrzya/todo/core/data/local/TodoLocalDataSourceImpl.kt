package com.verindrzya.todo.core.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.verindrzya.todo.core.data.local.dao.TodoDao
import com.verindrzya.todo.core.data.local.entity.TodoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoLocalDataSourceImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoLocalDataSource {
    override suspend fun insertItem(item: TodoEntity) {
        todoDao.insertItem(item)
    }

    override suspend fun updateItem(item: TodoEntity) {
        todoDao.updateItem(item)
    }

    override suspend fun deleteItem(item: TodoEntity) {
        todoDao.deleteItem(item)
    }

    override fun getItem(id: Int): LiveData<TodoEntity> {

        if (id == 0) {
            val emptyTodo = TodoEntity()
            return MutableLiveData(emptyTodo)
        }

        return todoDao.getItem(id)
    }

    override fun getItems(): PagingSource<Int, TodoEntity> {
        return todoDao.getItems()
    }


}