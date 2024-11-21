package com.verindrzya.mytodo.core.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.verindrzya.mytodo.core.data.local.entity.TodoEntity

interface TodoLocalDataSource {

    suspend fun insertItem(item: TodoEntity)

    suspend fun updateItem(item: TodoEntity)

    suspend fun deleteItem(item: TodoEntity)

    fun getItem(id: Int): LiveData<TodoEntity>

    fun getItems(): PagingSource<Int, TodoEntity>

}