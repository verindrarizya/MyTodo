package com.verindrzya.todo.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.verindrzya.todo.core.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun insertItem(item: Todo)

    suspend fun updateItem(item: Todo)

    suspend fun deleteItem(item: Todo)

    fun getItem(id: Int): LiveData<Todo>

    fun getItems(pagingConfig: PagingConfig): Flow<PagingData<Todo>>

}