package com.verindrzya.mytodo.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.verindrzya.mytodo.core.data.local.dao.TodoDao
import com.verindrzya.mytodo.core.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 1, exportSchema = true)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

}