package com.verindrzya.mytodo.core.domain.model

data class Todo(
    val id: Int = 0,
    val title: String,
    val description: String,
    val priorityLevel: String
)
