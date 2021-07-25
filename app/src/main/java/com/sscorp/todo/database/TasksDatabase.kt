package com.sscorp.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 2)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao
}