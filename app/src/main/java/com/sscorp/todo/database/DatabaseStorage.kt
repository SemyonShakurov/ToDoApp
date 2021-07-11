package com.sscorp.todo.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sscorp.todo.App

@Database(
    version = 1,
    entities = [
        NoteEntity::class,
    ],
)
abstract class DatabaseStorage : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        val instance: DatabaseStorage by lazy {
            Room.databaseBuilder(
                App.getAppContext(),
                DatabaseStorage::class.java,
                "notes.db"
            ).build()
        }
    }
}