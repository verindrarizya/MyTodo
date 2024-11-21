package com.verindrzya.todo.di

import android.content.Context
import androidx.room.Room
import com.verindrzya.todo.core.data.TodoRepositoryImpl
import com.verindrzya.todo.core.data.database.TodoDatabase
import com.verindrzya.todo.core.data.local.TodoLocalDataSource
import com.verindrzya.todo.core.data.local.TodoLocalDataSourceImpl
import com.verindrzya.todo.core.data.local.dao.TodoDao
import com.verindrzya.todo.core.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase =
        Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()

    @Provides
    fun provideDao(todoDatabase: TodoDatabase): TodoDao = todoDatabase.todoDao()

    @Provides
    fun provideTodoLocalDataSource(todoDao: TodoDao): TodoLocalDataSource =
        TodoLocalDataSourceImpl(todoDao)

    @Provides
    fun provideTodoRepository(todoLocalDataSource: TodoLocalDataSource): TodoRepository =
        TodoRepositoryImpl(todoLocalDataSource)
}