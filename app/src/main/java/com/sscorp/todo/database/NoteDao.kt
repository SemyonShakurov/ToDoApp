package com.sscorp.todo.database

import androidx.room.*

@Dao
interface NoteDao {

    @Query("select * from notes")
    suspend fun all(): List<NoteEntity>

    @Delete
    suspend fun delete(note: NoteEntity)

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("select id from notes where syncState='deleted'")
    suspend fun getDeleted(): List<Int>

    @Query("select * from notes where syncState='other'")
    suspend fun getOther(): List<NoteEntity>

    @Query("delete from notes where syncState='deleted'")
    suspend fun clearDeleteStates()

    @Query("update notes set syncState = 'synced' where syncState='other'")
    suspend fun clearOthers()
}