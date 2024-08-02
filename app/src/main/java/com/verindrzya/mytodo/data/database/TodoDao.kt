package com.verindrzya.mytodo.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface TodoDao {

    @Insert
    suspend fun insertItem(item: Todo)

    @Update
    suspend fun updateItem(item: Todo)

    @Delete
    suspend fun deleteItem(item: Todo)

    @Query("SELECT * FROM todos WHERE id = :id")
    fun getItem(id: Int): LiveData<Todo>

    // The Int type parameter tells Room to use a PositionalDataSource object
    @Query("SELECT * FROM todos")
    fun getItems(): PagingSource<Int, Todo>

}