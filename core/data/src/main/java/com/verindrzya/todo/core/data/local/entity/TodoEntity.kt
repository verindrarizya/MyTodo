package com.verindrzya.todo.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.verindrzya.todo.core.domain.model.Todo

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "priority_level")
    val priorityLevel: String = ""
) {
    fun toDomain() = com.verindrzya.todo.core.domain.model.Todo(
        id = id,
        title = title,
        description = description,
        priorityLevel = priorityLevel
    )
}

fun com.verindrzya.todo.core.domain.model.Todo.toEntity() = TodoEntity(
    id = id,
    title = title,
    description = description,
    priorityLevel = priorityLevel
)
