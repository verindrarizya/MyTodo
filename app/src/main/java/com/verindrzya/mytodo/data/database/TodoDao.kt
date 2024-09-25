package com.verindrzya.mytodo.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM todos WHERE priority_level = :priorityLevel ORDER BY id DESC LIMIT :limit")
    fun getLimitedItems(
        limit: Int,
        priorityLevel: String
    ): Flow<List<Todo>>

    @Query("SELECT * FROM todos ORDER BY id DESC LIMIT :limit")
    fun getLimitedItems(
        limit: Int
    ): Flow<List<Todo>>
}