package com.sscorp.todo.database

import androidx.room.Room
import com.sscorp.todo.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTasksDao(): TasksDao {
        return Room.databaseBuilder(
            App.getApplicationContext(),
            TasksDatabase::class.java,
            "tasks.db"
        ).build().tasksDao()
    }
}