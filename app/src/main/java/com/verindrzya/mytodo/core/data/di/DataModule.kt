package com.verindrzya.mytodo.core.data.di

import android.content.Context
import androidx.room.Room
import com.verindrzya.mytodo.core.data.TodoRepositoryImpl
import com.verindrzya.mytodo.core.data.database.TodoDatabase
import com.verindrzya.mytodo.core.data.local.TodoLocalDataSource
import com.verindrzya.mytodo.core.data.local.TodoLocalDataSourceImpl
import com.verindrzya.mytodo.core.data.local.dao.TodoDao
import com.verindrzya.mytodo.core.domain.repository.TodoRepository
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