package com.verindrzya.todo.core.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.verindrzya.todo.core.data.local.entity.TodoEntity

@Dao
interface TodoDao {

    @Insert
    suspend fun insertItem(item: TodoEntity)

    @Update
    suspend fun updateItem(item: TodoEntity)

    @Delete
    suspend fun deleteItem(item: TodoEntity)

    @Query("SELECT * FROM todos WHERE id = :id")
    fun getItem(id: Int): LiveData<TodoEntity>

    // The Int type parameter tells Room to use a PositionalDataSource object
    @Query("SELECT * FROM todos")
    fun getItems(): PagingSource<Int, TodoEntity>

}