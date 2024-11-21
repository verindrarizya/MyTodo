package com.verindrzya.todo.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.verindrzya.todo.core.data.local.dao.TodoDao
import com.verindrzya.todo.core.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 1, exportSchema = true)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

}