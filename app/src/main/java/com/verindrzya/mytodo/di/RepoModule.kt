package com.verindrzya.mytodo.di

import android.content.Context
import androidx.room.Room
import com.verindrzya.mytodo.data.database.TodoDao
import com.verindrzya.mytodo.data.database.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

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
}