package com.verindrzya.mytodo.di

import com.verindrzya.mytodo.data.TodoRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TodoListWidgetEntryPoint {
    fun getTodoRepository(): TodoRepository
}